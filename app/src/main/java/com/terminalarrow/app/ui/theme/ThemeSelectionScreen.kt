package com.terminalarrow.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionScreen(themeManager: ThemeManager, onThemeSelected: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Select Theme") }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(themeManager.themes) { theme ->
                ListItem(
                    headlineContent = { Text(theme.name) },
                    modifier = Modifier.clickable {
                        themeManager.currentTheme = theme
                        onThemeSelected()
                    },
                    leadingContent = {
                        Box(modifier = Modifier.size(24.dp).background(theme.background))
                    }
                )
                Divider()
            }
        }
    }
}
