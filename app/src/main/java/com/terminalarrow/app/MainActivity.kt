package com.terminalarrow.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.terminalarrow.app.core.ui.theme.TerminalArrowTheme
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.feature.editor.EditorUiEvent
import com.terminalarrow.app.feature.sftp.SftpUiEvent
import com.terminalarrow.app.feature.terminal.TerminalUiEvent
import com.terminalarrow.app.ui.AboutScreen
import com.terminalarrow.app.ui.EditorScreen
import com.terminalarrow.app.ui.EditorViewModel
import com.terminalarrow.app.ui.HostConfigScreen
import com.terminalarrow.app.ui.ProfileListScreen
import com.terminalarrow.app.ui.ProfileViewModel
import com.terminalarrow.app.ui.SFTPBrowserScreen
import com.terminalarrow.app.ui.SFTPViewModel
import com.terminalarrow.app.ui.SettingsScreen
import com.terminalarrow.app.ui.TerminalScreen
import com.terminalarrow.app.ui.TerminalViewModel
import com.terminalarrow.app.ui.cloud.CloudImportScreen
import com.terminalarrow.app.ui.cloud.CloudViewModel
import com.terminalarrow.app.ui.snippets.SnippetScreen
import com.terminalarrow.app.ui.snippets.SnippetViewModel
import com.terminalarrow.app.ui.theme.FontSelectionScreen
import com.terminalarrow.app.ui.theme.ThemeManager
import com.terminalarrow.app.ui.theme.ThemeSelectionScreen
import com.terminalarrow.app.utils.BiometricHelper
import com.terminalarrow.app.utils.VibratorHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject lateinit var biometricHelper: BiometricHelper
    @Inject lateinit var themeManager: ThemeManager
    @Inject lateinit var vibratorHelper: VibratorHelper

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* result handled silently */ }

    // Honored once at startup; routes intent action through to navigation.
    private var pendingShortcutAction: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        pendingShortcutAction = intent?.action

        maybeRequestNotificationPermission()

        if (biometricHelper.isBiometricAvailable(this)) {
            biometricHelper.showBiometricPrompt(
                activity = this,
                onSuccess = { setupContent() },
                onError = { setupContent() }
            )
        } else {
            setupContent()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // App shortcuts re-launching the activity should also navigate.
        pendingShortcutAction = intent?.action
        setupContent()
    }

    private fun maybeRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                runCatching { notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
            }
        }
    }

    private fun setupContent() {
        val action = pendingShortcutAction
        pendingShortcutAction = null
        setContent {
            TerminalArrowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TerminalArrowNavigation(themeManager, vibratorHelper, action)
                }
            }
        }
    }
}

@Composable
fun TerminalArrowNavigation(
    themeManager: ThemeManager,
    vibratorHelper: VibratorHelper,
    initialAction: String? = null
) {
    val navController = rememberNavController()
    val terminalViewModel: TerminalViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val sftpViewModel: SFTPViewModel = hiltViewModel()
    val snippetViewModel: SnippetViewModel = hiltViewModel()
    val cloudViewModel: CloudViewModel = hiltViewModel()
    val editorViewModel: EditorViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(initialAction) {
        when (initialAction) {
            "com.terminalarrow.app.ACTION_NEW_HOST" -> navController.navigate("config/new")
            "com.terminalarrow.app.ACTION_SETTINGS" -> navController.navigate("settings")
        }
    }

    fun startConnection(profile: ConnectionProfile) {
        terminalViewModel.onEvent(TerminalUiEvent.Connect(profile))
        if (profile.id != 0) profileViewModel.markConnected(profile.id)
        navController.navigate("terminal")
    }

    NavHost(navController = navController, startDestination = "profiles") {
        composable("profiles") {
            ProfileListScreen(
                viewModel = profileViewModel,
                onProfileClick = { profile -> startConnection(profile) },
                onSFTPClick = { profile ->
                    sftpViewModel.onEvent(
                        SftpUiEvent.Connect(profile.host, profile.port, profile.username, profile.password, profile.keyPath)
                    )
                    navController.navigate("sftp")
                },
                onAddProfile = { navController.navigate("config/new") },
                onEditProfile = { profile -> navController.navigate("config/${profile.id}") },
                onSettingsClick = { navController.navigate("settings") }
            )
        }
        composable(
            route = "config/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val rawId = entry.arguments?.getString("id") ?: "new"
            val id = rawId.toIntOrNull()
            var initial by remember(id) { mutableStateOf<ConnectionProfile?>(null) }
            LaunchedEffect(id) {
                initial = if (id == null || id == 0) null else profileViewModel.loadProfile(id)
            }
            HostConfigScreen(
                onBack = { navController.popBackStack() },
                initial = initial,
                onConnect = { profile -> startConnection(profile) },
                onSave = { existingId, name, host, port, user, pass, keyPath, group, rules ->
                    val merged = (initial ?: ConnectionProfile(name = name, host = host, username = user)).copy(
                        id = existingId,
                        name = name,
                        host = host,
                        port = port,
                        username = user,
                        password = pass,
                        keyPath = keyPath,
                        group = group,
                        forwardingRules = rules
                    )
                    profileViewModel.saveProfile(merged)
                    navController.popBackStack()
                }
            )
        }
        composable("terminal") {
            TerminalScreen(terminalViewModel, snippetViewModel, themeManager, vibratorHelper)
        }
        composable("sftp") {
            SFTPBrowserScreen(sftpViewModel) { path ->
                editorViewModel.onEvent(EditorUiEvent.LoadFile(context, path))
                navController.navigate("editor")
            }
        }
        composable("editor") {
            EditorScreen(editorViewModel) { navController.popBackStack() }
        }
        composable("settings") {
            SettingsScreen(
                profileViewModel = profileViewModel,
                onBack = { navController.popBackStack() },
                onThemeClick = { navController.navigate("themes") },
                onFontClick = { navController.navigate("fonts") },
                onSnippetsClick = { navController.navigate("snippets") },
                onCloudClick = { navController.navigate("cloud") },
                onAboutClick = { navController.navigate("about") }
            )
        }
        composable("snippets") {
            SnippetScreen(snippetViewModel) { command ->
                terminalViewModel.onEvent(TerminalUiEvent.SendCommand(command))
                navController.navigate("terminal")
            }
        }
        composable("cloud") { CloudImportScreen(cloudViewModel) }
        composable("themes") {
            ThemeSelectionScreen(themeManager) { navController.popBackStack() }
        }
        composable("fonts") {
            FontSelectionScreen(themeManager) { navController.popBackStack() }
        }
        composable("about") { AboutScreen(onBack = { navController.popBackStack() }) }
    }
}
