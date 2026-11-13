package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.data.LearningAttemptEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class LearnerProgressCalculatorTest {
    private fun attempt(
        id: String,
        learner: String = "ali",
        skill: String = "counting",
        correct: Boolean?,
        time: Long,
        usedHint: Boolean = false
    ) = LearningAttemptEntity(
        attemptId = id,
        learnerId = learner,
        activityId = "activity-$id",
        skillId = skill,
        isCorrect = correct,
        usedHint = usedHint,
        createdAtEpochMillis = time
    )

    @Test
    fun noAttemptsProducesNotStarted() {
        val result = LearnerProgressCalculator.summarize("ali", "counting", emptyList())
        assertEquals(MasteryLevel.NOT_STARTED, result.masteryLevel)
        assertEquals(0, result.accuracyPercent)
    }

    @Test
    fun progressIgnoresOtherLearnersAndSkills() {
        val attempts = listOf(
            attempt("a1", correct = true, time = 1),
            attempt("a2", correct = false, time = 2),
            attempt("b1", learner = "aboudy", correct = true, time = 3),
            attempt("a3", skill = "shapes", correct = true, time = 4)
        )
        val result = LearnerProgressCalculator.summarize("ali", "counting", attempts)
        assertEquals(2, result.scoredAttempts)
        assertEquals(1, result.correctAttempts)
        assertEquals(1, result.incorrectAttempts)
        assertEquals(50, result.accuracyPercent)
        assertEquals(0, result.currentCorrectStreak)
    }

    @Test
    fun unansweredAttemptsDoNotAffectAccuracyOrStreak() {
        val attempts = listOf(
            attempt("a1", correct = true, time = 1),
            attempt("a2", correct = null, time = 2),
            attempt("a3", correct = true, time = 3)
        )
        val result = LearnerProgressCalculator.summarize("ali", "counting", attempts)
        assertEquals(2, result.scoredAttempts)
        assertEquals(100, result.accuracyPercent)
        assertEquals(2, result.currentCorrectStreak)
        assertEquals(2, result.currentIndependentCorrectStreak)
    }

    @Test
    fun masteryRequiresMinimumAttemptsAccuracyAndIndependentRecentStreak() {
        val attempts = listOf(
            attempt("a1", correct = true, time = 1),
            attempt("a2", correct = false, time = 2),
            attempt("a3", correct = true, time = 3),
            attempt("a4", correct = true, time = 4),
            attempt("a5", correct = true, time = 5),
            attempt("a6", correct = true, time = 6)
        )
        val result = LearnerProgressCalculator.summarize("ali", "counting", attempts)
        assertEquals(MasteryLevel.MASTERED, result.masteryLevel)
        assertEquals(4, result.currentCorrectStreak)
        assertEquals(4, result.currentIndependentCorrectStreak)
        assertEquals(5, result.independentCorrectAttempts)
    }

    @Test
    fun hintedCorrectAnswersDoNotQualifyForIndependentMasteryStreak() {
        val attempts = (1..5).map { index ->
            attempt("hint-$index", correct = true, time = index.toLong(), usedHint = true)
        }
        val result = LearnerProgressCalculator.summarize("ali", "counting", attempts)
        assertEquals(100, result.accuracyPercent)
        assertEquals(5, result.hintedCorrectAttempts)
        assertEquals(0, result.independentCorrectAttempts)
        assertEquals(5, result.currentCorrectStreak)
        assertEquals(0, result.currentIndependentCorrectStreak)
        assertEquals(MasteryLevel.DEVELOPING, result.masteryLevel)
    }

    @Test
    fun incorrectAnswerResetsBothRecentStreaks() {
        val attempts = listOf(
            attempt("a1", correct = true, time = 1),
            attempt("a2", correct = true, time = 2),
            attempt("a3", correct = false, time = 3),
            attempt("a4", correct = true, time = 4)
        )
        val result = LearnerProgressCalculator.summarize("ali", "counting", attempts)
        assertEquals(1, result.currentCorrectStreak)
        assertEquals(1, result.currentIndependentCorrectStreak)
    }

    @Test
    fun lowAccuracyDoesNotReachDeveloping() {
        val attempts = listOf(
            attempt("a1", correct = false, time = 1),
            attempt("a2", correct = false, time = 2),
            attempt("a3", correct = false, time = 3)
        )
        val result = LearnerProgressCalculator.summarize("ali", "counting", attempts)
        assertEquals(0, result.accuracyPercent)
        assertEquals(MasteryLevel.PRACTICING, result.masteryLevel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun blankLearnerIdIsRejected() {
        LearnerProgressCalculator.summarize(" ", "counting", emptyList())
    }
}
