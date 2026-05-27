package com.terminalarrow.app.service

import android.util.Log
import com.terminalarrow.app.data.KnownHost
import com.terminalarrow.app.data.TerminalDao
import kotlinx.coroutines.runBlocking
import net.schmizz.sshj.common.SecurityUtils
import net.schmizz.sshj.transport.verification.HostKeyVerifier
import java.security.PublicKey

/**
 * Trust-On-First-Use host key verifier backed by Room. First time we see a
 * host we record its SHA-256 fingerprint; on every subsequent connection we
 * require the same key. `strictHostKeyChecking` (per-profile) controls
 * whether a mismatch refuses the connection (true) or just logs (false).
 */
class TofuHostKeyVerifier(
    private val dao: TerminalDao,
    private val strict: Boolean
) : HostKeyVerifier {

    override fun verify(host: String, port: Int, key: PublicKey): Boolean {
        val fingerprint = SecurityUtils.getFingerprint(key)
        val type = key.algorithm ?: "unknown"
        val existing = runBlocking { dao.getKnownHost(host, port) }
        return if (existing == null) {
            runBlocking {
                dao.upsertKnownHost(
                    KnownHost(host = host, port = port, keyType = type, fingerprint = fingerprint)
                )
            }
            Log.i(TAG, "TOFU: recorded new host key for $host:$port ($type / $fingerprint)")
            true
        } else if (existing.fingerprint == fingerprint) {
            true
        } else {
            Log.w(TAG, "Host key MISMATCH for $host:$port. Known=${existing.fingerprint}, presented=$fingerprint")
            !strict
        }
    }

    override fun findExistingAlgorithms(host: String, port: Int): MutableList<String> {
        val existing = runBlocking { dao.getKnownHost(host, port) }
        return if (existing != null) mutableListOf(existing.keyType) else mutableListOf()
    }

    companion object {
        private const val TAG = "TofuHostKeyVerifier"
    }
}
