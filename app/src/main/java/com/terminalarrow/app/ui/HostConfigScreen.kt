package com.terminalarrow.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.ForwardingRule

private enum class AuthMode { Password, Key }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostConfigScreen(
    onBack: () -> Unit,
    onConnect: (ConnectionProfile) -> Unit,
    onSave: (String, String, Int, String, String?, String?, String, List<ForwardingRule>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("Default") }
    var host by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("22") }
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var keyPath by remember { mutableStateOf("") }
    var authMode by remember { mutableStateOf(AuthMode.Password) }
    var revealPassword by remember { mutableStateOf(false) }
    var rules by remember { mutableStateOf(listOf<ForwardingRule>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New SSH connection") },
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
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Profile name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = group,
                onValueChange = { group = it },
                label = { Text("Group / folder") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = host,
                    onValueChange = { host = it },
                    label = { Text("Host") },
                    singleLine = true,
                    modifier = Modifier.weight(2f)
                )
                OutlinedTextField(
                    value = port,
                    onValueChange = { new -> port = new.filter { c -> c.isDigit() }.take(5) },
                    label = { Text("Port") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = user,
                onValueChange = { user = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(4.dp))
            Text("Authentication", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = authMode == AuthMode.Password,
                    onClick = { authMode = AuthMode.Password },
                    label = { Text("Password") }
                )
                FilterChip(
                    selected = authMode == AuthMode.Key,
                    onClick = { authMode = AuthMode.Key },
                    label = { Text("Private key") }
                )
            }
            when (authMode) {
                AuthMode.Password -> OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (revealPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { revealPassword = !revealPassword }) {
                            Icon(
                                imageVector = if (revealPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (revealPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                AuthMode.Key -> OutlinedTextField(
                    value = keyPath,
                    onValueChange = { keyPath = it },
                    label = { Text("Private key contents (PEM)") },
                    placeholder = { Text("-----BEGIN OPENSSH PRIVATE KEY-----…") },
                    minLines = 4,
                    maxLines = 8,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(4.dp))
            Text("Port forwarding", style = MaterialTheme.typography.titleMedium)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (rules.isEmpty()) {
                        Text(
                            "No rules. Add one to tunnel a local port through this session.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        rules.forEachIndexed { index, rule ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                AssistChip(
                                    onClick = {},
                                    label = { Text("${rule.type}  ${rule.localPort} → ${rule.remoteHost}:${rule.remotePort}") }
                                )
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = { rules = rules.toMutableList().also { it.removeAt(index) } }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Remove rule")
                                }
                            }
                        }
                    }
                    FilledTonalButton(onClick = {
                        rules = rules + ForwardingRule("LOCAL", 8080, "localhost", 80)
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Add local 8080 → localhost:80")
                    }
                }
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            fun validate(): Boolean {
                if (host.isBlank()) { errorMessage = "Host is required"; return false }
                if (user.isBlank()) { errorMessage = "Username is required"; return false }
                if (authMode == AuthMode.Password && pass.isBlank()) {
                    errorMessage = "Provide a password or switch to private key"; return false
                }
                if (authMode == AuthMode.Key && keyPath.isBlank()) {
                    errorMessage = "Paste the private key contents"; return false
                }
                errorMessage = null
                return true
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        if (validate()) {
                            val finalName = name.ifBlank { "${user}@${host}" }
                            onSave(
                                finalName,
                                host.trim(),
                                port.toIntOrNull() ?: 22,
                                user.trim(),
                                if (authMode == AuthMode.Password) pass.ifBlank { null } else null,
                                if (authMode == AuthMode.Key) keyPath.ifBlank { null } else null,
                                group.ifBlank { "Default" },
                                rules
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Save") }
                FilledTonalButton(
                    onClick = {
                        if (validate()) {
                            onConnect(
                                ConnectionProfile(
                                    name = name.ifBlank { "${user}@${host}" },
                                    host = host.trim(),
                                    port = port.toIntOrNull() ?: 22,
                                    username = user.trim(),
                                    password = if (authMode == AuthMode.Password) pass.ifBlank { null } else null,
                                    keyPath = if (authMode == AuthMode.Key) keyPath.ifBlank { null } else null,
                                    group = group.ifBlank { "Default" },
                                    forwardingRules = rules
                                )
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Connect") }
            }
        }
    }
}
