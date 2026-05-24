package com.terminalarrow.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.terminalarrow.app.data.ConnectionProfile
import com.terminalarrow.app.ui.*
import com.terminalarrow.app.ui.cloud.*
import com.terminalarrow.app.ui.snippets.*
import com.terminalarrow.app.ui.theme.*
import com.terminalarrow.app.utils.BiometricHelper
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.FragmentActivity
import androidx.compose.ui.platform.LocalContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject lateinit var biometricHelper: BiometricHelper
    @Inject lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (biometricHelper.isBiometricAvailable(this)) {
            biometricHelper.showBiometricPrompt(this, 
                onSuccess = { setupContent() },
                onError = { finish() }
            )
        } else {
            setupContent()
        }
    }

    private fun setupContent() {
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TerminalArrowNavigation(themeManager)
                }
            }
        }
    }
}

@Composable
fun TerminalArrowNavigation(themeManager: ThemeManager) {
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
                    terminalViewModel.connect(profile)
                    navController.navigate("terminal")
                },
                onSFTPClick = { profile ->
                    sftpViewModel.connectAndList(profile.host, profile.port, profile.username, profile.password, profile.keyPath)
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
                onConnect = { profile ->
                    terminalViewModel.connect(profile)
                    navController.navigate("terminal")
                },
                onSave = { name, host, port, user, pass, keyPath, group, rules ->
                    profileViewModel.saveProfile(ConnectionProfile(name = name, host = host, port = port, username = user, password = pass, keyPath = keyPath, group = group, forwardingRules = rules))
                    navController.popBackStack()
                }
            )
        }
        composable("terminal") {
            TerminalScreen(terminalViewModel, themeManager)
        }
        composable("sftp") {
            SFTPBrowserScreen(sftpViewModel) { path ->
                editorViewModel.loadFile(context, path)
                navController.navigate("editor")
            }
        }
        composable("editor") {
            EditorScreen(editorViewModel) {
                navController.popBackStack()
            }
        }
        composable("snippets") {
            SnippetScreen(snippetViewModel) { command ->
                terminalViewModel.sendCommand(command)
                navController.navigate("terminal")
            }
        }
        composable("cloud") {
            CloudImportScreen(cloudViewModel)
        }
        composable("themes") {
            ThemeSelectionScreen(themeManager) {
                navController.popBackStack()
            }
        }
        composable("fonts") {
            FontSelectionScreen(themeManager) {
                navController.popBackStack()
            }
        }
        composable("about") {
            AboutScreen()
        }
    }
}
