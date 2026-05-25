package com.terminalarrow.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.feature.sftp.*
import com.terminalarrow.app.service.SFTPService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SFTPViewModel @Inject constructor(
    private val sftpService: SFTPService
) : ViewModel() {

    private val _uiState = MutableStateFlow<SftpUiState>(SftpUiState.Loading)
    val uiState: StateFlow<SftpUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<SftpUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<SftpUiEffect> = _uiEffect.receiveAsFlow()

    fun onEvent(event: SftpUiEvent) {
        when (event) {
            is SftpUiEvent.Connect -> connectAndList(event.host, event.port, event.user, event.pass, event.keyPath)
            is SftpUiEvent.LoadPath -> loadPath(event.path)
            is SftpUiEvent.NavigateIntoArchive -> navigateIntoArchive(event.archivePath)
            SftpUiEvent.NavigateUp -> navigateUp()
            is SftpUiEvent.DownloadFile -> downloadFile(event.remotePath, event.localPath)
            is SftpUiEvent.UploadFile -> uploadFile(event.localPath, event.remotePath)
            is SftpUiEvent.DeleteFile -> deleteFile(event.path)
            is SftpUiEvent.RenameFile -> renameFile(event.oldPath, event.newName)
            SftpUiEvent.Refresh -> refresh()
        }
    }

    private fun connectAndList(host: String, port: Int, user: String, pass: String?, keyPath: String? = null) {
        viewModelScope.launch {
            _uiState.value = SftpUiState.Loading
            try {
                if (sftpService.connect(host, port, user, pass, keyPath)) {
                    loadPathInternal("/")
                } else {
                    _uiState.value = SftpUiState.Error("Failed to connect to SFTP")
                }
            } catch (e: Exception) {
                _uiState.value = SftpUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadPath(path: String) {
        viewModelScope.launch {
            _uiState.value = SftpUiState.Loading
            loadPathInternal(path)
        }
    }

    private suspend fun loadPathInternal(path: String) {
        try {
            val remoteFiles = sftpService.listFiles(path)
            val virtualFiles = remoteFiles.map { 
                val name = it.name
                val isArchive = name.endsWith(".zip") || name.endsWith(".tar") || name.endsWith(".gz")
                VirtualFile(name, it.path, it.isDirectory, isArchive)
            }
            _uiState.value = SftpUiState.Success(
                files = virtualFiles,
                currentPath = path,
                isInsideArchive = false
            )
        } catch (e: Exception) {
            _uiState.value = SftpUiState.Error(e.message ?: "Failed to load path")
        }
    }

    private fun navigateIntoArchive(archivePath: String) {
        viewModelScope.launch {
            _uiState.value = SftpUiState.Loading
            try {
                val inputStream = sftpService.getRemoteInputStream(archivePath)
                if (inputStream != null) {
                    try {
                        val entries = mutableListOf<VirtualFile>()
                        if (archivePath.endsWith(".zip")) {
                            val zipStream = java.util.zip.ZipInputStream(inputStream)
                            var entry = zipStream.getNextEntry()
                            while (entry != null) {
                                entries.add(VirtualFile(
                                    name = entry.name,
                                    path = "$archivePath!/${entry.name}",
                                    isDirectory = entry.isDirectory,
                                    parentArchivePath = archivePath
                                ))
                                entry = zipStream.getNextEntry()
                            }
                        }
                        _uiState.value = SftpUiState.Success(
                            files = entries,
                            currentPath = archivePath,
                            isInsideArchive = true
                        )
                    } catch (e: Exception) {
                        _uiEffect.send(SftpUiEffect.ShowSnackbar("Failed to read archive"))
                        loadPathInternal(archivePath.substringBeforeLast("/"))
                    } finally {
                        inputStream.close()
                    }
                }
            } catch (e: Exception) {
                _uiState.value = SftpUiState.Error("Archive error: ${e.message}")
            }
        }
    }

    private fun navigateUp() {
        val currentState = _uiState.value as? SftpUiState.Success ?: return
        if (currentState.isInsideArchive) {
            loadPath(currentState.currentPath.substringBeforeLast("/", "").ifEmpty { "/" })
        } else {
            val parent = currentState.currentPath.substringBeforeLast("/", "").ifEmpty { "/" }
            loadPath(parent)
        }
    }

    private fun downloadFile(remotePath: String, localPath: String) {
        viewModelScope.launch {
            try {
                sftpService.downloadFile(remotePath, localPath) { progress ->
                    _uiState.update { state ->
                        if (state is SftpUiState.Success) state.copy(transferProgress = progress) else state
                    }
                }
                _uiEffect.send(SftpUiEffect.ShowSnackbar("Download complete"))
            } catch (e: Exception) {
                _uiEffect.send(SftpUiEffect.ShowSnackbar("Download failed"))
            } finally {
                _uiState.update { state ->
                    if (state is SftpUiState.Success) state.copy(transferProgress = null) else state
                }
            }
        }
    }

    private fun uploadFile(localPath: String, remotePath: String) {
        viewModelScope.launch {
            try {
                sftpService.uploadFile(localPath, remotePath) { progress ->
                    _uiState.update { state ->
                        if (state is SftpUiState.Success) state.copy(transferProgress = progress) else state
                    }
                }
                _uiEffect.send(SftpUiEffect.ShowSnackbar("Upload complete"))
                refresh()
            } catch (e: Exception) {
                _uiEffect.send(SftpUiEffect.ShowSnackbar("Upload failed"))
            } finally {
                _uiState.update { state ->
                    if (state is SftpUiState.Success) state.copy(transferProgress = null) else state
                }
            }
        }
    }

    private fun deleteFile(path: String) {
        viewModelScope.launch {
            try {
                sftpService.deleteFile(path)
                _uiEffect.send(SftpUiEffect.ShowSnackbar("File deleted"))
                refresh()
            } catch (e: Exception) {
                _uiEffect.send(SftpUiEffect.ShowSnackbar("Delete failed"))
            }
        }
    }

    private fun renameFile(oldPath: String, newPath: String) {
        viewModelScope.launch {
            try {
                sftpService.renameFile(oldPath, newPath)
                _uiEffect.send(SftpUiEffect.ShowSnackbar("File renamed"))
                refresh()
            } catch (e: Exception) {
                _uiEffect.send(SftpUiEffect.ShowSnackbar("Rename failed"))
            }
        }
    }

    private fun refresh() {
        val state = _uiState.value as? SftpUiState.Success ?: return
        if (state.isInsideArchive) {
            navigateIntoArchive(state.currentPath)
        } else {
            loadPath(state.currentPath)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sftpService.disconnect()
    }
}
