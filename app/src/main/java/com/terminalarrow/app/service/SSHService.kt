package com.terminalarrow.app.service

import android.content.Context
import android.util.Log
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.ForwardingRule
import com.terminalarrow.app.data.TerminalDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.connection.channel.direct.Parameters
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.connection.channel.forwarded.RemotePortForwarder
import net.schmizz.sshj.connection.channel.forwarded.SocketForwardingConnectListener
import java.io.File
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SSHService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: TerminalDao
) {

    private val sessions = ConcurrentHashMap<String, SessionContainer>()

    fun getContext(): Context = context
    fun isConnected(sessionId: String): Boolean = sessions.containsKey(sessionId)

    data class SessionContainer(
        val client: SSHClient,
        val shell: Session.Shell,
        val shellStream: OutputStream,
        val scope: CoroutineScope,
        val profile: ConnectionProfile,
        val forwarders: MutableList<AutoCloseable> = mutableListOf()
    )

    suspend fun connect(
        sessionId: String,
        profile: ConnectionProfile,
        onOutput: (String) -> Unit
    ): Unit = withContext(Dispatchers.IO) {
        disconnect(sessionId)
        attemptConnect(sessionId, profile, onOutput, attempt = 0)
    }

    private suspend fun attemptConnect(
        sessionId: String,
        profile: ConnectionProfile,
        onOutput: (String) -> Unit,
        attempt: Int
    ): Unit = withContext(Dispatchers.IO) {
        val client = SSHClient().apply {
            addHostKeyVerifier(TofuHostKeyVerifier(dao, profile.strictHostKeyChecking))
            connectTimeout = CONNECT_TIMEOUT_MS
            timeout = SOCKET_TIMEOUT_MS
            if (profile.useCompression) {
                runCatching { useCompression() }
            }
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

            // SSH keepalive (server-side timeout protection)
            if (profile.keepAliveSeconds > 0) {
                runCatching {
                    client.connection.keepAlive.keepAliveInterval = profile.keepAliveSeconds
                }
            }

            val session = client.startSession().apply {
                allocatePTY("xterm-256color", 80, 24, 0, 0, emptyMap())
            }
            val shell = session.startShell()
            val outputStream = shell.outputStream

            val container = SessionContainer(
                client = client,
                shell = shell,
                shellStream = outputStream,
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                profile = profile
            )
            sessions[sessionId] = container

            profile.forwardingRules.forEach { rule ->
                runCatching { applyForward(container, rule) }.onFailure {
                    Log.w(TAG, "Forwarding ${rule.type}:${rule.localPort} failed", it)
                    onOutput("[forward ${rule.type}:${rule.localPort} failed: ${it.message}]\r\n")
                }
            }

            container.scope.launch {
                val buffer = ByteArray(4096)
                var crashed = false
                try {
                    val input = shell.inputStream
                    while (isActive) {
                        val read = input.read(buffer)
                        if (read == -1) break
                        if (read > 0) onOutput(String(buffer, 0, read, Charsets.UTF_8))
                    }
                } catch (e: Throwable) {
                    crashed = true
                    if (isActive) {
                        onOutput("\r\n[connection lost: ${e.message ?: e.javaClass.simpleName}]\r\n")
                    }
                } finally {
                    disconnect(sessionId)
                    if (crashed && profile.autoReconnect && attempt < MAX_RECONNECT_ATTEMPTS) {
                        onOutput("[auto-reconnect attempt ${attempt + 1}/$MAX_RECONNECT_ATTEMPTS in ${RECONNECT_DELAY_MS / 1000}s]\r\n")
                        delay(RECONNECT_DELAY_MS)
                        attemptConnect(sessionId, profile, onOutput, attempt + 1)
                    }
                }
            }
        } catch (e: Throwable) {
            Log.w(TAG, "connect($sessionId) attempt=$attempt failed", e)
            onOutput("Error: ${e.message ?: e.javaClass.simpleName}\r\n")
            runCatching { client.disconnect() }
            if (profile.autoReconnect && attempt < MAX_RECONNECT_ATTEMPTS) {
                onOutput("[auto-reconnect attempt ${attempt + 1}/$MAX_RECONNECT_ATTEMPTS in ${RECONNECT_DELAY_MS / 1000}s]\r\n")
                delay(RECONNECT_DELAY_MS)
                attemptConnect(sessionId, profile, onOutput, attempt + 1)
            }
        } finally {
            tempKeyFile?.let { runCatching { it.delete() } }
        }
    }

    private fun applyForward(container: SessionContainer, rule: ForwardingRule) {
        when (rule.type.uppercase()) {
            "LOCAL" -> {
                val remoteHost = rule.remoteHost ?: "localhost"
                val remotePort = rule.remotePort ?: rule.localPort
                val params = Parameters("127.0.0.1", rule.localPort, remoteHost, remotePort)
                val serverSocket = ServerSocket().apply {
                    reuseAddress = true
                    bind(InetSocketAddress("127.0.0.1", rule.localPort))
                }
                val forwarder = container.client.newLocalPortForwarder(params, serverSocket)
                Thread({ runCatching { forwarder.listen() } }, "ta-local-fwd-${rule.localPort}").apply {
                    isDaemon = true
                    start()
                }
                container.forwarders.add(AutoCloseable {
                    runCatching { forwarder.close() }
                    runCatching { serverSocket.close() }
                })
                Log.d(TAG, "LOCAL 127.0.0.1:${rule.localPort} -> $remoteHost:$remotePort")
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
                Log.d(TAG, "REMOTE server:${rule.localPort} -> $remoteHost:$remotePort")
            }
            "DYNAMIC" -> {
                // Minimal SOCKS5 proxy: accept localhost connections and tunnel
                // each via direct-tcpip to the destination requested by the client.
                val serverSocket = ServerSocket().apply {
                    reuseAddress = true
                    bind(InetSocketAddress("127.0.0.1", rule.localPort))
                }
                val acceptor = Thread({
                    SocksProxy(container.client, serverSocket).run()
                }, "ta-dynamic-fwd-${rule.localPort}").apply {
                    isDaemon = true
                    start()
                }
                container.forwarders.add(AutoCloseable {
                    runCatching { serverSocket.close() }
                    runCatching { acceptor.interrupt() }
                })
                Log.d(TAG, "DYNAMIC SOCKS proxy on 127.0.0.1:${rule.localPort}")
            }
            else -> Log.d(TAG, "Skipping unsupported forwarding type ${rule.type}")
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
        private const val MAX_RECONNECT_ATTEMPTS = 3
        private const val RECONNECT_DELAY_MS = 5_000L
    }
}
