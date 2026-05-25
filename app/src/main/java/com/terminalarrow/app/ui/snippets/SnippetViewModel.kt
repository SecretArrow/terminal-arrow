package com.terminalarrow.app.ui.snippets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.data.Snippet
import com.terminalarrow.app.data.TerminalDao
import com.terminalarrow.app.feature.snippets.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnippetViewModel @Inject constructor(
    private val terminalDao: TerminalDao
) : ViewModel() {

    val uiState: StateFlow<SnippetsUiState> = terminalDao.getAllSnippets()
        .map<List<Snippet>, SnippetsUiState> { SnippetsUiState.Success(it) }
        .onStart { emit(SnippetsUiState.Loading) }
        .catch { emit(SnippetsUiState.Error(it.message ?: "Failed to load snippets")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SnippetsUiState.Loading)

    private val _uiEffect = Channel<SnippetsUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<SnippetsUiEffect> = _uiEffect.receiveAsFlow()

    fun onEvent(event: SnippetsUiEvent) {
        when (event) {
            is SnippetsUiEvent.SaveSnippet -> saveSnippet(event.name, event.command)
            is SnippetsUiEvent.DeleteSnippet -> deleteSnippet(event.snippet)
        }
    }

    private fun saveSnippet(name: String, command: String) {
        viewModelScope.launch {
            try {
                terminalDao.insertSnippet(Snippet(name = name, command = command))
                _uiEffect.send(SnippetsUiEffect.ShowSnackbar("Snippet saved"))
            } catch (e: Exception) {
                _uiEffect.send(SnippetsUiEffect.ShowSnackbar("Failed to save snippet"))
            }
        }
    }

    private fun deleteSnippet(snippet: Snippet) {
        viewModelScope.launch {
            try {
                terminalDao.deleteSnippet(snippet)
                _uiEffect.send(SnippetsUiEffect.ShowSnackbar("Snippet deleted"))
            } catch (e: Exception) {
                _uiEffect.send(SnippetsUiEffect.ShowSnackbar("Failed to delete snippet"))
            }
        }
    }
}
