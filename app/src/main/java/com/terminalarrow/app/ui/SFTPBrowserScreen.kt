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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SFTPBrowserScreen(viewModel: SFTPViewModel, onFileClick: (String) -> Unit) {
    val files by viewModel.files.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var showRenameDialog by remember { mutableStateOf<String?>(null) }
    var newName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(currentPath.split("/").last().ifEmpty { "/" })
                        if (viewModel.isInsideArchive.collectAsState().value) {
                            Text("Inside Archive", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Up")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(files) { file ->
                    val isDir = file.isDirectory
                    val isArchive = file.isArchive
                    ListItem(
                        headlineContent = { Text(file.name) },
                        leadingContent = {
                            Icon(
                                when {
                                    isDir -> Icons.Default.Folder
                                    isArchive -> Icons.Default.Inventory // Icon for zip/tar
                                    else -> Icons.Default.Description
                                },
                                contentDescription = null,
                                tint = if (isArchive) MaterialTheme.colorScheme.tertiary else LocalContentColor.current
                            )
                        },
                        modifier = Modifier.clickable {
                            when {
                                isArchive -> viewModel.navigateIntoArchive(file.path)
                                isDir -> viewModel.loadPath(file.path)
                                else -> onFileClick(file.path)
                            }
                        },
                        trailingContent = {
                            if (!viewModel.isInsideArchive.collectAsState().value) {
                                Row {
                                    IconButton(onClick = { 
                                        showRenameDialog = file.path
                                        newName = file.name
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Rename", modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { viewModel.deleteFile(file.path) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    )
                    Divider()
                }
            }
        }
        
        if (showRenameDialog != null) {
            AlertDialog(
                onDismissRequest = { showRenameDialog = null },
                title = { Text("Rename File") },
                text = {
                    OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("New Name") })
                },
                confirmButton = {
                    Button(onClick = {
                        showRenameDialog?.let { viewModel.renameFile(it, newName) }
                        showRenameDialog = null
                    }) { Text("Rename") }
                }
            )
        }
    }
}
