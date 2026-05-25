package com.terminalarrow.app.feature.snippets

import com.terminalarrow.app.data.Snippet

sealed interface SnippetsUiState {
    data class Success(val snippets: List<Snippet>) : SnippetsUiState
    data object Loading : SnippetsUiState
    data class Error(val message: String) : SnippetsUiState
}

sealed interface SnippetsUiEvent {
    data class SaveSnippet(val name: String, val command: String) : SnippetsUiEvent
    data class DeleteSnippet(val snippet: Snippet) : SnippetsUiEvent
}

sealed interface SnippetsUiEffect {
    data class ShowSnackbar(val message: String) : SnippetsUiEffect
}
