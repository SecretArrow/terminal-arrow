package com.terminalarrow.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terminalarrow.app.data.KnownHost
import com.terminalarrow.app.data.TerminalDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnownHostsViewModel @Inject constructor(
    private val dao: TerminalDao
) : ViewModel() {
    val hosts: Flow<List<KnownHost>> = dao.getAllKnownHosts()

    fun delete(host: KnownHost) {
        viewModelScope.launch { runCatching { dao.deleteKnownHost(host) } }
    }
}
