package com.terminalarrow.app.ui.snippets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnippetScreen(viewModel: SnippetViewModel, onSnippetClick: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var command by remember { mutableStateOf("") }
    
    val snippets by viewModel.snippets.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Command Snippets") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Snippet")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(snippets) { snippet ->
                ListItem(
                    headlineContent = { Text(snippet.name) },
                    supportingContent = { Text(snippet.command) },
                    modifier = Modifier.clickable { onSnippetClick(snippet.command) },
                    trailingContent = {
                        IconButton(onClick = { viewModel.deleteSnippet(snippet) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                )
                Divider()
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Snippet") },
                text = {
                    Column {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                        OutlinedTextField(value = command, onValueChange = { command = it }, label = { Text("Command") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.saveSnippet(name, command)
                        showDialog = false
                    }) { Text("Save") }
                }
            )
        }
    }
}
