package com.terminalarrow.app.feature.editor

sealed interface EditorUiState {
    data class Success(val content: String, val isSaving: Boolean) : EditorUiState
    data object Loading : EditorUiState
    data class Error(val message: String) : EditorUiState
}

sealed interface EditorUiEvent {
    data class LoadFile(val context: android.content.Context, val remotePath: String) : EditorUiEvent
    data class SetContent(val content: String) : EditorUiEvent
    data class SaveFile(val context: android.content.Context, val onComplete: () -> Unit) : EditorUiEvent
}

sealed interface EditorUiEffect {
    data class ShowSnackbar(val message: String) : EditorUiEffect
}
