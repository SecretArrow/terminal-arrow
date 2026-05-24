package com.terminalarrow.app.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSelectionScreen(themeManager: ThemeManager, onBack: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Font Settings") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Font Size: ${themeManager.fontSize}sp", style = MaterialTheme.typography.titleMedium)
            Slider(
                value = themeManager.fontSize.toFloat(),
                onValueChange = { themeManager.fontSize = it.toInt() },
                valueRange = 8f..32f,
                steps = 24
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Font Family", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(themeManager.fontFamilies.toList()) { (name, family) ->
                    ListItem(
                        headlineContent = { Text(name, fontFamily = family) },
                        modifier = Modifier.clickable {
                            themeManager.fontFamily = family
                        },
                        trailingContent = {
                            if (themeManager.fontFamily == family) {
                                RadioButton(selected = true, onClick = null)
                            }
                        }
                    )
                    Divider()
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Apply & Back")
            }
        }
    }
}
