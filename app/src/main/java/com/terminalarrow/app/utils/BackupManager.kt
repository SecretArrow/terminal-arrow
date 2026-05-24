package com.terminalarrow.app.utils

import android.content.Context
import com.terminalarrow.app.data.ConnectionProfile
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
        val profiles = terminalDao.getAllProfiles().first()
        val jsonArray = JSONArray()
        
        profiles.forEach { profile ->
            val jsonObject = JSONObject().apply {
                put("name", profile.name)
                put("host", profile.host)
                put("port", profile.port)
                put("username", profile.username)
                put("password", profile.password)
            }
            jsonArray.put(jsonObject)
        }
        
        val backupFile = File(context.cacheDir, "terminal_arrow_backup.json")
        backupFile.writeText(jsonArray.toString(4))
        backupFile.absolutePath
    }

    suspend fun importProfiles(jsonString: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val profile = ConnectionProfile(
                    name = obj.getString("name"),
                    host = obj.getString("host"),
                    port = obj.getInt("port"),
                    username = obj.getString("username"),
                    password = obj.optString("password", null)
                )
                terminalDao.insertProfile(profile)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
