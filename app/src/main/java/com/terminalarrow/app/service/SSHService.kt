package com.terminalarrow.app.service

import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.IOUtils
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import net.schmizz.sshj.connection.channel.direct.Parameters
import net.schmizz.sshj.connection.channel.forwarded.RemotePortForwarder
import net.schmizz.sshj.connection.channel.forwarded.SocketForwardingConnectListener
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class SSHService @Inject constructor() {
    private var client: SSHClient? = null
    private var shellStream: OutputStream? = null

    suspend fun connect(profile: com.terminalarrow.app.data.ConnectionProfile, onOutput: (String) -> Unit) = withContext(Dispatchers.IO) {
        client = SSHClient()
        client?.addHostKeyVerifier(PromiscuousVerifier())
        try {
            client?.connect(profile.host, profile.port)
            if (profile.password != null) {
                client?.authPassword(profile.username, profile.password)
            }
            
            // Setup Port Forwarding Rules
            profile.forwardingRules.forEach { rule ->
                try {
                    when (rule.type) {
                        "LOCAL" -> {
                            client?.newLocalPortForwarder(Parameters("0.0.0.0", rule.localPort, rule.remoteHost ?: "localhost", rule.remotePort ?: 0), ServerSocket(rule.localPort))?.listen()
                            onOutput("Local Forward: ${rule.localPort} -> ${rule.remoteHost}:${rule.remotePort}\n")
                        }
                        "DYNAMIC" -> {
                            onOutput("Dynamic Forward (SOCKS) requested on port ${rule.localPort}\n")
                        }
                        "REMOTE" -> {
                            // Using SocketForwardingConnectListener for Remote Port Forwarding
                            client?.getRemotePortForwarder()?.bind(
                                RemotePortForwarder.Forward(rule.remotePort ?: 0),
                                SocketForwardingConnectListener(InetSocketAddress("localhost", rule.localPort))
                            )
                            onOutput("Remote Forward: ${rule.remotePort} -> localhost:${rule.localPort}\n")
                        }
                    }
                } catch (e: Exception) {
                    onOutput("Forward Error (${rule.type}): ${e.message}\n")
                }
            }

            val session = client?.startSession()
            session?.allocateDefaultPTY()
            val shell = session?.startShell()
            
            shellStream = shell?.outputStream
            val inputStream = shell?.inputStream

            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream?.read(buffer).also { read = it ?: -1 } != -1) {
                val output = String(buffer, 0, read)
                onOutput(output)
            }
        } catch (e: Exception) {
            onOutput("Connection Error: ${e.message}\n")
        } finally {
            disconnect()
        }
    }

    fun sendCommand(command: String) {
        shellStream?.write(command.toByteArray())
        shellStream?.flush()
    }

    fun disconnect() {
        client?.disconnect()
        client = null
        shellStream = null
    }
}
