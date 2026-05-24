package com.terminalarrow.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SFTPBrowserScreen(viewModel: SFTPViewModel, onFileClick: (String) -> Unit) {
    val files by viewModel.files.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentPath) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Up")
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
                    ListItem(
                        headlineContent = { Text(file.name) },
                        leadingContent = {
                            Icon(
                                if (isDir) Icons.Default.Folder else Icons.Default.Description,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.clickable {
                            if (isDir) {
                                viewModel.loadPath(file.path)
                            } else {
                                onFileClick(file.path)
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}
