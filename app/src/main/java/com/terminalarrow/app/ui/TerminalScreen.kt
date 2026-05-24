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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.terminalarrow.app.ui.theme.ThemeManager

@Composable
fun TerminalScreen(viewModel: TerminalViewModel, themeManager: ThemeManager) {
    val theme = themeManager.currentTheme
    val output by viewModel.terminalOutput.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isSplitView by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(output) {
        coroutineScope.launch {
            if (output.isNotEmpty()) {
                listState.animateScrollToItem(Int.MAX_VALUE)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Button(onClick = { isSplitView = !isSplitView }) {
                    Text(if (isSplitView) "Close Split" else "Split View")
                }
                IconButton(onClick = { isSearchActive = !isSearchActive }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = theme.foreground)
                }
            }
            IconButton(onClick = { viewModel.exportTerminalOutput(context) { /* Handle Share */ } }) {
                Icon(Icons.Default.Share, contentDescription = "Export Log", tint = theme.foreground)
            }
        }

        if (isSearchActive) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search buffer...") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textStyle = MaterialTheme.typography.bodySmall.copy(color = theme.foreground)
            )
        }

        if (suggestions.isNotEmpty()) {
            LazyRow(modifier = Modifier.padding(8.dp)) {
                items(suggestions) { suggestion ->
                    SuggestionChip(onClick = { 
                        inputText = suggestion
                        viewModel.onInputChange(suggestion)
                    }, label = { Text(suggestion) })
                }
            }
        }

        Row(modifier = Modifier.weight(1f)) {
            TerminalOutput(output, themeManager, listState, Modifier.weight(1f), searchQuery)
            if (isSplitView) {
                Divider(modifier = Modifier.width(1.dp).fillMaxHeight())
                TerminalOutput("Split Session Active...", themeManager, rememberLazyListState(), Modifier.weight(1f), searchQuery)
            }
        }

        KeyboardToolbar(onKeyClick = { viewModel.onSpecialKey(it) })

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
                    viewModel.onInputChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = theme.foreground,
                    fontFamily = themeManager.fontFamily,
                    fontSize = themeManager.fontSize.sp
                ),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(theme.cursor)
            )
        }
    }
}

@Composable
fun TerminalOutput(output: String, themeManager: ThemeManager, state: androidx.compose.foundation.lazy.LazyListState, modifier: Modifier, searchQuery: String = "") {
    val theme = themeManager.currentTheme
    val annotatedOutput = buildAnnotatedString {
        if (searchQuery.isEmpty()) {
            append(output)
        } else {
            var start = 0
            while (start < output.length) {
                val index = output.indexOf(searchQuery, start, ignoreCase = true)
                if (index == -1) {
                    append(output.substring(start))
                    break
                }
                append(output.substring(start, index))
                withStyle(style = SpanStyle(background = Color.Yellow, color = Color.Black)) {
                    append(output.substring(index, index + searchQuery.length))
                }
                start = index + searchQuery.length
            }
        }
    }

    LazyColumn(
        modifier = modifier.padding(8.dp),
        state = state
    ) {
        item {
            Text(
                text = annotatedOutput,
                color = theme.foreground,
                fontFamily = themeManager.fontFamily,
                fontSize = themeManager.fontSize.sp
            )
        }
    }
}
