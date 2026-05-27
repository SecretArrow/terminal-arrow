package com.terminalarrow.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TerminalDao {
    // ----- Connection profiles -----
    @Query("SELECT * FROM connection_profiles ORDER BY isFavorite DESC, lastConnectedAt DESC, name ASC")
    fun getAllProfiles(): Flow<List<ConnectionProfile>>

    @Query("SELECT * FROM connection_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfile(id: Int): ConnectionProfile?

    @Query("UPDATE connection_profiles SET isFavorite = :fav WHERE id = :id")
    suspend fun setFavorite(id: Int, fav: Boolean)

    @Query("UPDATE connection_profiles SET lastConnectedAt = :ts WHERE id = :id")
    suspend fun markConnected(id: Int, ts: Long)

    @Insert
    suspend fun insertProfile(profile: ConnectionProfile): Long

    @Update
    suspend fun updateProfile(profile: ConnectionProfile)

    @Delete
    suspend fun deleteProfile(profile: ConnectionProfile)

    // ----- Snippets -----
    @Query("SELECT * FROM snippets")
    fun getAllSnippets(): Flow<List<Snippet>>

    @Insert
    suspend fun insertSnippet(snippet: Snippet)

    @Delete
    suspend fun deleteSnippet(snippet: Snippet)

    // ----- Known hosts (TOFU) -----
    @Query("SELECT * FROM known_hosts WHERE host = :host AND port = :port LIMIT 1")
    suspend fun getKnownHost(host: String, port: Int): KnownHost?

    @Query("SELECT * FROM known_hosts ORDER BY firstSeen DESC")
    fun getAllKnownHosts(): Flow<List<KnownHost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertKnownHost(host: KnownHost)

    @Delete
    suspend fun deleteKnownHost(host: KnownHost)

    @Query("DELETE FROM known_hosts WHERE host = :host AND port = :port")
    suspend fun deleteKnownHostByAddress(host: String, port: Int)
}
