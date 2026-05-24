package com.terminalarrow.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.service.SFTPService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val sftpService: SFTPService
) : ViewModel() {

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private var currentRemotePath: String = ""

    fun loadFile(context: Context, remotePath: String) {
        currentRemotePath = remotePath
        viewModelScope.launch {
            val localFile = File(context.cacheDir, "editor_temp.txt")
            sftpService.downloadFile(remotePath, localFile.absolutePath)
            _content.value = localFile.readText()
        }
    }

    fun setContent(newContent: String) {
        _content.value = newContent
    }

    fun saveFile(context: Context, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            val localFile = File(context.cacheDir, "editor_temp.txt")
            localFile.writeText(_content.value)
            sftpService.uploadFile(localFile.absolutePath, currentRemotePath)
            _isSaving.value = false
            onComplete()
        }
    }
}
