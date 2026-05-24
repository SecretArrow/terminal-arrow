package com.terminalarrow.app.ui.cloud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudImportScreen(viewModel: CloudViewModel) {
    var token by remember { mutableStateOf("") }
    val instances by viewModel.instances.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Import from DigitalOcean", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = token, onValueChange = { token = it }, label = { Text("DO API Token") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { viewModel.fetchDigitalOceanInstances(token) }) { Text("Fetch Droplets") }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(instances) { inst ->
                ListItem(
                    headlineContent = { Text(inst.id) },
                    supportingContent = { Text(inst.ip) },
                    modifier = Modifier.clickable { viewModel.importInstance(inst) },
                    trailingContent = { Text("Import") }
                )
                Divider()
            }
        }
    }
}
