package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.ContentPackSelectionPolicy
import com.asimalimikial.aas2.content.ContentPackValidator
import com.asimalimikial.aas2.data.LearnerProfileDao
import com.asimalimikial.aas2.data.LearningAttemptDao
import com.asimalimikial.aas2.data.LearningAttemptEntity
import java.util.UUID

/**
 * Persists adult-observed guided-task completion as an unscored learning event.
 * It deliberately writes isCorrect = null so these tasks never affect accuracy,
 * retries, or the scored-question review queue.
 */
class GuidedTaskCompletionService(
    private val learnerProfileDao: LearnerProfileDao,
    private val learningAttemptDao: LearningAttemptDao,
    private val nowEpochMillis: () -> Long = System::currentTimeMillis,
    private val attemptIdFactory: () -> String = { UUID.randomUUID().toString() }
) {
    suspend fun complete(
        learnerId: String,
        contentPack: ContentPack,
        guidedTaskId: String,
        observationNote: String? = null
    ) {
        require(learnerId.isNotBlank()) { "learnerId must not be blank" }
        val learner = learnerProfileDao.findById(learnerId)
            ?: throw IllegalArgumentException("Unknown learnerId")
        require(learner.isActive) { "Inactive learner profiles cannot complete guided tasks" }

        val expectedPath = ContentPackSelectionPolicy.canonicalLearnerPath(learner.pathway)
            ?: throw IllegalArgumentException("Unsupported learner pathway")
        require(contentPack.learnerPath == expectedPath) {
            "Content pack does not belong to the selected learner pathway"
        }

        val validation = ContentPackValidator.validate(contentPack)
        require(validation.isValid) {
            "Invalid content pack: ${validation.errors.joinToString("; ")}"
        }

        val task = contentPack.guidedTasks.singleOrNull { it.activityId == guidedTaskId }
            ?: throw IllegalArgumentException("Guided task is not present exactly once in the content pack")
        val note = observationNote?.trim()?.takeIf { it.isNotEmpty() }
        require(note == null || note.length <= MAX_OBSERVATION_LENGTH) {
            "Adult observation must be $MAX_OBSERVATION_LENGTH characters or fewer"
        }

        val attemptId = attemptIdFactory()
        require(attemptId.isNotBlank()) { "attemptId must not be blank" }
        val createdAt = nowEpochMillis()
        require(createdAt >= 0L) { "Guided task timestamp must not be negative" }

        learningAttemptDao.insert(
            LearningAttemptEntity(
                attemptId = attemptId,
                learnerId = learnerId,
                activityId = task.activityId,
                skillId = task.skillId,
                isCorrect = null,
                usedHint = false,
                createdAtEpochMillis = createdAt,
                contentPackId = contentPack.packId,
                contentPackVersion = contentPack.version,
                selectedOptionId = null,
                feedbackText = note
            )
        )
    }

    private companion object {
        const val MAX_OBSERVATION_LENGTH = 500
    }
}
