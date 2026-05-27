package com.terminalarrow.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.service.SSHService
import com.terminalarrow.app.service.SFTPService
import com.terminalarrow.app.utils.VibratorHelper
import com.terminalarrow.app.utils.NativeBufferProcessor
import com.terminalarrow.app.feature.terminal.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private const val MAX_BUFFER_LINES = 5000

@HiltViewModel
class TerminalViewModel @Inject constructor(
    private val sshService: SSHService,
    private val sftpService: SFTPService,
    private val vibratorHelper: VibratorHelper,
    private val nativeProcessor: NativeBufferProcessor,
    private val dao: com.terminalarrow.app.data.TerminalDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<TerminalUiState>(
        TerminalUiState.Success(
            sessions = mapOf("primary" to "", "secondary" to ""),
            activeSession = "primary",
            suggestions = emptyList(),
            searchQuery = ""
        )
    )
    val uiState: StateFlow<TerminalUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<TerminalUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<TerminalUiEffect> = _uiEffect.receiveAsFlow()

    fun onEvent(event: TerminalUiEvent) {
        when (event) {
            is TerminalUiEvent.Connect -> connect(event.profile, event.id)
            is TerminalUiEvent.SendCommand -> sendCommand(event.command, event.id)
            is TerminalUiEvent.SetActiveSession -> setActiveSession(event.id)
            is TerminalUiEvent.OnInputChange -> onInputChange(event.id, event.text)
            is TerminalUiEvent.ResizeTerminal -> resizeTerminal(event.id, event.cols, event.rows)
            is TerminalUiEvent.PerformSearch -> performSearch(event.id, event.query)
            is TerminalUiEvent.SpecialKey -> onSpecialKey(event.id, event.key)
            is TerminalUiEvent.ExportOutput -> exportTerminalOutput(event.id, event.context, event.onComplete)
        }
    }

    private fun setActiveSession(id: String) {
        _uiState.update { state ->
            if (state is TerminalUiState.Success) {
                state.copy(activeSession = id)
            } else state
        }
    }

    private fun connect(profile: com.terminalarrow.app.data.ConnectionProfile, id: String) {
        viewModelScope.launch {
            try {
                com.terminalarrow.app.service.SSHForegroundService.start(sshService.getContext())
                sftpService.connect(profile.host, profile.port, profile.username, profile.password, profile.keyPath)

                if (profile.id != 0) {
                    runCatching { dao.markConnected(profile.id, System.currentTimeMillis()) }
                }
                
                sshService.connect(id, profile) { output ->
                    if (output.contains("\u0007")) {
                        viewModelScope.launch { _uiEffect.send(TerminalUiEffect.PlayVibration()) }
                    }
                    
                    val optimized = try {
                        nativeProcessor.processBuffer(output)
                    } catch (e: Exception) {
                        output
                    }

                    _uiState.update { state ->
                        if (state is TerminalUiState.Success) {
                            val newMap = state.sessions.toMutableMap()
                            val currentBuffer = newMap[id] ?: ""
                            val newBuffer = currentBuffer + optimized
                            
                            newMap[id] = trimToLineLimit(newBuffer, MAX_BUFFER_LINES)
                            state.copy(sessions = newMap)
                        } else state
                    }
                }
            } catch (e: Exception) {
                _uiEffect.send(TerminalUiEffect.ShowSnackbar("Connection failed: ${e.message}"))
            }
        }
    }

    private fun onInputChange(id: String, text: String) {
        viewModelScope.launch {
            try {
                if (text.endsWith("/")) {
                    val files = sftpService.listFiles(text)
                    _uiState.update { state ->
                        if (state is TerminalUiState.Success) {
                            state.copy(suggestions = files.map { it.name })
                        } else state
                    }
                }
            } catch (e: Exception) {
                // Ignore autocomplete errors
            }
        }
    }

    private fun sendCommand(command: String, id: String?) {
        val targetId = id ?: (_uiState.value as? TerminalUiState.Success)?.activeSession ?: "primary"
        sshService.sendCommand(targetId, command)
    }

    private fun resizeTerminal(id: String, cols: Int, rows: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sshService.resizePty(id, cols, rows)
            } catch (e: Exception) {}
        }
    }

    private fun performSearch(id: String, query: String) {
        _uiState.update { state ->
            if (state is TerminalUiState.Success) {
                state.copy(searchQuery = query)
            } else state
        }
        if (query.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val buffer = (_uiState.value as? TerminalUiState.Success)?.sessions?.get(id) ?: ""
                    nativeProcessor.fastSearch(buffer, query)
                } catch (e: Exception) {}
            }
        }
    }

    private fun onSpecialKey(id: String, key: String) {
        when (key) {
            "ESC" -> sendCommand("\u001B", id)
            "TAB" -> sendCommand("\t", id)
        }
    }

    private fun exportTerminalOutput(id: String, context: Context, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val buffer = (_uiState.value as? TerminalUiState.Success)?.sessions?.get(id) ?: ""
                val file = File(context.cacheDir, "terminal_${id}_${System.currentTimeMillis()}.txt")
                file.writeText(buffer)
                onComplete(file.absolutePath)
            } catch (e: Exception) {
                _uiEffect.send(TerminalUiEffect.ShowSnackbar("Export failed"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sshService.disconnectAll()
        com.terminalarrow.app.service.SSHForegroundService.stop(sshService.getContext())
    }
}

private fun trimToLineLimit(buffer: String, maxLines: Int): String {
    if (buffer.length < 4096) return buffer
    // Cheap line count via newlines; preserve trailing partial line.
    var newlines = 0
    for (i in buffer.indices) if (buffer[i] == '\n') newlines++
    if (newlines <= maxLines) return buffer
    // Drop the oldest (newlines - maxLines) lines.
    val toDrop = newlines - maxLines
    var i = 0
    var dropped = 0
    while (i < buffer.length && dropped < toDrop) {
        if (buffer[i] == '\n') dropped++
        i++
    }
    return buffer.substring(i)
}
