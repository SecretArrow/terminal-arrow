package com.terminalarrow.app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.terminalarrow.app.ui.HostConfigScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HostConfigScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun connectButtonValidatesAndEmitsProfile() {
        var connectedHost = ""
        var connectedUser = ""
        composeTestRule.setContent {
            HostConfigScreen(
                onBack = {},
                onConnect = { profile ->
                    connectedHost = profile.host
                    connectedUser = profile.username
                },
                onSave = { _, _, _, _, _, _, _, _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Host").performTextInput("192.168.1.1")
        composeTestRule.onNodeWithText("Username").performTextInput("admin")
        composeTestRule.onNodeWithText("Password").performTextInput("secret")
        composeTestRule.onNodeWithText("Connect").performClick()

        assertEquals("192.168.1.1", connectedHost)
        assertEquals("admin", connectedUser)
    }

    @Test
    fun connectWithoutAuthShowsInlineError() {
        var connected = false
        composeTestRule.setContent {
            HostConfigScreen(
                onBack = {},
                onConnect = { connected = true },
                onSave = { _, _, _, _, _, _, _, _, _ -> }
            )
        }
        composeTestRule.onNodeWithText("Host").performTextInput("example.com")
        composeTestRule.onNodeWithText("Username").performTextInput("root")
        composeTestRule.onNodeWithText("Connect").performClick()
        assertTrue("Connect should not fire without auth", !connected)
    }
}
