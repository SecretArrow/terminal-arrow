package com.terminalarrow.app.service

import android.util.Log
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.connection.channel.direct.Parameters
import java.io.DataInputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

/**
 * Minimal SOCKS5 proxy (no auth) that tunnels each accepted connection through
 * the given [SSHClient] as a direct-tcpip channel. Matches what Bitvise/OpenSSH
 * expose as "dynamic" port forwarding.
 *
 * Limitations vs a full RFC1928 proxy:
 *  - SOCKS5 only (no SOCKS4)
 *  - CONNECT only (no BIND, no UDP ASSOCIATE)
 *  - No GSSAPI, no username/password auth (NO_AUTH only)
 * This matches how `ssh -D` operates in practice.
 */
class SocksProxy(
    private val ssh: SSHClient,
    private val serverSocket: ServerSocket
) : Runnable {

    override fun run() {
        while (!serverSocket.isClosed) {
            val client = try {
                serverSocket.accept()
            } catch (e: IOException) {
                if (!serverSocket.isClosed) Log.w(TAG, "SOCKS accept failed", e)
                return
            }
            thread(name = "ta-socks-conn", isDaemon = true) {
                runCatching { handle(client) }.onFailure {
                    Log.w(TAG, "SOCKS connection handler failed", it)
                    runCatching { client.close() }
                }
            }
        }
    }

    private fun handle(client: Socket) {
        val input = DataInputStream(client.getInputStream())
        val output = client.getOutputStream()

        // --- Greeting ---
        val ver = input.readUnsignedByte()
        if (ver != 0x05) {
            client.close(); return
        }
        val nMethods = input.readUnsignedByte()
        val methods = ByteArray(nMethods)
        input.readFully(methods)
        // Reply: choose NO_AUTH (0x00) if offered; else 0xFF (none acceptable).
        val pick = if (methods.any { it == 0x00.toByte() }) 0x00 else 0xFF
        output.write(byteArrayOf(0x05, pick.toByte()))
        output.flush()
        if (pick == 0xFF) { client.close(); return }

        // --- Request ---
        if (input.readUnsignedByte() != 0x05) { client.close(); return }
        val cmd = input.readUnsignedByte()
        input.readUnsignedByte() // RSV
        val atyp = input.readUnsignedByte()
        if (cmd != 0x01) {
            // CMD not supported
            writeReply(output, 0x07); client.close(); return
        }
        val host = when (atyp) {
            0x01 -> {
                val ip = ByteArray(4); input.readFully(ip)
                ip.joinToString(".") { (it.toInt() and 0xFF).toString() }
            }
            0x03 -> {
                val len = input.readUnsignedByte()
                val b = ByteArray(len); input.readFully(b)
                String(b, Charsets.US_ASCII)
            }
            0x04 -> {
                val ip = ByteArray(16); input.readFully(ip)
                // Format as IPv6 literal
                buildString {
                    for (i in 0 until 16 step 2) {
                        if (i > 0) append(":")
                        append("%02x%02x".format(ip[i].toInt() and 0xFF, ip[i + 1].toInt() and 0xFF))
                    }
                }
            }
            else -> {
                writeReply(output, 0x08); client.close(); return
            }
        }
        val port = (input.readUnsignedByte() shl 8) or input.readUnsignedByte()

        // Open SSH direct-tcpip channel and bridge the two sockets.
        val channel = try {
            val params = Parameters("127.0.0.1", 0, host, port)
            ssh.newDirectConnection(params.remoteHost, params.remotePort)
        } catch (e: Throwable) {
            Log.w(TAG, "SOCKS CONNECT to $host:$port failed", e)
            writeReply(output, 0x05); client.close(); return
        }
        writeReply(output, 0x00)

        // Bridge bidirectionally.
        thread(name = "ta-socks-up", isDaemon = true) {
            runCatching { client.getInputStream().copyTo(channel.outputStream) }
            runCatching { channel.close() }
            runCatching { client.close() }
        }
        thread(name = "ta-socks-dn", isDaemon = true) {
            runCatching { channel.inputStream.copyTo(client.getOutputStream()) }
            runCatching { channel.close() }
            runCatching { client.close() }
        }
    }

    private fun writeReply(out: java.io.OutputStream, code: Int) {
        // VER REP RSV ATYP=ipv4 0.0.0.0 0
        out.write(byteArrayOf(0x05, code.toByte(), 0x00, 0x01, 0, 0, 0, 0, 0, 0))
        out.flush()
    }

    companion object {
        private const val TAG = "SocksProxy"
    }
}
