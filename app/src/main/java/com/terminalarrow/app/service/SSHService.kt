package com.terminalarrow.app.service

import android.content.Context
import android.util.Log
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.ForwardingRule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.connection.channel.direct.LocalPortForwarder
import net.schmizz.sshj.connection.channel.direct.Parameters
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.connection.channel.forwarded.RemotePortForwarder
import net.schmizz.sshj.connection.channel.forwarded.SocketForwardingConnectListener
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import java.io.File
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Owns the lifecycle of every active SSH shell session. Designed so that
 * partial failures (auth issues, dropped sockets) never crash the app - every
 * external entry point is wrapped in try/catch and reports back through
 * [onOutput] or returns silently.
 */
@Singleton
class SSHService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val sessions = ConcurrentHashMap<String, SessionContainer>()

    fun getContext(): Context = context

    fun isConnected(sessionId: String): Boolean = sessions.containsKey(sessionId)

    data class SessionContainer(
        val client: SSHClient,
        val shell: Session.Shell,
        val shellStream: OutputStream,
        val scope: CoroutineScope,
        val forwarders: MutableList<AutoCloseable> = mutableListOf()
    )

    /**
     * Open an interactive shell. All errors are surfaced through [onOutput] as
     * a single "Error: ..." line so that the UI can show them without the
     * caller having to know about SSH internals.
     */
    suspend fun connect(
        sessionId: String,
        profile: ConnectionProfile,
        onOutput: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        // If a previous session with this id is around, tear it down first.
        disconnect(sessionId)

        val client = SSHClient().apply {
            addHostKeyVerifier(PromiscuousVerifier())
            connectTimeout = CONNECT_TIMEOUT_MS
            timeout = SOCKET_TIMEOUT_MS
        }
        var tempKeyFile: File? = null
        try {
            client.connect(profile.host, profile.port)

            val pass = profile.password
            val keyMaterial = profile.keyPath
            when {
                !keyMaterial.isNullOrBlank() -> {
                    tempKeyFile = File(context.cacheDir, "key_${System.currentTimeMillis()}_${sessionId.hashCode()}")
                    tempKeyFile.writeText(keyMaterial)
                    val keyProvider = client.loadKeys(tempKeyFile.absolutePath)
                    client.authPublickey(profile.username, keyProvider)
                }
                !pass.isNullOrEmpty() -> {
                    client.authPassword(profile.username, pass)
                }
                else -> {
                    throw IllegalArgumentException("No password or private key provided")
                }
            }

            val session = client.startSession().apply {
                allocatePTY("xterm-256color", 80, 24, 0, 0, emptyMap())
            }
            val shell = session.startShell()
            val outputStream = shell.outputStream

            // Optional port-forwarding rules; failures are non-fatal so the
            // primary shell session always wins.
            val container = SessionContainer(
                client = client,
                shell = shell,
                shellStream = outputStream,
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            )
            sessions[sessionId] = container

            profile.forwardingRules.forEach { rule ->
                runCatching { applyForward(container, rule) }.onFailure {
                    Log.w(TAG, "Forwarding rule ${rule.type}:${rule.localPort} failed", it)
                }
            }

            container.scope.launch {
                val buffer = ByteArray(4096)
                try {
                    val input = shell.inputStream
                    while (isActive) {
                        val read = input.read(buffer)
                        if (read == -1) break
                        if (read > 0) onOutput(String(buffer, 0, read, Charsets.UTF_8))
                    }
                } catch (e: Throwable) {
                    if (isActive) {
                        onOutput("\r\n[connection lost: ${e.message ?: e.javaClass.simpleName}]\r\n")
                    }
                } finally {
                    disconnect(sessionId)
                }
            }
        } catch (e: Throwable) {
            Log.w(TAG, "connect($sessionId) failed", e)
            onOutput("Error: ${e.message ?: e.javaClass.simpleName}\r\n")
            runCatching { client.disconnect() }
        } finally {
            tempKeyFile?.let { runCatching { it.delete() } }
        }
    }

    private fun applyForward(container: SessionContainer, rule: ForwardingRule) {
        when (rule.type.uppercase()) {
            "LOCAL" -> {
                val remoteHost = rule.remoteHost ?: "localhost"
                val remotePort = rule.remotePort ?: rule.localPort
                val params = Parameters(
                    /* localAddress = */ "127.0.0.1",
                    /* localPort = */ rule.localPort,
                    /* remoteHost = */ remoteHost,
                    /* remotePort = */ remotePort
                )
                val serverSocket = ServerSocket().apply {
                    reuseAddress = true
                    bind(InetSocketAddress("127.0.0.1", rule.localPort))
                }
                val forwarder = container.client.newLocalPortForwarder(params, serverSocket)
                // Forwarder.listen() blocks; run it on a daemon thread so the
                // call site stays responsive.
                Thread({
                    runCatching { forwarder.listen() }
                }, "ta-local-fwd-${rule.localPort}").apply {
                    isDaemon = true
                    start()
                }
                container.forwarders.add(AutoCloseable {
                    runCatching { forwarder.close() }
                    runCatching { serverSocket.close() }
                })
                Log.d(TAG, "LOCAL forward 127.0.0.1:${rule.localPort} -> $remoteHost:$remotePort")
            }
            "REMOTE" -> {
                val remoteHost = rule.remoteHost ?: "127.0.0.1"
                val remotePort = rule.remotePort ?: rule.localPort
                val forward = RemotePortForwarder.Forward("0.0.0.0", rule.localPort)
                container.client.remotePortForwarder.bind(
                    forward,
                    SocketForwardingConnectListener(InetSocketAddress(remoteHost, remotePort))
                )
                container.forwarders.add(AutoCloseable {
                    runCatching { container.client.remotePortForwarder.cancel(forward) }
                })
                Log.d(TAG, "REMOTE forward server:${rule.localPort} -> $remoteHost:$remotePort")
            }
            else -> {
                Log.d(TAG, "Skipping unsupported forwarding type ${rule.type}")
            }
        }
    }

    fun resizePty(sessionId: String, cols: Int, rows: Int) {
        runCatching {
            val safeCols = cols.coerceIn(1, 1000)
            val safeRows = rows.coerceIn(1, 500)
            sessions[sessionId]?.shell?.changeWindowDimensions(safeCols, safeRows, 0, 0)
        }
    }

    fun sendCommand(sessionId: String, command: String) {
        val container = sessions[sessionId] ?: return
        container.scope.launch(Dispatchers.IO) {
            try {
                container.shellStream.write(command.toByteArray(Charsets.UTF_8))
                container.shellStream.flush()
            } catch (e: Throwable) {
                Log.w(TAG, "sendCommand($sessionId) failed", e)
            }
        }
    }

    fun disconnect(sessionId: String) {
        val container = sessions.remove(sessionId) ?: return
        container.forwarders.forEach { runCatching { it.close() } }
        runCatching { container.scope.cancel() }
        runCatching { container.shell.close() }
        runCatching { container.client.disconnect() }
    }

    fun disconnectAll() {
        sessions.keys.toList().forEach { disconnect(it) }
    }

    companion object {
        private const val TAG = "SSHService"
        private const val CONNECT_TIMEOUT_MS = 15_000
        private const val SOCKET_TIMEOUT_MS = 30_000
    }
}
