package com.terminalarrow.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        maybeRequestNotificationPermission()

        if (biometricHelper.isBiometricAvailable(this)) {
            biometricHelper.showBiometricPrompt(
                activity = this,
                onSuccess = { setupContent() },
                onError = { /* fall back to no-auth UI rather than killing the app */ setupContent() }
            )
        } else {
            setupContent()
        }
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
        setContent {
            TerminalArrowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TerminalArrowNavigation(themeManager, vibratorHelper)
                }
            }
        }
    }
}

@Composable
fun TerminalArrowNavigation(themeManager: ThemeManager, vibratorHelper: VibratorHelper) {
    val navController = rememberNavController()
    val terminalViewModel: TerminalViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val sftpViewModel: SFTPViewModel = hiltViewModel()
    val snippetViewModel: SnippetViewModel = hiltViewModel()
    val cloudViewModel: CloudViewModel = hiltViewModel()
    val editorViewModel: EditorViewModel = hiltViewModel()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "profiles") {
        composable("profiles") {
            ProfileListScreen(
                viewModel = profileViewModel,
                onProfileClick = { profile ->
                    terminalViewModel.onEvent(TerminalUiEvent.Connect(profile))
                    navController.navigate("terminal")
                },
                onSFTPClick = { profile ->
                    sftpViewModel.onEvent(
                        SftpUiEvent.Connect(profile.host, profile.port, profile.username, profile.password, profile.keyPath)
                    )
                    navController.navigate("sftp")
                },
                onAddProfile = { navController.navigate("config") },
                onSnippetClick = { navController.navigate("snippets") },
                onCloudClick = { navController.navigate("cloud") },
                onThemeClick = { navController.navigate("themes") },
                onFontClick = { navController.navigate("fonts") },
                onAboutClick = { navController.navigate("about") }
            )
        }
        composable("config") {
            HostConfigScreen(
                onBack = { navController.popBackStack() },
                onConnect = { profile ->
                    terminalViewModel.onEvent(TerminalUiEvent.Connect(profile))
                    navController.navigate("terminal")
                },
                onSave = { name, host, port, user, pass, keyPath, group, rules ->
                    profileViewModel.saveProfile(
                        ConnectionProfile(
                            name = name,
                            host = host,
                            port = port,
                            username = user,
                            password = pass,
                            keyPath = keyPath,
                            group = group,
                            forwardingRules = rules
                        )
                    )
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
