package com.terminalarrow.app.ui

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    profileViewModel: ProfileViewModel,
    onBack: () -> Unit,
    onThemeClick: () -> Unit,
    onFontClick: () -> Unit,
    onSnippetsClick: () -> Unit,
    onCloudClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    val json = input.readBytes().toString(Charsets.UTF_8)
                    profileViewModel.importData(json)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingsSection(title = "Appearance") {
                ClickableRow(
                    title = "Terminal theme",
                    subtitle = "Color palette for terminal output",
                    leading = Icons.Filled.Palette,
                    onClick = onThemeClick
                )
                ClickableRow(
                    title = "Font",
                    subtitle = "Family and size",
                    leading = Icons.Filled.TextFields,
                    onClick = onFontClick
                )
            }

            SettingsSection(title = "Productivity") {
                ClickableRow(
                    title = "Snippets",
                    subtitle = "Saved one-liners and macros",
                    leading = Icons.Filled.Code,
                    onClick = onSnippetsClick
                )
                ClickableRow(
                    title = "Cloud import",
                    subtitle = "Pull instances from AWS EC2",
                    leading = Icons.Filled.CloudUpload,
                    onClick = onCloudClick
                )
            }

            SettingsSection(title = "Data") {
                ClickableRow(
                    title = "Export profiles",
                    subtitle = "Save a JSON backup",
                    leading = Icons.Filled.FileDownload,
                    onClick = {
                        profileViewModel.exportData { path ->
                            shareFile(context, path)
                        }
                    }
                )
                ClickableRow(
                    title = "Import profiles",
                    subtitle = "Restore from a JSON backup",
                    leading = Icons.Filled.FileUpload,
                    onClick = { importLauncher.launch("application/json") }
                )
            }

            SettingsSection(title = "About") {
                ClickableRow(
                    title = "About Terminal Arrow",
                    subtitle = "Version & credits",
                    leading = Icons.Filled.Info,
                    onClick = onAboutClick
                )
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold
    )
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        content()
    }
}

@Composable
private fun ClickableRow(
    title: String,
    subtitle: String,
    leading: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(leading, contentDescription = null) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

private fun shareFile(context: Context, path: String) {
    runCatching {
        val file = java.io.File(path)
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share backup"))
    }
}
