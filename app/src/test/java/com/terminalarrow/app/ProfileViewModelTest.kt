package com.terminalarrow.app

import com.terminalarrow.app.data.TerminalDao
import com.terminalarrow.app.feature.profiles.ProfilesUiState
import com.terminalarrow.app.ui.ProfileViewModel
import com.terminalarrow.app.utils.BackupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
    fun `state becomes Success empty after loadProfiles completes`() = runTest {
        // The init block schedules the load on the main dispatcher; advance it.
        advanceUntilIdle()
        val current = viewModel.uiState.value
        assertTrue("Expected Success but was $current", current is ProfilesUiState.Success)
        val success = current as ProfilesUiState.Success
        assertTrue(success.profiles.isEmpty())
    }
}
