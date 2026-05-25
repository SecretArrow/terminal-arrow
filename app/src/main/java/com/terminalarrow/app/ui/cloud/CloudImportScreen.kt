package com.terminalarrow.app.ui.cloud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.terminalarrow.app.feature.cloud.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudImportScreen(viewModel: CloudViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var accessKey by remember { mutableStateOf("") }
    var secretKey by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("us-east-1") }
    var token by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is CloudUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Cloud Import") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Import from AWS EC2", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = accessKey, onValueChange = { accessKey = it }, label = { Text("Access Key") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = secretKey, onValueChange = { secretKey = it }, label = { Text("Secret Key") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = region, onValueChange = { region = it }, label = { Text("Region") }, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = { viewModel.onEvent(CloudUiEvent.FetchAWS(accessKey, secretKey, region)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Fetch AWS Instances")
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Import from DigitalOcean", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = token, onValueChange = { token = it }, label = { Text("API Token") }, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = { viewModel.onEvent(CloudUiEvent.FetchDigitalOcean(token)) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Fetch Droplets")
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (val state = uiState) {
                    is CloudUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is CloudUiState.Error -> Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                    is CloudUiState.Success -> {
                        InstanceList(
                            instances = state.instances,
                            onImport = { viewModel.onEvent(CloudUiEvent.ImportInstance(it)) }
                        )
                    }
                    CloudUiState.Idle -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Fetch instances to start importing", color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InstanceList(instances: List<CloudInstance>, onImport: (CloudInstance) -> Unit) {
    LazyColumn {
        items(instances) { instance ->
            ListItem(
                headlineContent = { Text(instance.id) },
                supportingContent = { Text("${instance.user}@${instance.ip}") },
                trailingContent = {
                    IconButton(onClick = { onImport(instance) }) {
                        Icon(Icons.Default.CloudDownload, contentDescription = "Import")
                    }
                }
            )
            Divider()
        }
    }
}
