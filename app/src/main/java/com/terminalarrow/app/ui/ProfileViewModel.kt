package com.terminalarrow.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.TerminalDao
import com.terminalarrow.app.feature.profiles.ProfilesUiState
import com.terminalarrow.app.feature.profiles.ProfilesUiEvent
import com.terminalarrow.app.feature.profiles.ProfilesUiEffect
import com.terminalarrow.app.utils.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val terminalDao: TerminalDao,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfilesUiState>(ProfilesUiState.Loading)
    val uiState: StateFlow<ProfilesUiState> = _uiState.asStateFlow()

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
            } catch (e: Exception) {
                viewModelScope.launch { _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Export failed")) }
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
                terminalDao.insertProfile(profile)
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Profile saved"))
            } catch (e: Exception) {
                _uiEffect.send(ProfilesUiEffect.ShowSnackbar("Failed to save profile"))
            }
        }
    }
}
