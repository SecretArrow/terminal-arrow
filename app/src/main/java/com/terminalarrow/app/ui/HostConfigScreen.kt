package com.terminalarrow.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostConfigScreen(
    onConnect: (com.terminalarrow.app.data.ConnectionProfile) -> Unit, 
    onSave: (String, String, Int, String, String, String, List<com.terminalarrow.app.data.ForwardingRule>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("Default") }
    var host by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("22") }
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "New SSH Connection", style = MaterialTheme.typography.headlineMedium)
        
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Profile Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = group, onValueChange = { group = it }, label = { Text("Group/Folder") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = host, onValueChange = { host = it }, label = { Text("Host") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = port, onValueChange = { port = it }, label = { Text("Port") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = user, onValueChange = { user = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        
        var rules by remember { mutableStateOf(listOf<com.terminalarrow.app.data.ForwardingRule>()) }

        // Port Forwarding Section
        Text("Port Forwarding", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { rules = rules + com.terminalarrow.app.data.ForwardingRule("LOCAL", 8080, "localhost", 80) }) {
            Text("Add Local Rule (8080->80)")
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onSave(name, host, port.toIntOrNull() ?: 22, user, pass, group, rules) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
            Button(
                onClick = { onConnect(com.terminalarrow.app.data.ConnectionProfile(name=name, host=host, port=port.toIntOrNull()?:22, username=user, password=pass, group=group, forwardingRules=rules)) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Connect")
            }
        }
    }
}
