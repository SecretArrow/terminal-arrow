package com.terminalarrow.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.feature.profiles.ProfilesUiEffect
import com.terminalarrow.app.feature.profiles.ProfilesUiEvent
import com.terminalarrow.app.feature.profiles.ProfilesUiState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileListScreen(
    viewModel: ProfileViewModel,
    onProfileClick: (ConnectionProfile) -> Unit,
    onSFTPClick: (ConnectionProfile) -> Unit,
    onAddProfile: () -> Unit,
    onEditProfile: (ConnectionProfile) -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sortMode by viewModel.sortMode.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var query by remember { mutableStateOf("") }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is ProfilesUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Terminal Arrow") },
                actions = {
                    Box {
                        IconButton(onClick = { sortMenuExpanded = true }) {
                            Icon(Icons.Filled.Sort, contentDescription = "Sort")
                        }
                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            ProfileSortMode.values().forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.label()) },
                                    onClick = {
                                        viewModel.setSortMode(mode)
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddProfile,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("New host") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is ProfilesUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is ProfilesUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(onClick = { viewModel.onEvent(ProfilesUiEvent.Refresh) }) {
                            Text("Retry")
                        }
                    }
                }
                is ProfilesUiState.Success -> Column(modifier = Modifier.fillMaxSize()) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        placeholder = { Text("Search hosts") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    val filtered = remember(state.profiles, query, sortMode) {
                        val base = if (query.isBlank()) state.profiles
                        else state.profiles.filter {
                            val q = query.trim().lowercase()
                            it.name.lowercase().contains(q) ||
                                it.host.lowercase().contains(q) ||
                                it.username.lowercase().contains(q)
                        }
                        when (sortMode) {
                            ProfileSortMode.Alphabetical -> base.sortedBy { it.name.lowercase() }
                            ProfileSortMode.Recent -> base.sortedWith(
                                compareByDescending<ConnectionProfile> { it.lastConnectedAt }
                                    .thenBy { it.name.lowercase() }
                            )
                            ProfileSortMode.Favorites -> base.sortedWith(
                                compareByDescending<ConnectionProfile> { it.isFavorite }
                                    .thenByDescending { it.lastConnectedAt }
                                    .thenBy { it.name.lowercase() }
                            )
                        }
                    }
                    if (filtered.isEmpty()) {
                        EmptyState(
                            query = query,
                            onAdd = onAddProfile,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        )
                    } else {
                        ProfileList(
                            profiles = filtered,
                            onProfileClick = onProfileClick,
                            onSFTPClick = onSFTPClick,
                            onDeleteClick = { viewModel.onEvent(ProfilesUiEvent.DeleteProfile(it)) },
                            onEditClick = onEditProfile,
                            onFavoriteClick = { viewModel.toggleFavorite(it) }
                        )
                    }
                }
            }
        }
    }
}

private fun ProfileSortMode.label() = when (this) {
    ProfileSortMode.Recent -> "Sort: Recent"
    ProfileSortMode.Alphabetical -> "Sort: A → Z"
    ProfileSortMode.Favorites -> "Sort: Favorites"
}

@Composable
private fun EmptyState(query: String, onAdd: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Storage,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.height(12.dp))
        if (query.isBlank()) {
            Text("No saved hosts yet", style = MaterialTheme.typography.titleMedium)
            Text(
                "Tap \"New host\" to add your first SSH connection.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = onAdd) { Text("Add a host") }
        } else {
            Text("No matches for \"$query\"", style = MaterialTheme.typography.titleMedium)
            Text(
                "Try a different search term.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun ProfileList(
    profiles: List<ConnectionProfile>,
    onProfileClick: (ConnectionProfile) -> Unit,
    onSFTPClick: (ConnectionProfile) -> Unit,
    onDeleteClick: (ConnectionProfile) -> Unit,
    onEditClick: (ConnectionProfile) -> Unit,
    onFavoriteClick: (ConnectionProfile) -> Unit
) {
    val grouped = profiles.groupBy { it.group ?: "Default" }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        grouped.forEach { (group, list) ->
            item(key = "header:$group") {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = group,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            items(list, key = { "profile:${it.id}" }) { profile ->
                ListItem(
                    headlineContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FavoriteDot(profile.isFavorite)
                            Spacer(Modifier.size(8.dp))
                            Text(profile.name, fontWeight = FontWeight.Medium)
                        }
                    },
                    supportingContent = {
                        Column {
                            Text("${profile.username}@${profile.host}:${profile.port}")
                            if (profile.lastConnectedAt > 0) {
                                Text(
                                    "Last connected ${relativeTime(profile.lastConnectedAt)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    },
                    modifier = Modifier.clickable { onProfileClick(profile) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { onFavoriteClick(profile) }) {
                                Icon(
                                    imageVector = if (profile.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                                    contentDescription = "Favorite",
                                    tint = if (profile.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            }
                            IconButton(onClick = { onSFTPClick(profile) }) {
                                Icon(Icons.Filled.Folder, contentDescription = "SFTP")
                            }
                            IconButton(onClick = { onEditClick(profile) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = { onDeleteClick(profile) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                )
                Divider()
            }
        }
    }
}

@Composable
private fun FavoriteDot(favorite: Boolean) {
    val color = if (favorite) MaterialTheme.colorScheme.primary else Color.Transparent
    Surface(
        modifier = Modifier.size(8.dp),
        shape = CircleShape,
        color = color
    ) {}
}

private fun relativeTime(ts: Long): String {
    if (ts <= 0L) return "never"
    val diff = System.currentTimeMillis() - ts
    val sec = diff / 1000
    val min = sec / 60
    val hr = min / 60
    val day = hr / 24
    return when {
        sec < 60 -> "just now"
        min < 60 -> "${min}m ago"
        hr < 24 -> "${hr}h ago"
        day < 30 -> "${day}d ago"
        else -> "long ago"
    }
}
