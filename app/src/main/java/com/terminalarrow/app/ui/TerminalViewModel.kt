package com.terminalarrow.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.service.SSHService
import com.terminalarrow.app.utils.VibratorHelper
import com.terminalarrow.app.utils.NativeBufferProcessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TerminalViewModel @Inject constructor(
    private val sshService: SSHService,
    private val vibratorHelper: VibratorHelper,
    private val nativeProcessor: NativeBufferProcessor
) : ViewModel() {

    private val _terminalOutput = MutableStateFlow("")
    val terminalOutput: StateFlow<String> = _terminalOutput

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    fun connect(profile: com.terminalarrow.app.data.ConnectionProfile) {
        viewModelScope.launch {
            sshService.connect(profile) { output ->
                if (output.contains("\u0007")) {
                    vibratorHelper.vibrate()
                }
                // High-performance processing via C++ JNI
                val optimizedOutput = nativeProcessor.processBufferNative(output)
                _terminalOutput.value += optimizedOutput
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun connectLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val process = ProcessBuilder("/system/bin/sh", "-i").redirectErrorStream(true).start()
                val input = process.inputStream
                val buffer = ByteArray(1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    val output = String(buffer, 0, read)
                    if (output.contains("\u0007")) vibratorHelper.vibrate()
                    val optimizedOutput = nativeProcessor.processBufferNative(output)
                    _terminalOutput.value += optimizedOutput
                }
            } catch (e: Exception) {
                _terminalOutput.value += "Local shell error: ${e.message}\n"
            }
        }
    }

    fun onInputChange(text: String) {
        val commands = listOf("ls", "cd", "mkdir", "rm", "cp", "mv", "grep", "cat", "ssh", "sftp")
        _suggestions.value = if (text.isNotEmpty()) {
            commands.filter { it.startsWith(text) }
        } else emptyList()
    }

    fun sendCommand(command: String) {
        sshService.sendCommand(command)
    }

    fun onSpecialKey(key: String) {
        when (key) {
            "ESC" -> sendCommand("\u001B")
            "TAB" -> sendCommand("\t")
        }
    }

    fun exportTerminalOutput(context: Context, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val file = File(context.cacheDir, "terminal_output_${System.currentTimeMillis()}.txt")
            file.writeText(_terminalOutput.value)
            onComplete(file.absolutePath)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sshService.disconnect()
    }
}
