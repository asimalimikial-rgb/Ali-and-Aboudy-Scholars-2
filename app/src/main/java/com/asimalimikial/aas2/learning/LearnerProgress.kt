package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.data.LearningAttemptEntity

enum class MasteryLevel {
    NOT_STARTED,
    PRACTICING,
    DEVELOPING,
    MASTERED
}

data class SkillProgressSnapshot(
    val learnerId: String,
    val skillId: String,
    val scoredAttempts: Int,
    val correctAttempts: Int,
    val incorrectAttempts: Int,
    val hintedCorrectAttempts: Int,
    val independentCorrectAttempts: Int,
    val accuracyPercent: Int,
    val currentCorrectStreak: Int,
    val currentIndependentCorrectStreak: Int,
    val masteryLevel: MasteryLevel
)

object LearnerProgressCalculator {
    /**
     * Progress is always scoped to both learner and skill. The input may contain
     * records from the whole database; records for other learners are ignored.
     */
    fun summarize(
        learnerId: String,
        skillId: String,
        attempts: List<LearningAttemptEntity>
    ): SkillProgressSnapshot {
        require(learnerId.isNotBlank()) { "learnerId must not be blank" }
        require(skillId.isNotBlank()) { "skillId must not be blank" }

        val scored = attempts
            .asSequence()
            .filter { it.learnerId == learnerId && it.skillId == skillId }
            .filter { it.isCorrect != null }
            .sortedWith(compareBy<LearningAttemptEntity> { it.createdAtEpochMillis }.thenBy { it.attemptId })
            .toList()

        val correct = scored.count { it.isCorrect == true }
        val incorrect = scored.size - correct
        val hintedCorrect = scored.count { it.isCorrect == true && it.usedHint }
        val independentCorrect = scored.count { it.isCorrect == true && !it.usedHint }
        val accuracy = if (scored.isEmpty()) 0 else (correct * 100) / scored.size
        val currentStreak = scored.asReversed().takeWhile { it.isCorrect == true }.size
        val independentStreak = scored.asReversed()
            .takeWhile { it.isCorrect == true && !it.usedHint }
            .size

        // Provisional deterministic bands; educational calibration remains pending.
        val mastery = when {
            scored.isEmpty() -> MasteryLevel.NOT_STARTED
            scored.size >= 5 && accuracy >= 80 && independentStreak >= 3 -> MasteryLevel.MASTERED
            scored.size >= 3 && accuracy >= 60 -> MasteryLevel.DEVELOPING
            else -> MasteryLevel.PRACTICING
        }

        return SkillProgressSnapshot(
            learnerId = learnerId,
            skillId = skillId,
            scoredAttempts = scored.size,
            correctAttempts = correct,
            incorrectAttempts = incorrect,
            hintedCorrectAttempts = hintedCorrect,
            independentCorrectAttempts = independentCorrect,
            accuracyPercent = accuracy,
            currentCorrectStreak = currentStreak,
            currentIndependentCorrectStreak = independentStreak,
            masteryLevel = mastery
        )
    }
}
