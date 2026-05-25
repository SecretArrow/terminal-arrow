package com.terminalarrow.app.feature.cloud

import com.terminalarrow.app.ui.cloud.CloudInstance

sealed interface CloudUiState {
    data object Idle : CloudUiState
    data object Loading : CloudUiState
    data class Success(val instances: List<CloudInstance>) : CloudUiState
    data class Error(val message: String) : CloudUiState
}

sealed interface CloudUiEvent {
    data class FetchAWS(val accessKey: String, val secretKey: String, val region: String) : CloudUiEvent
    data class FetchDigitalOcean(val token: String) : CloudUiEvent
    data class ImportInstance(val instance: CloudInstance) : CloudUiEvent
}

sealed interface CloudUiEffect {
    data class ShowSnackbar(val message: String) : CloudUiEffect
}
