package com.terminalarrow.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VirtualDPad(
    modifier: Modifier = Modifier,
    onDirection: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onDirection("UP") },
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(40.dp)
        ) {
            Text("↑")
        }
        Row {
            Button(
                onClick = { onDirection("LEFT") },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Text("←")
            }
            Spacer(modifier = Modifier.size(40.dp))
            Button(
                onClick = { onDirection("RIGHT") },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Text("→")
            }
        }
        Button(
            onClick = { onDirection("DOWN") },
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(40.dp)
        ) {
            Text("↓")
        }
    }
}
