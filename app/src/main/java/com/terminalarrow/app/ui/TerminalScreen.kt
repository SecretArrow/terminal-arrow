package com.terminalarrow.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Description
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.terminalarrow.app.ui.theme.ThemeManager

@Composable
fun TerminalScreen(viewModel: TerminalViewModel, themeManager: ThemeManager) {
    val sessions by viewModel.sessions.collectAsState()
    val activeSessionId by viewModel.activeSession.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    
    var isSplitView by remember { mutableStateOf(false) }
    var isSearchVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val theme = themeManager.currentTheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        if (isSearchVisible) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.performSearch(activeSessionId, it) },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                placeholder = { Text("Search buffer...") },
                trailingIcon = {
                    IconButton(onClick = { isSearchVisible = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close Search")
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = theme.background.copy(alpha = 0.8f),
                    unfocusedContainerColor = theme.background.copy(alpha = 0.5f),
                    focusedTextColor = theme.foreground,
                    unfocusedTextColor = theme.foreground
                )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Button(onClick = { isSplitView = !isSplitView }) {
                    Text(if (isSplitView) "Close Split" else "Dual Session")
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (isSplitView) {
                    Button(onClick = { viewModel.setActiveSession(if (activeSessionId == "primary") "secondary" else "primary") }) {
                        Text("Switch focus: $activeSessionId")
                    }
                }
            }
            Row {
                IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = theme.foreground)
                }
                IconButton(onClick = { viewModel.exportTerminalOutput(activeSessionId, context) { /* Share */ } }) {
                    Icon(Icons.Default.Share, contentDescription = "Export", tint = theme.foreground)
                }
            }
        }

        Row(modifier = Modifier.weight(1f)) {
            TerminalCanvas(sessions["primary"] ?: "", themeManager, Modifier.weight(1f))
            if (isSplitView) {
                Divider(modifier = Modifier.width(1.dp).fillMaxHeight())
                TerminalCanvas(sessions["secondary"] ?: "", themeManager, Modifier.weight(1f))
            }
        }

        KeyboardToolbar(onKeyClick = { viewModel.onSpecialKey(activeSessionId, it) })

        var inputText by remember { mutableStateOf("") }
        
        if (suggestions.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suggestions) { suggestion ->
                    SuggestionChip(
                        onClick = {
                            inputText += suggestion
                            viewModel.onInputChange(activeSessionId, inputText)
                        },
                        label = { Text(suggestion) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "> ", color = theme.foreground, fontFamily = themeManager.fontFamily)
            BasicTextField(
                value = inputText,
                onValueChange = { 
                    inputText = it
                    viewModel.onInputChange(activeSessionId, it)
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = theme.foreground,
                    fontFamily = themeManager.fontFamily,
                    fontSize = themeManager.fontSize.sp
                ),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(theme.cursor),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendCommand(activeSessionId, inputText + "\n")
                            inputText = ""
                        }
                    }
                )
            )
        }
    }
}

@Composable
fun SuggestionChip(onClick: () -> Unit, label: @Composable () -> Unit) {
    Surface(
        onClick = onClick,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            label()
        }
    }
}
