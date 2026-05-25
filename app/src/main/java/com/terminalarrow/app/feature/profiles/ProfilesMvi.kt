package com.terminalarrow.app.feature.profiles

import com.terminalarrow.app.data.ConnectionProfile

sealed interface ProfilesUiState {
    data object Loading : ProfilesUiState
    data class Success(val profiles: List<ConnectionProfile>) : ProfilesUiState
    data class Error(val message: String) : ProfilesUiState
}

sealed interface ProfilesUiEvent {
    data class DeleteProfile(val profile: ConnectionProfile) : ProfilesUiEvent
    data object Refresh : ProfilesUiEvent
}

sealed interface ProfilesUiEffect {
    data class ShowSnackbar(val message: String) : ProfilesUiEffect
}
