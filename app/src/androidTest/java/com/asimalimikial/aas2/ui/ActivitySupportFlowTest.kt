package com.asimalimikial.aas2.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.content.AnswerOption
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class ActivitySupportFlowTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun revealsEnglishHintsAndHelpOneStepAtATimeAndRecordsSupportUse() {
        val activity = ActivityDefinition(
            activityId = "support-flow-test",
            skillId = "counting",
            activityType = "SINGLE_CHOICE",
            language = "en",
            prompt = "Which number comes next?",
            options = listOf(
                AnswerOption(optionId = "a", text = "4"),
                AnswerOption(optionId = "b", text = "5")
            ),
            correctOptionId = "b",
            feedbackCorrect = "Correct!",
            feedbackIncorrect = "Try again.",
            difficulty = 1,
            hintSequence = listOf(
                "Look at the numbers in order.",
                "Count one more than the last number."
            ),
            helpSteps = listOf(
                "Start with the first number.",
                "Say the next number as you count forward once."
            )
        )
        val supportUses = AtomicInteger(0)

        composeRule.setContent {
            ActivityRenderer(
                activity = activity,
                evaluation = null,
                onSubmitAnswer = {},
                onRetry = {},
                onSupportUsed = { supportUses.incrementAndGet() }
            )
        }

        composeRule.onNodeWithText("Need a hint?").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Hint 1").assertIsDisplayed()
        composeRule.onNodeWithText("Look at the numbers in order.").assertIsDisplayed()

        composeRule.onNodeWithText("Show next hint").performClick()
        composeRule.onNodeWithText("Hint 2").assertIsDisplayed()
        composeRule.onNodeWithText("Count one more than the last number.").assertIsDisplayed()

        composeRule.onNodeWithText("Show me how").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Work it out step by step").assertIsDisplayed()
        composeRule.onNodeWithText("Step 1").assertIsDisplayed()
        composeRule.onNodeWithText("Start with the first number.").assertIsDisplayed()

        composeRule.onNodeWithText("Show next step").performClick()
        composeRule.onNodeWithText("Step 2").assertIsDisplayed()
        composeRule.onNodeWithText("Say the next number as you count forward once.").assertIsDisplayed()

        assertEquals(4, supportUses.get())
    }
}
