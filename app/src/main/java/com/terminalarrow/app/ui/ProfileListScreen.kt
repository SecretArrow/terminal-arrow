package com.terminalarrow.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.terminalarrow.app.data.ConnectionProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileListScreen(
    viewModel: ProfileViewModel,
    onProfileClick: (ConnectionProfile) -> Unit,
    onSFTPClick: (ConnectionProfile) -> Unit,
    onAddProfile: () -> Unit,
    onSnippetClick: () -> Unit,
    onCloudClick: () -> Unit,
    onThemeClick: () -> Unit,
    onFontClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val profiles by viewModel.profiles.collectAsState()
    val groupedProfiles = profiles.groupBy { it.group ?: "Default" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terminal Arrow") },
                actions = {
                    IconButton(onClick = onFontClick) { Icon(Icons.Default.TextFields, contentDescription = "Fonts") }
                    IconButton(onClick = onThemeClick) { Icon(Icons.Default.Palette, contentDescription = "Themes") }
                    IconButton(onClick = onSnippetClick) { Icon(Icons.Default.Code, contentDescription = "Snippets") }
                    IconButton(onClick = onCloudClick) { Icon(Icons.Default.CloudDownload, contentDescription = "Cloud") }
                    IconButton(onClick = onAboutClick) { Icon(Icons.Default.Info, contentDescription = "About") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProfile) {
                Icon(Icons.Default.Add, contentDescription = "Add Profile")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            groupedProfiles.forEach { (group, profileList) ->
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = group,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                items(profileList) { profile ->
                    ListItem(
                        headlineContent = { Text(profile.name) },
                        supportingContent = { Text("${profile.username}@${profile.host}:${profile.port}") },
                        modifier = Modifier.clickable { onProfileClick(profile) },
                        trailingContent = {
                            Row {
                                IconButton(onClick = { onSFTPClick(profile) }) {
                                    Icon(Icons.Default.Folder, contentDescription = "SFTP")
                                }
                                IconButton(onClick = { viewModel.deleteProfile(profile) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}
