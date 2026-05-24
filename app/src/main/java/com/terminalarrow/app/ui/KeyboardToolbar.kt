package com.terminalarrow.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KeyboardToolbar(onKeyClick: (String) -> Unit) {
    val keys = listOf("ESC", "CTRL", "ALT", "TAB", "↑", "↓", "←", "→", "PAGE UP", "PAGE DOWN")
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(keys) { key ->
            Button(
                onClick = { onKeyClick(key) },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = key)
            }
        }
    }
}
