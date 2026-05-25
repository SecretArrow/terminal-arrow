package com.terminalarrow.app.service

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import net.schmizz.sshj.connection.channel.direct.Parameters
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

@Singleton
class SSHService @Inject constructor(@dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context) {
    private val sessions = ConcurrentHashMap<String, SessionContainer>()

    fun getContext(): android.content.Context = context

    data class SessionContainer(
        val client: SSHClient,
        val shell: net.schmizz.sshj.connection.channel.direct.Session.Shell,
        val shellStream: OutputStream,
        val scope: CoroutineScope
    )

    suspend fun connect(
        sessionId: String,
        profile: com.terminalarrow.app.data.ConnectionProfile,
        onOutput: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val client = SSHClient()
        client.addHostKeyVerifier(PromiscuousVerifier())
        client.timeout = 10000
        
        try {
            client.connect(profile.host, profile.port)
            if (!profile.keyPath.isNullOrBlank()) {
                val tempKeyFile = java.io.File(context.cacheDir, "temp_key_${System.currentTimeMillis()}")
                tempKeyFile.writeText(profile.keyPath)
                val keyProvider = client.loadKeys(tempKeyFile.absolutePath)
                client.authPublickey(profile.username, keyProvider)
                tempKeyFile.delete()
            } else if (profile.password != null) {
                client.authPassword(profile.username, profile.password)
            }

            val session = client.startSession()
            session.allocatePTY("xterm", 80, 24, 0, 0, emptyMap())
            val shell = session.startShell()
            val outputStream = shell.outputStream
            
            val sessionScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            sessions[sessionId] = SessionContainer(client, shell, outputStream, sessionScope)

            sessionScope.launch {
                try {
                    val inputStream = shell.inputStream
                    val buffer = ByteArray(2048)
                    var read: Int = 0
                    while (isActive && inputStream.read(buffer).also { read = it } != -1) {
                        onOutput(String(buffer, 0, read))
                    }
                } catch (e: Exception) {
                    onOutput("\nConnection lost: ${e.message}\n")
                } finally {
                    disconnect(sessionId)
                }
            }
        } catch (e: Exception) {
            onOutput("Error: ${e.message}\n")
        }
    }

    fun resizePty(sessionId: String, cols: Int, rows: Int) {
        sessions[sessionId]?.shell?.changeWindowDimensions(cols, rows, 0, 0)
    }

    fun sendCommand(sessionId: String, command: String) {
        sessions[sessionId]?.let { container ->
            container.scope.launch(Dispatchers.IO) {
                try {
                    container.shellStream.write(command.toByteArray())
                    container.shellStream.flush()
                } catch (e: Exception) {
                    // Ignored or handle connection drop
                }
            }
        }
    }

    fun disconnect(sessionId: String) {
        sessions[sessionId]?.let {
            it.scope.cancel()
            it.client.disconnect()
            sessions.remove(sessionId)
        }
    }

    fun disconnectAll() {
        sessions.keys.forEach { disconnect(it) }
    }
}
