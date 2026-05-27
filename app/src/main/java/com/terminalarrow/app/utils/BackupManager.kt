package com.terminalarrow.app.utils

import android.content.Context
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.ForwardingRule
import com.terminalarrow.app.data.TerminalDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val terminalDao: TerminalDao
) {

    suspend fun exportProfiles(): String = withContext(Dispatchers.IO) {
        try {
            val profiles = terminalDao.getAllProfiles().first()
            val jsonArray = JSONArray()
            profiles.forEach { profile ->
                jsonArray.put(profile.toJson())
            }
            val backupFile = File(context.cacheDir, "terminal_arrow_backup.json")
            backupFile.writeText(jsonArray.toString(2))
            backupFile.absolutePath
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun importProfiles(jsonString: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                terminalDao.insertProfile(obj.toConnectionProfile())
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun ConnectionProfile.toJson(): JSONObject = JSONObject().apply {
        put("name", name)
        put("host", host)
        put("port", port)
        put("username", username)
        if (password != null) put("password", password) else put("password", JSONObject.NULL)
        if (keyPath != null) put("keyPath", keyPath) else put("keyPath", JSONObject.NULL)
        put("group", group ?: "Default")
        val rules = JSONArray()
        forwardingRules.forEach { rule ->
            rules.put(JSONObject().apply {
                put("type", rule.type)
                put("localPort", rule.localPort)
                if (rule.remoteHost != null) put("remoteHost", rule.remoteHost) else put("remoteHost", JSONObject.NULL)
                if (rule.remotePort != null) put("remotePort", rule.remotePort) else put("remotePort", JSONObject.NULL)
            })
        }
        put("forwardingRules", rules)
        put("isFavorite", isFavorite)
        put("lastConnectedAt", lastConnectedAt)
        put("keepAliveSeconds", keepAliveSeconds)
        put("useCompression", useCompression)
        put("autoReconnect", autoReconnect)
        put("strictHostKeyChecking", strictHostKeyChecking)
    }

    private fun JSONObject.toConnectionProfile(): ConnectionProfile {
        val rules = mutableListOf<ForwardingRule>()
        val rulesJson = optJSONArray("forwardingRules")
        if (rulesJson != null) {
            for (i in 0 until rulesJson.length()) {
                val r = rulesJson.optJSONObject(i) ?: continue
                rules.add(
                    ForwardingRule(
                        type = r.optString("type", "LOCAL"),
                        localPort = r.optInt("localPort", 0),
                        remoteHost = r.optStringOrNull("remoteHost"),
                        remotePort = if (r.isNull("remotePort")) null else r.optInt("remotePort")
                    )
                )
            }
        }
        return ConnectionProfile(
            name = optString("name", "Unnamed"),
            host = optString("host", ""),
            port = optInt("port", 22),
            username = optString("username", ""),
            password = optStringOrNull("password"),
            keyPath = optStringOrNull("keyPath"),
            group = optString("group", "Default"),
            forwardingRules = rules,
            isFavorite = optBoolean("isFavorite", false),
            lastConnectedAt = optLong("lastConnectedAt", 0L),
            keepAliveSeconds = optInt("keepAliveSeconds", 30),
            useCompression = optBoolean("useCompression", false),
            autoReconnect = optBoolean("autoReconnect", false),
            strictHostKeyChecking = optBoolean("strictHostKeyChecking", true)
        )
    }

    private fun JSONObject.optStringOrNull(key: String): String? =
        if (!has(key) || isNull(key)) null else optString(key)
}
