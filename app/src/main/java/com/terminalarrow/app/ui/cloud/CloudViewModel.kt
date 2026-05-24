package com.terminalarrow.app.ui.cloud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.TerminalDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CloudViewModel @Inject constructor(
    private val terminalDao: TerminalDao
) : ViewModel() {

    private val _instances = MutableStateFlow<List<CloudInstance>>(emptyList())
    val instances: StateFlow<List<CloudInstance>> = _instances

    fun fetchAWSInstances(accessKey: String, secretKey: String, region: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val ec2 = AmazonEC2Client(BasicAWSCredentials(accessKey, secretKey))
            ec2.setEndpoint("ec2.$region.amazonaws.com")
            val response = ec2.describeInstances(DescribeInstancesRequest())
            val list = response.reservations.flatMap { res ->
                res.instances.map { inst ->
                    CloudInstance(inst.instanceId, inst.publicIpAddress ?: inst.privateIpAddress, "ec2-user")
                }
            }
            _instances.value = list
        }
    }

    fun fetchDigitalOceanInstances(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.digitalocean.com/v2/droplets")
                .addHeader("Authorization", "Bearer $token")
                .build()
            
            val response = client.newCall(request).execute()
            val json = JSONObject(response.body?.string() ?: "{}")
            val droplets = json.getJSONArray("droplets")
            val list = mutableListOf<CloudInstance>()
            for (i in 0 until droplets.length()) {
                val d = droplets.getJSONObject(i)
                val ip = d.getJSONArray("networks").getJSONObject(0).getJSONArray("v4").getJSONObject(0).getString("ip_address")
                list.add(CloudInstance(d.getString("name"), ip, "root"))
            }
            _instances.value = list
        }
    }

    fun importInstance(instance: CloudInstance) {
        viewModelScope.launch {
            terminalDao.insertProfile(ConnectionProfile(name = instance.id, host = instance.ip, username = instance.user))
        }
    }
}

data class CloudInstance(val id: String, val ip: String, val user: String)
