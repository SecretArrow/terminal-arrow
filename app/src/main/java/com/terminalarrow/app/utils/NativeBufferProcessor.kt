package com.terminalarrow.app.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NativeBufferProcessor @Inject constructor() {
    init {
        System.loadLibrary("native-lib")
    }

    external fun processBufferNative(inputText: String): String
}
