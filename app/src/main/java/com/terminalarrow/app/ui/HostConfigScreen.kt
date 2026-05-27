package com.terminalarrow.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
    onSave: (Int, String, String, Int, String, String?, String?, String, List<ForwardingRule>) -> Unit,
    initial: ConnectionProfile? = null
) {
    val editing = initial != null
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var group by remember { mutableStateOf(initial?.group ?: "Default") }
    var host by remember { mutableStateOf(initial?.host ?: "") }
    var port by remember { mutableStateOf(initial?.port?.toString() ?: "22") }
    var user by remember { mutableStateOf(initial?.username ?: "") }
    var pass by remember { mutableStateOf(initial?.password ?: "") }
    var keyPath by remember { mutableStateOf(initial?.keyPath ?: "") }
    var authMode by remember {
        mutableStateOf(if (initial?.keyPath.isNullOrBlank()) AuthMode.Password else AuthMode.Key)
    }
    var revealPassword by remember { mutableStateOf(false) }
    var rules by remember { mutableStateOf(initial?.forwardingRules ?: emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (editing) "Edit host" else "New host") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionHeader("Identity")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Display name (optional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = group,
                onValueChange = { group = it },
                label = { Text("Group") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            SectionHeader("Connection")
            OutlinedTextField(
                value = host,
                onValueChange = { host = it; errorMessage = null },
                label = { Text("Host or IP *") },
                singleLine = true,
                isError = host.isBlank() && errorMessage != null,
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = port,
                    onValueChange = { p -> port = p.filter { it.isDigit() }.take(5) },
                    label = { Text("Port") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it; errorMessage = null },
                    label = { Text("Username *") },
                    singleLine = true,
                    modifier = Modifier.weight(2f)
                )
            }

            SectionHeader("Authentication")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = authMode == AuthMode.Password,
                    onClick = { authMode = AuthMode.Password },
                    label = { Text("Use password") }
                )
                FilterChip(
                    selected = authMode == AuthMode.Key,
                    onClick = { authMode = AuthMode.Key },
                    label = { Text("Use private key") }
                )
            }
            when (authMode) {
                AuthMode.Password -> OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it; errorMessage = null },
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
                    onValueChange = { keyPath = it; errorMessage = null },
                    label = { Text("Private key (PEM content)") },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SectionHeader("Port forwarding (optional)")
            rules.forEachIndexed { idx, rule ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${rule.type} :${rule.localPort}", style = MaterialTheme.typography.titleSmall)
                            val target = rule.remoteHost?.let { "${rule.remoteHost}:${rule.remotePort}" } ?: "—"
                            Text("→ $target", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = {
                            rules = rules.toMutableList().also { it.removeAt(idx) }
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Remove rule")
                        }
                    }
                }
            }
            AssistChip(
                onClick = {
                    rules = rules + ForwardingRule(
                        type = "LOCAL",
                        localPort = 8080,
                        remoteHost = "localhost",
                        remotePort = 80
                    )
                },
                label = { Text("Add LOCAL forward") },
                leadingIcon = { Icon(Icons.Filled.Add, contentDescription = null) }
            )

            if (errorMessage != null) {
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        val validation = validate(host, user, port, authMode, pass, keyPath)
                        if (validation != null) {
                            errorMessage = validation
                            return@OutlinedButton
                        }
                        onSave(
                            initial?.id ?: 0,
                            name.ifBlank { host },
                            host.trim(),
                            port.toIntOrNull() ?: 22,
                            user.trim(),
                            pass.ifBlank { null },
                            keyPath.ifBlank { null },
                            group.ifBlank { "Default" },
                            rules
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (editing) "Update" else "Save")
                }
                FilledTonalButton(
                    onClick = {
                        val validation = validate(host, user, port, authMode, pass, keyPath)
                        if (validation != null) {
                            errorMessage = validation
                            return@FilledTonalButton
                        }
                        onConnect(
                            ConnectionProfile(
                                id = initial?.id ?: 0,
                                name = name.ifBlank { host },
                                host = host.trim(),
                                port = port.toIntOrNull() ?: 22,
                                username = user.trim(),
                                password = pass.ifBlank { null },
                                keyPath = keyPath.ifBlank { null },
                                group = group.ifBlank { "Default" },
                                forwardingRules = rules,
                                isFavorite = initial?.isFavorite ?: false,
                                lastConnectedAt = initial?.lastConnectedAt ?: 0L
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Connect")
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(label: String) {
    Text(
        text = label.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

private fun validate(
    host: String,
    user: String,
    port: String,
    authMode: AuthMode,
    pass: String,
    keyPath: String
): String? {
    if (host.isBlank()) return "Host is required"
    if (user.isBlank()) return "Username is required"
    val portInt = port.toIntOrNull()
    if (portInt == null || portInt !in 1..65535) return "Port must be between 1 and 65535"
    when (authMode) {
        AuthMode.Password -> if (pass.isBlank()) return "Password is required (or switch to Private key)"
        AuthMode.Key -> if (keyPath.isBlank()) return "Private key is required (or switch to Password)"
    }
    return null
}
