package com.terminalarrow.app.ui.cloud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.TerminalDao
import com.terminalarrow.app.feature.cloud.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CloudViewModel @Inject constructor(
    private val terminalDao: TerminalDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<CloudUiState>(CloudUiState.Idle)
    val uiState: StateFlow<CloudUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<CloudUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<CloudUiEffect> = _uiEffect.receiveAsFlow()

    fun onEvent(event: CloudUiEvent) {
        when (event) {
            is CloudUiEvent.FetchAWS -> fetchAWSInstances(event.accessKey, event.secretKey, event.region)
            is CloudUiEvent.FetchDigitalOcean -> fetchDigitalOceanInstances(event.token)
            is CloudUiEvent.ImportInstance -> importInstance(event.instance)
        }
    }

    @Suppress("DEPRECATION")
    private fun fetchAWSInstances(accessKey: String, secretKey: String, region: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = CloudUiState.Loading
            try {
                val ec2 = AmazonEC2Client(BasicAWSCredentials(accessKey, secretKey))
                ec2.setEndpoint("ec2.$region.amazonaws.com")
                val response = ec2.describeInstances(DescribeInstancesRequest())
                val list = response.reservations.flatMap { res ->
                    res.instances.map { inst ->
                        CloudInstance(inst.instanceId, inst.publicIpAddress ?: inst.privateIpAddress, "ec2-user")
                    }
                }
                _uiState.value = CloudUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = CloudUiState.Error("AWS Error: ${e.message}")
            }
        }
    }

    private fun fetchDigitalOceanInstances(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = CloudUiState.Loading
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.digitalocean.com/v2/droplets")
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                
                val response = client.newCall(request).execute()
                val bodyString = response.body?.string()
                if (bodyString != null) {
                    val json = JSONObject(bodyString)
                    val droplets = json.optJSONArray("droplets")
                    if (droplets != null) {
                        val list = mutableListOf<CloudInstance>()
                        for (i in 0 until droplets.length()) {
                            val d = droplets.getJSONObject(i)
                            val networks = d.optJSONObject("networks")
                            val v4 = networks?.optJSONArray("v4")
                            val ip = v4?.optJSONObject(0)?.optString("ip_address")
                            if (ip != null) {
                                list.add(CloudInstance(d.getString("name"), ip, "root"))
                            }
                        }
                        _uiState.value = CloudUiState.Success(list)
                    } else {
                        _uiState.value = CloudUiState.Error("No droplets found")
                    }
                } else {
                    _uiState.value = CloudUiState.Error("Empty response from DigitalOcean")
                }
            } catch (e: Exception) {
                _uiState.value = CloudUiState.Error("DigitalOcean Error: ${e.message}")
            }
        }
    }

    private fun importInstance(instance: CloudInstance) {
        viewModelScope.launch {
            try {
                terminalDao.insertProfile(ConnectionProfile(name = instance.id, host = instance.ip, username = instance.user))
                _uiEffect.send(CloudUiEffect.ShowSnackbar("Instance imported successfully"))
            } catch (e: Exception) {
                _uiEffect.send(CloudUiEffect.ShowSnackbar("Import failed"))
            }
        }
    }
}
