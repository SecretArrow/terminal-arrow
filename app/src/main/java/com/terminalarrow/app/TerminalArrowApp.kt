package com.terminalarrow.app

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltAndroidApp
class TerminalArrowApp : Application() {

    override fun onCreate() {
        super.onCreate()
        installGlobalExceptionHandler()
    }

    private fun installGlobalExceptionHandler() {
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                Log.e(TAG, "Uncaught exception in thread ${thread.name}", throwable)
                writeCrashLog(thread, throwable)
            } catch (logging: Throwable) {
                Log.e(TAG, "Failed to write crash log", logging)
            } finally {
                // Defer to the previous handler so the process actually dies and the system
                // can show the ANR / crash UI rather than leaving the app in a half-dead state.
                previous?.uncaughtException(thread, throwable)
            }
        }
    }

    private fun writeCrashLog(thread: Thread, throwable: Throwable) {
        val dir = File(filesDir, "crash-logs").apply { if (!exists()) mkdirs() }
        // Cap at 10 most recent logs to keep storage bounded.
        dir.listFiles()
            ?.sortedByDescending { it.lastModified() }
            ?.drop(9)
            ?.forEach { runCatching { it.delete() } }
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val out = File(dir, "crash_$stamp.txt")
        PrintWriter(out).use { writer ->
            writer.println("Terminal Arrow crash")
            writer.println("Timestamp: ${Date()}")
            writer.println("Thread: ${thread.name}")
            writer.println("Build: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL} / Android ${android.os.Build.VERSION.RELEASE}")
            writer.println()
            throwable.printStackTrace(writer)
        }
    }

    companion object {
        private const val TAG = "TerminalArrow"
    }
}
