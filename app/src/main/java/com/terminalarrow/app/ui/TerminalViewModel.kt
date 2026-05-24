package com.terminalarrow.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.service.SSHService
import com.terminalarrow.app.service.SFTPService
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
    private val sftpService: SFTPService,
    private val vibratorHelper: VibratorHelper,
    private val nativeProcessor: NativeBufferProcessor
) : ViewModel() {

    private val _sessions = MutableStateFlow<Map<String, String>>(mapOf("primary" to "", "secondary" to ""))
    val sessions: StateFlow<Map<String, String>> = _sessions

    private val _activeSession = MutableStateFlow("primary")
    val activeSession: StateFlow<String> = _activeSession

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    fun setActiveSession(id: String) {
        _activeSession.value = id
    }

    fun connect(profile: com.terminalarrow.app.data.ConnectionProfile, id: String = "primary") {
        viewModelScope.launch {
            // Start background persistence service
            com.terminalarrow.app.service.SSHForegroundService.start(sshService.getContext())
            
            // Connect SFTP for smart autocomplete support
            sftpService.connect(profile.host, profile.port, profile.username, profile.password ?: "")
            
            sshService.connect(id, profile) { output ->
                if (output.contains("\u0007")) vibratorHelper.vibrate()
                
                val optimized = nativeProcessor.processBufferNative(output)
                val currentMap = _sessions.value.toMutableMap()
                val newBuffer = (currentMap[id] ?: "") + optimized
                
                // Buffer Optimization: Limit memory usage per session
                currentMap[id] = if (newBuffer.length > 5000) {
                    newBuffer.substring(newBuffer.length - 5000)
                } else {
                    newBuffer
                }
                _sessions.value = currentMap
            }
        }
    }

    fun onInputChange(id: String, text: String) {
        // Smart SFTP-based Autocomplete foundation
        viewModelScope.launch {
            if (text.endsWith("/")) {
                val files = sftpService.listFiles(text)
                _suggestions.value = files.map { it.name }
            }
        }
    }

    fun sendCommand(id: String, command: String) {
        sshService.sendCommand(id, command)
    }

    fun performSearch(id: String, query: String) {
        _searchQuery.value = query
        if (query.isNotBlank()) {
            viewModelScope.launch {
                val results = nativeProcessor.fastSearchNative(_sessions.value[id] ?: "", query)
                // In a full implementation, we'd use these indices for highlighting
                // For now, we update the search query state to trigger UI feedback
            }
        }
    }

    fun onSpecialKey(id: String, key: String) {
        when (key) {
            "ESC" -> sendCommand(id, "\u001B")
            "TAB" -> sendCommand(id, "\t")
        }
    }

    fun exportTerminalOutput(id: String, context: Context, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val file = File(context.cacheDir, "terminal_${id}_${System.currentTimeMillis()}.txt")
            file.writeText(_sessions.value[id] ?: "")
            onComplete(file.absolutePath)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sshService.disconnectAll()
        com.terminalarrow.app.service.SSHForegroundService.stop(sshService.getContext())
    }
}
