package com.terminalarrow.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TerminalDao {
    @Query("SELECT * FROM connection_profiles")
    fun getAllProfiles(): Flow<List<ConnectionProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ConnectionProfile)

    @Delete
    suspend fun deleteProfile(profile: ConnectionProfile)

    @Query("SELECT * FROM snippets")
    fun getAllSnippets(): Flow<List<Snippet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnippet(snippet: Snippet)

    @Delete
    suspend fun deleteSnippet(snippet: Snippet)
}
