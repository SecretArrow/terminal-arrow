package com.terminalarrow.app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.terminalarrow.app.ui.HostConfigScreen
import org.junit.Rule
import org.junit.Test

class HostConfigScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHostConfigInput() {
        var connected = false
        composeTestRule.setContent {
            HostConfigScreen { _, _, _, _ ->
                connected = true
            }
        }

        composeTestRule.onNodeWithText("Host").performTextInput("192.168.1.1")
        composeTestRule.onNodeWithText("Username").performTextInput("admin")
        composeTestRule.onNodeWithText("Connect").performClick()

        assert(connected)
    }
}
