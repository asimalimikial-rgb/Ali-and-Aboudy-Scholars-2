package com.asimalimikial.aas2.ui

import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asimalimikial.aas2.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersonalBookShelfFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun bookshelfShowsOnlyTheSelectedLearnersOfflineBookCovers() {
        waitForText("Ali · KG2")
        composeRule.onNodeWithText("Ali · KG2").performClick()
        waitForText("Start learning")
        composeRule.onNodeWithText("Start learning").performClick()

        waitForText("Ali's Books")
        composeRule.onNodeWithText("Ali's Books").assertIsDisplayed()
        composeRule.onNodeWithText("Aboudy's Books").assertDoesNotExist()
        waitForContentDescription("Hop, Skip and Jump — Senior KG Complete Kit")
        composeRule.onNodeWithContentDescription("Hop, Skip and Jump — Senior KG Complete Kit").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Maths Xpress — Senior KG").assertIsDisplayed()

        composeRule.onNodeWithText("Back to learning space").performClick()
        waitForText("Switch learner")
        composeRule.onNodeWithText("Switch learner").performClick()

        waitForText("Aboudy · Grade 2")
        composeRule.onNodeWithText("Aboudy · Grade 2").performClick()
        waitForText("Start learning")
        composeRule.onNodeWithText("Start learning").performClick()

        waitForText("Aboudy's Books")
        composeRule.onNodeWithText("Aboudy's Books").assertIsDisplayed()
        composeRule.onNodeWithText("Ali's Books").assertDoesNotExist()
        waitForContentDescription("English World — Level 2 Pupil's Book Pack")
        composeRule.onNodeWithContentDescription("English World — Level 2 Pupil's Book Pack").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Macmillan Mathematics — Level 2A").assertIsDisplayed()
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 20_000) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun waitForContentDescription(description: String) {
        composeRule.waitUntil(timeoutMillis = 20_000) {
            composeRule.onAllNodesWithContentDescription(description).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
