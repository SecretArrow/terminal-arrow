package com.terminalarrow.app.feature.sftp

import com.terminalarrow.app.ui.VirtualFile

sealed interface SftpUiState {
    data object Loading : SftpUiState
    data class Success(
        val files: List<VirtualFile>,
        val currentPath: String,
        val isInsideArchive: Boolean,
        val transferProgress: Int? = null
    ) : SftpUiState
    data class Error(val message: String) : SftpUiState
}

sealed interface SftpUiEvent {
    data class Connect(val host: String, val port: Int, val user: String, val pass: String?, val keyPath: String? = null) : SftpUiEvent
    data class LoadPath(val path: String) : SftpUiEvent
    data class NavigateIntoArchive(val archivePath: String) : SftpUiEvent
    data object NavigateUp : SftpUiEvent
    data class DownloadFile(val remotePath: String, val localPath: String) : SftpUiEvent
    data class UploadFile(val localPath: String, val remotePath: String) : SftpUiEvent
    data class DeleteFile(val path: String) : SftpUiEvent
    data class RenameFile(val oldPath: String, val newName: String) : SftpUiEvent
    data object Refresh : SftpUiEvent
}

sealed interface SftpUiEffect {
    data class ShowSnackbar(val message: String) : SftpUiEffect
}
