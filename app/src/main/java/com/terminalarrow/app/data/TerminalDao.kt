package com.terminalarrow.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TerminalDao {
    @Query("SELECT * FROM connection_profiles ORDER BY isFavorite DESC, lastConnectedAt DESC, name ASC")
    fun getAllProfiles(): Flow<List<ConnectionProfile>>

    @Query("SELECT * FROM connection_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: Int): ConnectionProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ConnectionProfile): Long

    @Update
    suspend fun updateProfile(profile: ConnectionProfile)

    @Delete
    suspend fun deleteProfile(profile: ConnectionProfile)

    @Query("UPDATE connection_profiles SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Int, favorite: Boolean)

    @Query("UPDATE connection_profiles SET lastConnectedAt = :ts WHERE id = :id")
    suspend fun markConnected(id: Int, ts: Long)

    @Query("SELECT * FROM snippets ORDER BY name ASC")
    fun getAllSnippets(): Flow<List<Snippet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnippet(snippet: Snippet)

    @Delete
    suspend fun deleteSnippet(snippet: Snippet)
}
