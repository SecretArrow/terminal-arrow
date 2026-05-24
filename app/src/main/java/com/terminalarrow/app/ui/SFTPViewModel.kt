package com.terminalarrow.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.service.SFTPService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.schmizz.sshj.sftp.RemoteResourceInfo
import javax.inject.Inject

@HiltViewModel
class SFTPViewModel @Inject constructor(
    private val sftpService: SFTPService
) : ViewModel() {

    private val _files = MutableStateFlow<List<RemoteResourceInfo>>(emptyList())
    val files: StateFlow<List<RemoteResourceInfo>> = _files

    private val _currentPath = MutableStateFlow("/")
    val currentPath: StateFlow<String> = _currentPath

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun connectAndList(host: String, port: Int, user: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if (sftpService.connect(host, port, user, pass)) {
                loadPath("/")
            }
            _isLoading.value = false
        }
    }

    fun loadPath(path: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _files.value = sftpService.listFiles(path)
            _currentPath.value = path
            _isLoading.value = false
        }
    }

    fun navigateUp() {
        val parent = _currentPath.value.substringBeforeLast("/", "").ifEmpty { "/" }
        loadPath(parent)
    }

    fun deleteFile(path: String) {
        viewModelScope.launch {
            _isLoading.value = true
            sftpService.deleteFile(path)
            loadPath(_currentPath.value)
        }
    }

    fun renameFile(oldPath: String, newName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val parent = oldPath.substringBeforeLast("/", "")
            val newPath = if (parent.isEmpty()) "/$newName" else "$parent/$newName"
            sftpService.renameFile(oldPath, newPath)
            loadPath(_currentPath.value)
        }
    }

    fun refresh() {
        loadPath(_currentPath.value)
    }

    override fun onCleared() {
        super.onCleared()
        sftpService.disconnect()
    }
}
