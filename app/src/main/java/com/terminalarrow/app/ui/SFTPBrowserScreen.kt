package com.terminalarrow.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.terminalarrow.app.feature.sftp.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SFTPBrowserScreen(viewModel: SFTPViewModel, onFileClick: (String) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is SftpUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("SFTP: ${(uiState as? SftpUiState.Success)?.currentPath ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(SftpUiEvent.NavigateUp) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Up")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(SftpUiEvent.Refresh) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is SftpUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is SftpUiState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.onEvent(SftpUiEvent.Refresh) }) { Text("Retry") }
                    }
                }
                is SftpUiState.Success -> {
                    SftpFileList(
                        state = state,
                        onFileClick = onFileClick,
                        onNavigateIntoArchive = { viewModel.onEvent(SftpUiEvent.NavigateIntoArchive(it)) },
                        onLoadPath = { viewModel.onEvent(SftpUiEvent.LoadPath(it)) },
                        onDeleteFile = { viewModel.onEvent(SftpUiEvent.DeleteFile(it)) }
                    )
                    
                    if (state.transferProgress != null) {
                        LinearProgressIndicator(
                            progress = state.transferProgress.toFloat() / 100f,
                            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SftpFileList(
    state: SftpUiState.Success,
    onFileClick: (String) -> Unit,
    onNavigateIntoArchive: (String) -> Unit,
    onLoadPath: (String) -> Unit,
    onDeleteFile: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.files) { file ->
            ListItem(
                headlineContent = { Text(file.name) },
                leadingContent = {
                    Icon(
                        if (file.isDirectory) Icons.Default.Folder else Icons.Default.Description,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable {
                    when {
                        file.isArchive -> onNavigateIntoArchive(file.path)
                        file.isDirectory -> onLoadPath(file.path)
                        else -> onFileClick(file.path)
                    }
                },
                trailingContent = {
                    IconButton(onClick = { onDeleteFile(file.path) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
            Divider()
        }
        if (state.files.isEmpty()) {
            item {
                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No files in this directory")
                }
            }
        }
    }
}
