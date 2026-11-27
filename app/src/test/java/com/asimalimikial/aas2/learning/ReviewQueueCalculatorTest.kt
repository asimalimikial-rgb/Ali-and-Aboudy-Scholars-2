package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.content.AnswerOption
import com.asimalimikial.aas2.data.LearningAttemptEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class ReviewQueueCalculatorTest {
    private fun activity(id: String) = ActivityDefinition(
        activityId = id,
        skillId = "counting",
        activityType = "SINGLE_CHOICE",
        language = "en",
        prompt = "Question $id",
        options = listOf(AnswerOption("wrong", "1"), AnswerOption("right", "2")),
        correctOptionId = "right",
        feedbackCorrect = "Well done",
        feedbackIncorrect = "Try again",
        difficulty = 1
    )

    private fun attempt(
        id: String,
        activityId: String,
        correct: Boolean,
        time: Long,
        learnerId: String = "ali",
        packId: String = "kg2-pack"
    ) = LearningAttemptEntity(
        attemptId = id,
        learnerId = learnerId,
        activityId = activityId,
        skillId = "counting",
        isCorrect = correct,
        usedHint = false,
        createdAtEpochMillis = time,
        contentPackId = packId
    )

    @Test
    fun questionIsQueuedWhenLatestAnswerIsIncorrect() {
        val result = ReviewQueueCalculator.activitiesNeedingReview(
            learnerId = "ali",
            contentPackId = "kg2-pack",
            activities = listOf(activity("q1"), activity("q2")),
            attempts = listOf(
                attempt("q1-first", "q1", false, 100),
                attempt("q1-second", "q1", false, 200),
                attempt("q2-first", "q2", false, 100),
                attempt("q2-second", "q2", true, 200)
            )
        )

        assertEquals(listOf("q1"), result.map { it.activityId })
    }

    @Test
    fun attemptsFromAnotherLearnerOrPackDoNotEnterReviewQueue() {
        val result = ReviewQueueCalculator.activitiesNeedingReview(
            learnerId = "ali",
            contentPackId = "kg2-pack",
            activities = listOf(activity("q1")),
            attempts = listOf(
                attempt("aboudy", "q1", false, 300, learnerId = "aboudy"),
                attempt("other-pack", "q1", false, 400, packId = "other-pack")
            )
        )

        assertEquals(emptyList<ActivityDefinition>(), result)
    }
}
