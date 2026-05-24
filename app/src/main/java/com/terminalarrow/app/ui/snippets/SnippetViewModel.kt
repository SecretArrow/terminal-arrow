package com.terminalarrow.app.ui.snippets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.data.Snippet
import com.terminalarrow.app.data.TerminalDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnippetViewModel @Inject constructor(
    private val terminalDao: TerminalDao
) : ViewModel() {

    val snippets: StateFlow<List<Snippet>> = terminalDao.getAllSnippets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveSnippet(name: String, command: String) {
        viewModelScope.launch {
            terminalDao.insertSnippet(Snippet(name = name, command = command))
        }
    }

    fun deleteSnippet(snippet: Snippet) {
        viewModelScope.launch {
            terminalDao.deleteSnippet(snippet)
        }
    }
}
