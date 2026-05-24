package com.terminalarrow.app.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.RemoteResourceInfo
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SFTPService @Inject constructor() {
    private var client: SSHClient? = null
    private var sftpClient: SFTPClient? = null

    suspend fun connect(host: String, port: Int, user: String, pass: String): Boolean = withContext(Dispatchers.IO) {
        try {
            client = SSHClient()
            client?.addHostKeyVerifier(PromiscuousVerifier())
            client?.connect(host, port)
            client?.authPassword(user, pass)
            sftpClient = client?.newSFTPClient()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun listFiles(path: String): List<RemoteResourceInfo> = withContext(Dispatchers.IO) {
        sftpClient?.ls(path) ?: emptyList()
    }

    suspend fun downloadFile(remotePath: String, localPath: String) = withContext(Dispatchers.IO) {
        sftpClient?.get(remotePath, localPath)
    }

    suspend fun uploadFile(localPath: String, remotePath: String) = withContext(Dispatchers.IO) {
        sftpClient?.put(localPath, remotePath)
    }

    fun disconnect() {
        sftpClient?.close()
        client?.disconnect()
        sftpClient = null
        client = null
    }
}
