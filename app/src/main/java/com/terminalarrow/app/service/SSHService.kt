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
import kotlinx.coroutines.delay

@Singleton
class SSHService @Inject constructor() {
    private var client: SSHClient? = null
    private var shellStream: OutputStream? = null

    suspend fun connect(profile: com.terminalarrow.app.data.ConnectionProfile, onOutput: (String) -> Unit) = withContext(Dispatchers.IO) {
        var retryCount = 0
        var connected = false
        
        while (retryCount < 3 && !connected) {
            client = SSHClient()
            client?.addHostKeyVerifier(PromiscuousVerifier())
            client?.timeout = 10000
            
            try {
                client?.connect(profile.host, profile.port)
                if (profile.password != null) {
                    client?.authPassword(profile.username, profile.password)
                }
                
                // Port Forwarding
                profile.forwardingRules.forEach { rule ->
                    try {
                        when (rule.type) {
                            "LOCAL" -> {
                                client?.newLocalPortForwarder(Parameters("0.0.0.0", rule.localPort, rule.remoteHost ?: "localhost", rule.remotePort ?: 0), ServerSocket(rule.localPort))?.listen()
                                onOutput("Local Forward: ${rule.localPort} -> ${rule.remoteHost}:${rule.remotePort}\n")
                            }
                            "REMOTE" -> {
                                client?.getRemotePortForwarder()?.bind(
                                    RemotePortForwarder.Forward(rule.remotePort ?: 0),
                                    SocketForwardingConnectListener(InetSocketAddress("localhost", rule.localPort))
                                )
                                onOutput("Remote Forward: ${rule.remotePort} -> localhost:${rule.localPort}\n")
                            }
                        }
                    } catch (e: Exception) {
                        onOutput("Forward Error: ${e.message}\n")
                    }
                }

                val session = client?.startSession()
                session?.allocateDefaultPTY()
                val shell = session?.startShell()
                shellStream = shell?.outputStream
                
                connected = true
                onOutput("Connected to ${profile.host}\n")

                val inputStream = shell?.inputStream
                val buffer = ByteArray(1024)
                var read: Int
                while (inputStream?.read(buffer).also { read = it ?: -1 } != -1) {
                    onOutput(String(buffer, 0, read))
                }
            } catch (e: Exception) {
                retryCount++
                onOutput("Connection attempt $retryCount failed: ${e.message}\n")
                if (retryCount < 3) delay(2000)
            } finally {
                if (!connected) disconnect()
            }
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
