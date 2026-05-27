package com.terminalarrow.app.data

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
data class ForwardingRule(
    val type: String, // "LOCAL", "REMOTE", "DYNAMIC"
    val localPort: Int,
    val remoteHost: String? = null,
    val remotePort: Int? = null
)

@Keep
@Entity(tableName = "connection_profiles")
data class ConnectionProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val host: String,
    val port: Int = 22,
    val username: String,
    val password: String? = null,
    val keyPath: String? = null,
    val group: String? = "Default",
    val forwardingRules: List<ForwardingRule> = emptyList(),
    @ColumnInfo(defaultValue = "0") val isFavorite: Boolean = false,
    @ColumnInfo(defaultValue = "0") val lastConnectedAt: Long = 0L
)

@Keep
@Entity(tableName = "snippets")
data class Snippet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val command: String
)
