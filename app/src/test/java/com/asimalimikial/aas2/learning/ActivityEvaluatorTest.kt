package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.content.AnswerOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivityEvaluatorTest {
    private fun activity() = ActivityDefinition(
        activityId = "count-1",
        skillId = "counting",
        activityType = "SINGLE_CHOICE",
        language = "en",
        prompt = "How many?",
        options = listOf(
            AnswerOption("one", "1"),
            AnswerOption("two", "2")
        ),
        correctOptionId = "two",
        feedbackCorrect = "Well done",
        feedbackIncorrect = "Try counting again",
        difficulty = 1
    )

    @Test
    fun correctAnswerReturnsCorrectFeedback() {
        val result = ActivityEvaluator.evaluate(activity(), "two")
        assertTrue(result.isCorrect)
        assertEquals("Well done", result.feedback)
    }

    @Test
    fun incorrectAnswerReturnsIncorrectFeedback() {
        val result = ActivityEvaluator.evaluate(activity(), "one")
        assertFalse(result.isCorrect)
        assertEquals("Try counting again", result.feedback)
    }

    @Test(expected = IllegalArgumentException::class)
    fun unknownOptionCannotBeEvaluatedAsAnAnswer() {
        ActivityEvaluator.evaluate(activity(), "not-in-this-activity")
    }

    @Test(expected = IllegalArgumentException::class)
    fun blankOptionCannotBeSubmitted() {
        ActivityEvaluator.evaluate(activity(), " ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun unsupportedActivityTypeIsRejected() {
        ActivityEvaluator.evaluate(activity().copy(activityType = "FREE_TEXT"), "two")
    }

    @Test(expected = IllegalArgumentException::class)
    fun duplicateOptionIdsAreRejectedBeforeScoring() {
        val invalid = activity().copy(
            options = listOf(AnswerOption("same", "1"), AnswerOption("same", "2")),
            correctOptionId = "same"
        )
        ActivityEvaluator.evaluate(invalid, "same")
    }

    @Test(expected = IllegalArgumentException::class)
    fun blankFeedbackCannotBeReturned() {
        ActivityEvaluator.evaluate(activity().copy(feedbackCorrect = " "), "two")
    }
}
