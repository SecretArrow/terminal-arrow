package com.terminalarrow.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.terminalarrow.app.data.KnownHost
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnownHostsScreen(
    viewModel: KnownHostsViewModel,
    onBack: () -> Unit
) {
    val hosts by viewModel.hosts.collectAsState(initial = emptyList<KnownHost>())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Known hosts") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (hosts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Shield,
                        contentDescription = null,
                        modifier = Modifier.height(48.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "No known hosts yet.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Server fingerprints you accept will appear here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(vertical = 8.dp), modifier = Modifier.padding(padding)) {
                items(hosts, key = { it.id }) { host ->
                    ListItem(
                        headlineContent = { Text("${host.host}:${host.port}") },
                        supportingContent = {
                            Column {
                                Text("${host.keyType}  ${shortFingerprint(host.fingerprint)}", style = MaterialTheme.typography.bodySmall)
                                Text("First seen ${DateFormat.getDateInstance().format(Date(host.firstSeen))}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        },
                        trailingContent = {
                            IconButton(onClick = { viewModel.delete(host) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Forget host")
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

private fun shortFingerprint(fp: String): String {
    return if (fp.length > 32) fp.take(16) + "…" + fp.takeLast(16) else fp
}
