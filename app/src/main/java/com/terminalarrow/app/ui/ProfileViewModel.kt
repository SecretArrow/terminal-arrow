package com.terminalarrow.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.data.TerminalDao
import com.terminalarrow.app.utils.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val terminalDao: TerminalDao,
    private val backupManager: BackupManager
) : ViewModel() {

    val profiles: StateFlow<List<ConnectionProfile>> = terminalDao.getAllProfiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun exportData(onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val path = backupManager.exportProfiles()
            onComplete(path)
        }
    }

    fun importData(json: String) {
        viewModelScope.launch {
            backupManager.importProfiles(json)
        }
    }

    fun saveProfile(profile: ConnectionProfile) {
        viewModelScope.launch {
            terminalDao.insertProfile(profile)
        }
    }

    fun deleteProfile(profile: ConnectionProfile) {
        viewModelScope.launch {
            terminalDao.deleteProfile(profile)
        }
    }
}
