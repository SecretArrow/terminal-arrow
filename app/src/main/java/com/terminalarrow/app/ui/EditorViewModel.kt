package com.terminalarrow.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.feature.editor.*
import com.terminalarrow.app.service.SFTPService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val sftpService: SFTPService
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditorUiState>(EditorUiState.Loading)
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<EditorUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<EditorUiEffect> = _uiEffect.receiveAsFlow()

    private var currentRemotePath: String = ""

    fun onEvent(event: EditorUiEvent) {
        when (event) {
            is EditorUiEvent.LoadFile -> loadFile(event.context, event.remotePath)
            is EditorUiEvent.SetContent -> setContent(event.content)
            is EditorUiEvent.SaveFile -> saveFile(event.context, event.onComplete)
        }
    }

    private fun loadFile(context: Context, remotePath: String) {
        currentRemotePath = remotePath
        viewModelScope.launch {
            _uiState.value = EditorUiState.Loading
            try {
                val localFile = File(context.cacheDir, "editor_temp.txt")
                sftpService.downloadFile(remotePath, localFile.absolutePath)
                if (localFile.exists()) {
                    _uiState.value = EditorUiState.Success(localFile.readText(), false)
                } else {
                    _uiState.value = EditorUiState.Error("Failed to download file")
                }
            } catch (e: Exception) {
                _uiState.value = EditorUiState.Error(e.message ?: "Failed to load file")
            }
        }
    }

    private fun setContent(newContent: String) {
        _uiState.update { state ->
            if (state is EditorUiState.Success) state.copy(content = newContent) else state
        }
    }

    private fun saveFile(context: Context, onComplete: () -> Unit) {
        val currentState = _uiState.value as? EditorUiState.Success ?: return
        viewModelScope.launch {
            _uiState.value = currentState.copy(isSaving = true)
            try {
                val localFile = File(context.cacheDir, "editor_temp.txt")
                localFile.writeText(currentState.content)
                sftpService.uploadFile(localFile.absolutePath, currentRemotePath)
                _uiEffect.send(EditorUiEffect.ShowSnackbar("File saved successfully"))
                onComplete()
            } catch (e: Exception) {
                _uiEffect.send(EditorUiEffect.ShowSnackbar("Failed to save file"))
            } finally {
                _uiState.value = currentState.copy(isSaving = false)
            }
        }
    }
}
