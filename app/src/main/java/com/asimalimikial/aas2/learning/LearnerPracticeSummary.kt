package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.data.LearningAttemptEntity

data class LearnerPracticeSummary(
    val totalAttempts: Int,
    val scoredAttempts: Int,
    val correctAnswers: Int,
    val accuracyPercent: Int,
    val independentCorrectAnswers: Int,
    val hintedCorrectAnswers: Int
)

/** Calculates a defensive learner-scoped overview for the learner home screen. */
object LearnerPracticeSummaryCalculator {
    fun summarize(
        learnerId: String,
        attempts: List<LearningAttemptEntity>
    ): LearnerPracticeSummary {
        val learnerAttempts = attempts.filter { it.learnerId == learnerId }
        val scoredAttempts = learnerAttempts.filter { it.isCorrect != null }
        val correctAttempts = scoredAttempts.filter { it.isCorrect == true }
        val accuracy = if (scoredAttempts.isEmpty()) {
            0
        } else {
            (correctAttempts.size * 100) / scoredAttempts.size
        }

        return LearnerPracticeSummary(
            totalAttempts = learnerAttempts.size,
            scoredAttempts = scoredAttempts.size,
            correctAnswers = correctAttempts.size,
            accuracyPercent = accuracy,
            independentCorrectAnswers = correctAttempts.count { !it.usedHint },
            hintedCorrectAnswers = correctAttempts.count { it.usedHint }
        )
    }
}
