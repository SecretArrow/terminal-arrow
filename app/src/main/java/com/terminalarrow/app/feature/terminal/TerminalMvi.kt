package com.terminalarrow.app.feature.terminal

sealed interface TerminalUiState {
    data class Success(
        val sessions: Map<String, String>,
        val activeSession: String,
        val suggestions: List<String>,
        val searchQuery: String
    ) : TerminalUiState
    data object Loading : TerminalUiState
    data class Error(val message: String) : TerminalUiState
}

sealed interface TerminalUiEvent {
    data class Connect(val profile: com.terminalarrow.app.data.ConnectionProfile, val id: String = "primary") : TerminalUiEvent
    data class SendCommand(val command: String, val id: String? = null) : TerminalUiEvent
    data class SetActiveSession(val id: String) : TerminalUiEvent
    data class OnInputChange(val id: String, val text: String) : TerminalUiEvent
    data class ResizeTerminal(val id: String, val cols: Int, val rows: Int) : TerminalUiEvent
    data class PerformSearch(val id: String, val query: String) : TerminalUiEvent
    data class SpecialKey(val id: String, val key: String) : TerminalUiEvent
    data class ExportOutput(val id: String, val context: android.content.Context, val onComplete: (String) -> Unit) : TerminalUiEvent
}

sealed interface TerminalUiEffect {
    data class ShowSnackbar(val message: String) : TerminalUiEffect
    data class PlayVibration(val duration: Long = 100) : TerminalUiEffect
}
