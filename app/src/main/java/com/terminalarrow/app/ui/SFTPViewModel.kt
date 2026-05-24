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

data class VirtualFile(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val isArchive: Boolean = false,
    val parentArchivePath: String? = null
)

@HiltViewModel
class SFTPViewModel @Inject constructor(
    private val sftpService: SFTPService
) : ViewModel() {

    private val _files = MutableStateFlow<List<VirtualFile>>(emptyList())
    val files: StateFlow<List<VirtualFile>> = _files

    private val _currentPath = MutableStateFlow("/")
    val currentPath: StateFlow<String> = _currentPath

    private val _isInsideArchive = MutableStateFlow(false)
    val isInsideArchive: StateFlow<Boolean> = _isInsideArchive

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var archiveContentsCache: Map<String, List<VirtualFile>> = emptyMap()

    fun connectAndList(host: String, port: Int, user: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if (sftpService.connect(host, port, user, pass)) {
                _isInsideArchive.value = false
                loadPath("/")
            }
            _isLoading.value = false
        }
    }

    fun loadPath(path: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _isInsideArchive.value = false
            val remoteFiles = sftpService.listFiles(path)
            _files.value = remoteFiles.map { 
                val name = it.name
                val isArchive = name.endsWith(".zip") || name.endsWith(".tar") || name.endsWith(".gz")
                VirtualFile(name, it.path, it.isDirectory, isArchive)
            }
            _currentPath.value = path
            _isLoading.value = false
        }
    }

    fun navigateIntoArchive(archivePath: String) {
        viewModelScope.launch {
            _isLoading.value = true
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
                    // Process archive into a tree or flat list based on 'current virtual path'
                    // For brevity, we'll show all entries as a flat list initially
                    _files.value = entries
                    _currentPath.value = archivePath
                    _isInsideArchive.value = true
                } catch (e: Exception) {
                    // Handle error
                } finally {
                    inputStream.close()
                }
            }
            _isLoading.value = false
        }
    }

    fun navigateUp() {
        if (_isInsideArchive.value) {
            _isInsideArchive.value = false
            loadPath(_currentPath.value.substringBeforeLast("/", "").ifEmpty { "/" })
        } else {
            val parent = _currentPath.value.substringBeforeLast("/", "").ifEmpty { "/" }
            loadPath(parent)
        }
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
        if (_isInsideArchive.value) {
            navigateIntoArchive(_currentPath.value)
        } else {
            loadPath(_currentPath.value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sftpService.disconnect()
    }
}
