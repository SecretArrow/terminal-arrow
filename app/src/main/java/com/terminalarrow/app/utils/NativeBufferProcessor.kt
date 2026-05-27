package com.terminalarrow.app.utils

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NativeBufferProcessor @Inject constructor() {

    val available: Boolean

    init {
        var ok = false
        try {
            System.loadLibrary("native-lib")
            ok = true
        } catch (t: Throwable) {
            Log.w(TAG, "Native library 'native-lib' not loadable; falling back to JVM", t)
        }
        available = ok
    }

    private external fun processBufferNative(inputText: String): String
    private external fun fastSearchNative(buffer: String, query: String): String

    fun processBuffer(input: String): String = if (available) {
        runCatching { processBufferNative(input) }.getOrElse { input }
    } else input

    fun fastSearch(buffer: String, query: String): String = if (available) {
        runCatching { fastSearchNative(buffer, query) }.getOrElse {
            jvmFallbackSearch(buffer, query)
        }
    } else jvmFallbackSearch(buffer, query)

    private fun jvmFallbackSearch(buffer: String, query: String): String {
        if (query.isEmpty()) return ""
        return buffer.lineSequence()
            .filter { it.contains(query, ignoreCase = true) }
            .joinToString("\n")
    }

    private companion object { const val TAG = "NativeBufferProcessor" }
}
