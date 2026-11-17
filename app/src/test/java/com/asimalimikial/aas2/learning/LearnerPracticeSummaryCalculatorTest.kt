package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.data.LearningAttemptEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class LearnerPracticeSummaryCalculatorTest {
    @Test
    fun emptyHistoryReturnsZeroSummary() {
        val summary = LearnerPracticeSummaryCalculator.summarize("ali", emptyList())

        assertEquals(0, summary.totalAttempts)
        assertEquals(0, summary.scoredAttempts)
        assertEquals(0, summary.correctAnswers)
        assertEquals(0, summary.accuracyPercent)
        assertEquals(0, summary.independentCorrectAnswers)
        assertEquals(0, summary.hintedCorrectAnswers)
    }

    @Test
    fun summaryIncludesOnlyTheRequestedLearner() {
        val attempts = listOf(
            attempt("ali-1", "ali", true, false, 1L),
            attempt("ali-2", "ali", false, false, 2L),
            attempt("aboudy-1", "aboudy", true, false, 3L)
        )

        val summary = LearnerPracticeSummaryCalculator.summarize("ali", attempts)

        assertEquals(2, summary.totalAttempts)
        assertEquals(2, summary.scoredAttempts)
        assertEquals(1, summary.correctAnswers)
        assertEquals(50, summary.accuracyPercent)
    }

    @Test
    fun accuracyUsesScoredAttemptsAndSeparatesHintedCorrectAnswers() {
        val attempts = listOf(
            attempt("a1", "ali", true, false, 1L),
            attempt("a2", "ali", true, true, 2L),
            attempt("a3", "ali", false, false, 3L),
            attempt("a4", "ali", null, false, 4L)
        )

        val summary = LearnerPracticeSummaryCalculator.summarize("ali", attempts)

        assertEquals(4, summary.totalAttempts)
        assertEquals(3, summary.scoredAttempts)
        assertEquals(2, summary.correctAnswers)
        assertEquals(66, summary.accuracyPercent)
        assertEquals(1, summary.independentCorrectAnswers)
        assertEquals(1, summary.hintedCorrectAnswers)
    }

    private fun attempt(
        id: String,
        learnerId: String,
        isCorrect: Boolean?,
        usedHint: Boolean,
        timestamp: Long
    ) = LearningAttemptEntity(
        attemptId = id,
        learnerId = learnerId,
        activityId = "activity-$id",
        skillId = "skill",
        isCorrect = isCorrect,
        usedHint = usedHint,
        createdAtEpochMillis = timestamp
    )
}
