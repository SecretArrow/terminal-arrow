package com.terminalarrow.app.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.RemoteResourceInfo
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import net.schmizz.sshj.xfer.TransferListener
import net.schmizz.sshj.common.StreamCopier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SFTPService @Inject constructor(@dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context) {
    private var client: SSHClient? = null
    private var sftpClient: SFTPClient? = null

    suspend fun connect(host: String, port: Int, user: String, pass: String?, keyPath: String? = null): Boolean = withContext(Dispatchers.IO) {
        try {
            client = SSHClient()
            client?.addHostKeyVerifier(PromiscuousVerifier())
            client?.connect(host, port)
            
            if (!keyPath.isNullOrBlank()) {
                val tempKeyFile = java.io.File(context.cacheDir, "sftp_temp_key_${System.currentTimeMillis()}")
                tempKeyFile.writeText(keyPath)
                val keyProvider = client?.loadKeys(tempKeyFile.absolutePath)
                if (keyProvider != null) client?.authPublickey(user, keyProvider)
                tempKeyFile.delete()
            } else if (pass != null) {
                client?.authPassword(user, pass)
            }
            
            sftpClient = client?.newSFTPClient()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun listFiles(path: String): List<RemoteResourceInfo> = withContext(Dispatchers.IO) {
        try {
            sftpClient?.ls(path) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun setTransferListener(onProgress: (Int) -> Unit) {
        try {
            sftpClient?.fileTransfer?.transferListener = object : TransferListener {
                override fun directory(name: String): TransferListener = this
                override fun file(name: String, size: Long): StreamCopier.Listener {
                    return object : StreamCopier.Listener {
                        override fun reportProgress(transferred: Long) {
                            if (size > 0) {
                                val percent = ((transferred.toDouble() / size) * 100).toInt()
                                onProgress(percent)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Log or handle error
        }
    }

    suspend fun downloadFile(remotePath: String, localPath: String, onProgress: (Int) -> Unit = {}) = withContext(Dispatchers.IO) {
        try {
            setTransferListener(onProgress)
            sftpClient?.get(remotePath, localPath)
        } catch (e: Exception) {
            // Handle error
        } finally {
            try {
                sftpClient?.fileTransfer?.transferListener = null
            } catch (e: Exception) {}
        }
    }

    suspend fun uploadFile(localPath: String, remotePath: String, onProgress: (Int) -> Unit = {}) = withContext(Dispatchers.IO) {
        try {
            setTransferListener(onProgress)
            sftpClient?.put(localPath, remotePath)
        } catch (e: Exception) {
            // Handle error
        } finally {
            try {
                sftpClient?.fileTransfer?.transferListener = null
            } catch (e: Exception) {}
        }
    }

    suspend fun deleteFile(path: String) = withContext(Dispatchers.IO) {
        try {
            sftpClient?.rm(path)
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun renameFile(oldPath: String, newPath: String) = withContext(Dispatchers.IO) {
        try {
            sftpClient?.rename(oldPath, newPath)
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun getRemoteInputStream(path: String): java.io.InputStream? = withContext(Dispatchers.IO) {
        try {
            sftpClient?.open(path)?.let { remoteFile ->
                remoteFile.RemoteFileInputStream()
            }
        } catch (e: Exception) {
            null
        }
    }

    fun disconnect() {
        try {
            sftpClient?.close()
            client?.disconnect()
        } catch (e: Exception) {
            // Ignore on disconnect
        } finally {
            sftpClient = null
            client = null
        }
    }
}
