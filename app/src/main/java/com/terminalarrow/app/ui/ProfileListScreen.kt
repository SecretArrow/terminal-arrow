package com.terminalarrow.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.feature.profiles.ProfilesUiState
import com.terminalarrow.app.feature.profiles.ProfilesUiEvent
import com.terminalarrow.app.feature.profiles.ProfilesUiEffect
import kotlinx.coroutines.flow.collectLatest

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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is ProfilesUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is ProfilesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfilesUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.onEvent(ProfilesUiEvent.Refresh) }) {
                            Text("Retry")
                        }
                    }
                }
                is ProfilesUiState.Success -> {
                    if (state.profiles.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                            Text("No profiles found", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        ProfileList(
                            profiles = state.profiles,
                            onProfileClick = onProfileClick,
                            onSFTPClick = onSFTPClick,
                            onDeleteClick = { viewModel.onEvent(ProfilesUiEvent.DeleteProfile(it)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileList(
    profiles: List<ConnectionProfile>,
    onProfileClick: (ConnectionProfile) -> Unit,
    onSFTPClick: (ConnectionProfile) -> Unit,
    onDeleteClick: (ConnectionProfile) -> Unit
) {
    val groupedProfiles = profiles.groupBy { it.group ?: "Default" }
    LazyColumn {
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
            items(profileList, key = { it.id }) { profile ->
                ListItem(
                    headlineContent = { Text(profile.name) },
                    supportingContent = { Text("${profile.username}@${profile.host}:${profile.port}") },
                    modifier = Modifier.clickable { onProfileClick(profile) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { onSFTPClick(profile) }) {
                                Icon(Icons.Default.Folder, contentDescription = "SFTP")
                            }
                            IconButton(onClick = { onDeleteClick(profile) }) {
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
