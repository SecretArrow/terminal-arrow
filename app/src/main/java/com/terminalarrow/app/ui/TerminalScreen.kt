package com.terminalarrow.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.terminalarrow.app.feature.terminal.*
import com.terminalarrow.app.feature.snippets.SnippetsUiState
import com.terminalarrow.app.ui.snippets.SnippetViewModel
import com.terminalarrow.app.ui.theme.ThemeManager
import com.terminalarrow.app.utils.VibratorHelper
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    viewModel: TerminalViewModel,
    snippetViewModel: SnippetViewModel,
    themeManager: ThemeManager,
    vibratorHelper: VibratorHelper
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snippetsState by snippetViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var isSplitView by remember { mutableStateOf(false) }
    var isSearchVisible by remember { mutableStateOf(false) }
    var showSnippets by remember { mutableStateOf(false) }
    var isImmersive by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? android.app.Activity
    val theme = themeManager.currentTheme

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is TerminalUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                is TerminalUiEffect.PlayVibration -> vibratorHelper.vibrate(effect.duration)
            }
        }
    }

    LaunchedEffect(isImmersive) {
        activity?.window?.let { window ->
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            if (isImmersive) {
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (isSearchVisible && uiState is TerminalUiState.Success) {
                val state = uiState as TerminalUiState.Success
                TextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onEvent(TerminalUiEvent.PerformSearch(state.activeSession, it)) },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(theme.background)
        ) {
            TerminalToolbar(
                isSplitView = isSplitView,
                activeSessionId = (uiState as? TerminalUiState.Success)?.activeSession ?: "primary",
                theme = theme,
                onToggleSplit = { isSplitView = !isSplitView },
                onSwitchSession = { viewModel.onEvent(TerminalUiEvent.SetActiveSession(it)) },
                onShowSnippets = { showSnippets = true },
                onToggleImmersive = { isImmersive = !isImmersive },
                onToggleSearch = { isSearchVisible = !isSearchVisible },
                onExport = { 
                    val activeId = (uiState as? TerminalUiState.Success)?.activeSession ?: "primary"
                    viewModel.onEvent(TerminalUiEvent.ExportOutput(activeId, context) { /* Share */ }) 
                }
            )

            Box(modifier = Modifier.weight(1f)) {
                when (val state = uiState) {
                    is TerminalUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is TerminalUiState.Error -> Text(state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                    is TerminalUiState.Success -> {
                        TerminalContent(
                            state = state,
                            isSplitView = isSplitView,
                            themeManager = themeManager,
                            onResize = { id, cols, rows -> viewModel.onEvent(TerminalUiEvent.ResizeTerminal(id, cols, rows)) },
                            onSendCommand = { id, cmd -> viewModel.onEvent(TerminalUiEvent.SendCommand(cmd, id)) }
                        )
                    }
                }
            }

            KeyboardToolbar(onKeyClick = { key ->
                val activeId = (uiState as? TerminalUiState.Success)?.activeSession ?: "primary"
                val seq = specialKeyToSequence(key)
                if (seq.isNotEmpty()) {
                    viewModel.onEvent(TerminalUiEvent.SendCommand(seq, activeId))
                }
            })

            if (uiState is TerminalUiState.Success) {
                val state = uiState as TerminalUiState.Success
                SuggestionRow(
                    suggestions = state.suggestions,
                    onSuggestionClick = {
                        inputText += it
                        viewModel.onEvent(TerminalUiEvent.OnInputChange(state.activeSession, inputText))
                    }
                )

                CommandLineInput(
                    inputText = inputText,
                    theme = theme,
                    themeManager = themeManager,
                    onInputChange = {
                        inputText = it
                        viewModel.onEvent(TerminalUiEvent.OnInputChange(state.activeSession, it))
                    },
                    onSendCommand = {
                        if (inputText.isNotBlank()) {
                            viewModel.onEvent(TerminalUiEvent.SendCommand(inputText + "\n", state.activeSession))
                            inputText = ""
                        }
                    },
                    onKeyEvent = { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown) {
                            if (keyEvent.isCtrlPressed) {
                                when (keyEvent.key) {
                                    Key.C -> { viewModel.onEvent(TerminalUiEvent.SendCommand("\u0003", state.activeSession)); true }
                                    Key.D -> { viewModel.onEvent(TerminalUiEvent.SendCommand("\u0004", state.activeSession)); true }
                                    Key.L -> { viewModel.onEvent(TerminalUiEvent.SendCommand("\u000C", state.activeSession)); true }
                                    Key.Z -> { viewModel.onEvent(TerminalUiEvent.SendCommand("\u001A", state.activeSession)); true }
                                    else -> false
                                }
                            } else false
                        } else false
                    }
                )
            }
        }
    }

    if (showSnippets) {
        val snippets = (snippetsState as? SnippetsUiState.Success)?.snippets ?: emptyList()
        SnippetBottomSheet(
            snippets = snippets,
            onSnippetClick = {
                val activeId = (uiState as? TerminalUiState.Success)?.activeSession ?: "primary"
                viewModel.onEvent(TerminalUiEvent.SendCommand(it.command + "\n", activeId))
                showSnippets = false
            },
            onDismiss = { showSnippets = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TerminalToolbar(
    isSplitView: Boolean,
    activeSessionId: String,
    theme: com.terminalarrow.app.ui.theme.TerminalTheme,
    onToggleSplit: () -> Unit,
    onSwitchSession: (String) -> Unit,
    onShowSnippets: () -> Unit,
    onToggleImmersive: () -> Unit,
    onToggleSearch: () -> Unit,
    onExport: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilledTonalButton(onClick = onToggleSplit) {
                Text(if (isSplitView) "Close Split" else "Dual Session")
            }
            if (isSplitView) {
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = activeSessionId == "primary",
                    onClick = { onSwitchSession("primary") },
                    label = { Text("P") }
                )
                Spacer(modifier = Modifier.width(4.dp))
                FilterChip(
                    selected = activeSessionId == "secondary",
                    onClick = { onSwitchSession("secondary") },
                    label = { Text("S") }
                )
            }
        }
        Row {
            IconButton(onClick = onShowSnippets) {
                Icon(Icons.Default.Add, contentDescription = "Snippets", tint = theme.foreground)
            }
            IconButton(onClick = onToggleImmersive) {
                Icon(Icons.Default.Fullscreen, contentDescription = "Immersive", tint = theme.foreground)
            }
            IconButton(onClick = onToggleSearch) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = theme.foreground)
            }
            IconButton(onClick = onExport) {
                Icon(Icons.Default.Share, contentDescription = "Export", tint = theme.foreground)
            }
        }
    }
}

@Composable
private fun TerminalContent(
    state: TerminalUiState.Success,
    isSplitView: Boolean,
    themeManager: ThemeManager,
    onResize: (String, Int, Int) -> Unit,
    onSendCommand: (String, String) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            TerminalCanvas(
                output = state.sessions["primary"] ?: "",
                themeManager = themeManager,
                modifier = Modifier.fillMaxSize(),
                onResize = { cols, rows -> onResize("primary", cols, rows) }
            )
            VirtualDPad(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                onDirection = { dir -> onSendCommand("primary", directionToEscape(dir)) }
            )
        }
        if (isSplitView) {
            Divider(modifier = Modifier.width(1.dp).fillMaxHeight())
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                TerminalCanvas(
                    output = state.sessions["secondary"] ?: "",
                    themeManager = themeManager,
                    modifier = Modifier.fillMaxSize(),
                    onResize = { cols, rows -> onResize("secondary", cols, rows) }
                )
                VirtualDPad(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                    onDirection = { dir -> onSendCommand("secondary", directionToEscape(dir)) }
                )
            }
        }
    }
}

private fun directionToEscape(dir: String): String = when (dir) {
    "UP" -> "\u001B[A"
    "DOWN" -> "\u001B[B"
    "RIGHT" -> "\u001B[C"
    "LEFT" -> "\u001B[D"
    else -> ""
}

private fun specialKeyToSequence(key: String): String = when (key) {
    "ESC" -> "\u001B"
    "TAB" -> "\t"
    "↑" -> "\u001B[A"
    "↓" -> "\u001B[B"
    "←" -> "\u001B[D"
    "→" -> "\u001B[C"
    "PAGE UP" -> "\u001B[5~"
    "PAGE DOWN" -> "\u001B[6~"
    "HOME" -> "\u001B[H"
    "END" -> "\u001B[F"
    "DEL" -> "\u001B[3~"
    // CTRL / ALT are modifiers and handled together with a follow-up keystroke
    else -> ""
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuggestionRow(suggestions: List<String>, onSuggestionClick: (String) -> Unit) {
    if (suggestions.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(suggestions) { suggestion ->
                SuggestionChip(
                    onClick = { onSuggestionClick(suggestion) },
                    label = { Text(suggestion) }
                )
            }
        }
    }
}

@Composable
private fun CommandLineInput(
    inputText: String,
    theme: com.terminalarrow.app.ui.theme.TerminalTheme,
    themeManager: ThemeManager,
    onInputChange: (String) -> Unit,
    onSendCommand: () -> Unit,
    onKeyEvent: (KeyEvent) -> Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "> ", color = theme.foreground, fontFamily = themeManager.fontFamily)
        BasicTextField(
            value = inputText,
            onValueChange = onInputChange,
            modifier = Modifier.fillMaxWidth().onKeyEvent(onKeyEvent),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = theme.foreground,
                fontFamily = themeManager.fontFamily,
                fontSize = themeManager.fontSize.sp
            ),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(theme.cursor),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = { onSendCommand() })
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SnippetBottomSheet(
    snippets: List<com.terminalarrow.app.data.Snippet>,
    onSnippetClick: (com.terminalarrow.app.data.Snippet) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            item {
                Text("Quick Snippets", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
            }
            items(snippets) { snippet ->
                ListItem(
                    headlineContent = { Text(snippet.name) },
                    supportingContent = { Text(snippet.command) },
                    modifier = Modifier.clickable { onSnippetClick(snippet) }
                )
                Divider()
            }
            if (snippets.isEmpty()) {
                item {
                    Text("No snippets found. Add them from the main menu.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
        }
    }
}
