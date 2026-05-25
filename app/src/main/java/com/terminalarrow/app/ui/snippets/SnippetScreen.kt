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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.terminalarrow.app.feature.snippets.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnippetScreen(viewModel: SnippetViewModel, onSnippetUse: (String) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is SnippetsUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Command Snippets") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Snippet")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is SnippetsUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is SnippetsUiState.Error -> Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                is SnippetsUiState.Success -> {
                    SnippetList(
                        snippets = state.snippets,
                        onSnippetUse = onSnippetUse,
                        onDelete = { viewModel.onEvent(SnippetsUiEvent.DeleteSnippet(it)) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddSnippetDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, cmd ->
                viewModel.onEvent(SnippetsUiEvent.SaveSnippet(name, cmd))
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun SnippetList(
    snippets: List<com.terminalarrow.app.data.Snippet>,
    onSnippetUse: (String) -> Unit,
    onDelete: (com.terminalarrow.app.data.Snippet) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(snippets) { snippet ->
            ListItem(
                headlineContent = { Text(snippet.name) },
                supportingContent = { Text(snippet.command) },
                modifier = Modifier.clickable { onSnippetUse(snippet.command) },
                trailingContent = {
                    IconButton(onClick = { onDelete(snippet) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
            Divider()
        }
        if (snippets.isEmpty()) {
            item {
                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No snippets found")
                }
            }
        }
    }
}

@Composable
private fun AddSnippetDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var command by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Snippet") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = command, onValueChange = { command = it }, label = { Text("Command") })
            }
        },
        confirmButton = {
            Button(onClick = { onSave(name, command) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
