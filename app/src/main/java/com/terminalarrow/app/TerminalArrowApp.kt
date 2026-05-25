package com.terminalarrow.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TerminalArrowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            android.util.Log.e("TerminalArrow", "CRASH in thread ${thread.name}", throwable)
            // Optionally: Save crash log to file for user feedback
        }
    }
}
