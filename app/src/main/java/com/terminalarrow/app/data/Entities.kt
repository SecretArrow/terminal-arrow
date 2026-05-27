package com.terminalarrow.app.data

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Keep
data class ForwardingRule(
    /** "LOCAL", "REMOTE", or "DYNAMIC" (SOCKS proxy). */
    val type: String,
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
    @ColumnInfo(defaultValue = "0") val lastConnectedAt: Long = 0L,
    @ColumnInfo(defaultValue = "30") val keepAliveSeconds: Int = 30,
    @ColumnInfo(defaultValue = "0") val useCompression: Boolean = false,
    @ColumnInfo(defaultValue = "0") val autoReconnect: Boolean = false,
    @ColumnInfo(defaultValue = "0") val strictHostKeyChecking: Boolean = true
)

@Keep
@Entity(
    tableName = "known_hosts",
    indices = [Index(value = ["host", "port"], unique = true)]
)
data class KnownHost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val host: String,
    val port: Int = 22,
    val keyType: String,
    /** SHA-256 fingerprint of the server key, base64 (sshj format). */
    val fingerprint: String,
    val firstSeen: Long = System.currentTimeMillis()
)

@Keep
@Entity(tableName = "snippets")
data class Snippet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val command: String
)
