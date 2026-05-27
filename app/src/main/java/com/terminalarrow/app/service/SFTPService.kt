package com.terminalarrow.app.service

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.StreamCopier
import net.schmizz.sshj.sftp.RemoteResourceInfo
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import net.schmizz.sshj.xfer.TransferListener
import java.io.File
import java.io.FilterInputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SFTPService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var client: SSHClient? = null
    private var sftpClient: SFTPClient? = null

    suspend fun connect(
        host: String,
        port: Int,
        user: String,
        pass: String?,
        keyPath: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val c = SSHClient().apply {
                addHostKeyVerifier(PromiscuousVerifier())
                connectTimeout = 15_000
                timeout = 30_000
                connect(host, port)
            }
            client = c

            if (!keyPath.isNullOrBlank()) {
                val tempKeyFile = File(context.cacheDir, "sftp_key_${System.currentTimeMillis()}")
                try {
                    tempKeyFile.writeText(keyPath)
                    val keyProvider = c.loadKeys(tempKeyFile.absolutePath)
                    c.authPublickey(user, keyProvider)
                } finally {
                    runCatching { tempKeyFile.delete() }
                }
            } else if (!pass.isNullOrEmpty()) {
                c.authPassword(user, pass)
            } else {
                throw IllegalArgumentException("No password or private key provided")
            }

            sftpClient = c.newSFTPClient()
            true
        } catch (t: Throwable) {
            Log.w(TAG, "SFTP connect failed", t)
            runCatching { client?.disconnect() }
            client = null
            sftpClient = null
            false
        }
    }

    suspend fun listFiles(path: String): List<RemoteResourceInfo> = withContext(Dispatchers.IO) {
        try {
            sftpClient?.ls(path) ?: emptyList()
        } catch (t: Throwable) {
            Log.w(TAG, "SFTP ls($path) failed", t)
            emptyList()
        }
    }

    private fun setTransferListener(onProgress: (Int) -> Unit) {
        try {
            sftpClient?.fileTransfer?.transferListener = object : TransferListener {
                override fun directory(name: String): TransferListener = this
                override fun file(name: String, size: Long): StreamCopier.Listener =
                    StreamCopier.Listener { transferred ->
                        if (size > 0) {
                            val percent = ((transferred.toDouble() / size) * 100).toInt()
                            onProgress(percent.coerceIn(0, 100))
                        }
                    }
            }
        } catch (t: Throwable) {
            Log.w(TAG, "set transfer listener failed", t)
        }
    }

    suspend fun downloadFile(remotePath: String, localPath: String, onProgress: (Int) -> Unit = {}) =
        withContext(Dispatchers.IO) {
            try {
                setTransferListener(onProgress)
                sftpClient?.get(remotePath, localPath)
            } catch (t: Throwable) {
                Log.w(TAG, "SFTP download($remotePath) failed", t)
            } finally {
                runCatching { sftpClient?.fileTransfer?.transferListener = null }
            }
        }

    suspend fun uploadFile(localPath: String, remotePath: String, onProgress: (Int) -> Unit = {}) =
        withContext(Dispatchers.IO) {
            try {
                setTransferListener(onProgress)
                sftpClient?.put(localPath, remotePath)
            } catch (t: Throwable) {
                Log.w(TAG, "SFTP upload($localPath -> $remotePath) failed", t)
            } finally {
                runCatching { sftpClient?.fileTransfer?.transferListener = null }
            }
        }

    suspend fun deleteFile(path: String) = withContext(Dispatchers.IO) {
        try {
            sftpClient?.rm(path)
        } catch (t: Throwable) {
            Log.w(TAG, "SFTP delete($path) failed", t)
        }
    }

    suspend fun renameFile(oldPath: String, newPath: String) = withContext(Dispatchers.IO) {
        try {
            sftpClient?.rename(oldPath, newPath)
        } catch (t: Throwable) {
            Log.w(TAG, "SFTP rename($oldPath -> $newPath) failed", t)
        }
    }

    /**
     * Opens a remote file for reading. The returned [InputStream] owns the
     * underlying [net.schmizz.sshj.sftp.RemoteFile] handle and closes it when the
     * stream is closed — callers MUST close the stream, ideally via `use { }`.
     */
    suspend fun getRemoteInputStream(path: String): InputStream? = withContext(Dispatchers.IO) {
        try {
            val sftp = sftpClient ?: return@withContext null
            val remoteFile = sftp.open(path)
            val inner = remoteFile.RemoteFileInputStream()
            object : FilterInputStream(inner) {
                override fun close() {
                    try {
                        super.close()
                    } finally {
                        runCatching { remoteFile.close() }
                    }
                }
            }
        } catch (t: Throwable) {
            Log.w(TAG, "SFTP open($path) failed", t)
            null
        }
    }

    fun disconnect() {
        try {
            sftpClient?.close()
        } catch (t: Throwable) {
            Log.w(TAG, "SFTP client close failed", t)
        }
        try {
            client?.takeIf { it.isConnected }?.disconnect()
        } catch (t: Throwable) {
            Log.w(TAG, "SSH client disconnect failed", t)
        } finally {
            sftpClient = null
            client = null
        }
    }

    companion object {
        private const val TAG = "SFTPService"
    }
}
