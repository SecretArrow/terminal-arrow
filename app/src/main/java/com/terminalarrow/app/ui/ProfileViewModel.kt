package com.terminalarrow.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.TerminalDao
import com.terminalarrow.app.feature.profiles.ProfilesUiEffect
import com.terminalarrow.app.feature.profiles.ProfilesUiEvent
import com.terminalarrow.app.feature.profiles.ProfilesUiState
import com.terminalarrow.app.utils.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ProfileSortMode { Recent, Alphabetical, Favorites }

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val terminalDao: TerminalDao,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfilesUiState>(ProfilesUiState.Loading)
    val uiState: StateFlow<ProfilesUiState> = _uiState.asStateFlow()

    private val _sortMode = MutableStateFlow(ProfileSortMode.Recent)
    val sortMode: StateFlow<ProfileSortMode> = _sortMode.asStateFlow()

    private val _uiEffect = Channel<ProfilesUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<ProfilesUiEffect> = _uiEffect.receiveAsFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        terminalDao.getAllProfiles()
            .map { ProfilesUiState.Success(it) }
            .onEach { _uiState.value = it }
            .catch { _uiState.value = ProfilesUiState.Error(it.message ?: "Unknown error") }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ProfilesUiEvent) {
        when (event) {
            is ProfilesUiEvent.DeleteProfile -> deleteProfile(event.profile)
            ProfilesUiEvent.Refresh -> loadProfiles()
        }
    }

    fun setSortMode(mode: ProfileSortMode) {
        _sortMode.value = mode
    }

    fun toggleFavorite(profile: ConnectionProfile) {
        viewModelScope.launch {
            runCatching { terminalDao.setFavorite(profile.id, !profile.isFavorite) }
        }
    }

    fun markConnected(profileId: Int) {
        viewModelScope.launch {
            runCatching { terminalDao.markConnected(profileId, System.currentTimeMillis()) }
        }
    }

    suspend fun loadProfile(id: Int): ConnectionProfile? = runCatching {
        terminalDao.getProfile(id)
    }.getOrNull()

    private fun deleteProfile(profile: ConnectionProfile) {
        viewModelScope.launch {
            try {
                terminalDao.deleteProfile(profile)
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Profile deleted"))
            } catch (e: Exception) {
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Failed to delete profile"))
            }
        }
    }

    fun exportData(onComplete: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val path = backupManager.exportProfiles()
                onComplete(path)
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Exported to $path"))
            } catch (e: Exception) {
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Export failed"))
            }
        }
    }

    fun importData(json: String) {
        viewModelScope.launch {
            try {
                backupManager.importProfiles(json)
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Import successful"))
            } catch (e: Exception) {
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Import failed"))
            }
        }
    }

    fun saveProfile(profile: ConnectionProfile) {
        viewModelScope.launch {
            try {
                if (profile.id == 0) {
                    terminalDao.insertProfile(profile)
                    _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Profile saved"))
                } else {
                    terminalDao.updateProfile(profile)
                    _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Profile updated"))
                }
            } catch (e: Exception) {
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Failed to save profile"))
            }
        }
    }
}
