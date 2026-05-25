package com.terminalarrow.app

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class ProfileListUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testProfileListIsDisplayed() {
        // Wait for biometric or skip if possible in test
        // In a real UI test environment, we would use a mock for biometric
        composeTestRule.onNodeWithText("Terminal Arrow").assertIsDisplayed()
    }
}
