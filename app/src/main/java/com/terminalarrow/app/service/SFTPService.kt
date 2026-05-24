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
        sftpClient?.ls(path) ?: emptyList()
    }

    private fun setTransferListener(onProgress: (Int) -> Unit) {
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
    }

    suspend fun downloadFile(remotePath: String, localPath: String, onProgress: (Int) -> Unit = {}) = withContext(Dispatchers.IO) {
        setTransferListener(onProgress)
        sftpClient?.get(remotePath, localPath)
        sftpClient?.fileTransfer?.transferListener = null
    }

    suspend fun uploadFile(localPath: String, remotePath: String, onProgress: (Int) -> Unit = {}) = withContext(Dispatchers.IO) {
        setTransferListener(onProgress)
        sftpClient?.put(localPath, remotePath)
        sftpClient?.fileTransfer?.transferListener = null
    }

    suspend fun deleteFile(path: String) = withContext(Dispatchers.IO) {
        sftpClient?.rm(path)
    }

    suspend fun renameFile(oldPath: String, newPath: String) = withContext(Dispatchers.IO) {
        sftpClient?.rename(oldPath, newPath)
    }

    suspend fun getRemoteInputStream(path: String): java.io.InputStream? = withContext(Dispatchers.IO) {
        sftpClient?.open(path)?.let { remoteFile ->
            remoteFile.RemoteFileInputStream()
        }
    }

    fun disconnect() {
        sftpClient?.close()
        client?.disconnect()
        sftpClient = null
        client = null
    }
}
