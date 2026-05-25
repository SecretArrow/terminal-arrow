package com.terminalarrow.app

import com.terminalarrow.app.data.TerminalDao
import com.terminalarrow.app.feature.profiles.ProfilesUiState
import com.terminalarrow.app.ui.ProfileViewModel
import com.terminalarrow.app.utils.BackupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var terminalDao: TerminalDao

    @Mock
    lateinit var backupManager: BackupManager

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        `when`(terminalDao.getAllProfiles()).thenReturn(flowOf(emptyList()))
        viewModel = ProfileViewModel(terminalDao, backupManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Success empty when no profiles`() = runTest {
        assertTrue(viewModel.uiState.value is ProfilesUiState.Success)
        val state = viewModel.uiState.value as ProfilesUiState.Success
        assertTrue(state.profiles.isEmpty())
    }
}
