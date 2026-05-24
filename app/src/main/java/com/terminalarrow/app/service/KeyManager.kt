package com.terminalarrow.app.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.security.KeyPairGenerator
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun generateKeyPair(alias: String, algorithm: String = "RSA"): String {
        val keyDir = File(context.filesDir, "keys")
        if (!keyDir.exists()) keyDir.mkdirs()

        val kpg = KeyPairGenerator.getInstance(algorithm)
        if (algorithm == "RSA") {
            kpg.initialize(2048, SecureRandom())
        }
        val kp = kpg.generateKeyPair()

        val privateKeyFile = File(keyDir, "$alias.pem")
        val publicKeyFile = File(keyDir, "$alias.pub")

        privateKeyFile.writeBytes(kp.private.encoded)
        publicKeyFile.writeBytes(kp.public.encoded)

        return privateKeyFile.absolutePath
    }

    fun listKeys(): List<String> {
        val keyDir = File(context.filesDir, "keys")
        return keyDir.listFiles()?.filter { it.extension == "pem" }?.map { it.nameWithoutExtension } ?: emptyList()
    }

    fun deleteKey(alias: String) {
        val keyDir = File(context.filesDir, "keys")
        File(keyDir, "$alias.pem").delete()
        File(keyDir, "$alias.pub").delete()
    }
}
