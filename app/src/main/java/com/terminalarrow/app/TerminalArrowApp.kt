package com.terminalarrow.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TerminalArrowApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
