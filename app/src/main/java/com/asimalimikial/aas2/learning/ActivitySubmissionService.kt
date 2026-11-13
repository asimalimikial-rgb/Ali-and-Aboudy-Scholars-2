package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.ContentPackValidator
import com.asimalimikial.aas2.data.LearnerProfileDao
import com.asimalimikial.aas2.data.LearningAttemptDao
import com.asimalimikial.aas2.data.LearningAttemptEntity
import java.util.Locale
import java.util.UUID

/**
 * Validates the active learner/content-path pairing, evaluates the response,
 * and persists the result under that learner's ID.
 */
class ActivitySubmissionService(
    private val learnerProfileDao: LearnerProfileDao,
    private val learningAttemptDao: LearningAttemptDao,
    private val nowEpochMillis: () -> Long = System::currentTimeMillis,
    private val attemptIdFactory: () -> String = { UUID.randomUUID().toString() }
) {
    suspend fun submit(
        learnerId: String,
        contentPack: ContentPack,
        activityId: String,
        selectedOptionId: String,
        usedHint: Boolean = false
    ): ActivityEvaluationResult {
        require(learnerId.isNotBlank()) { "learnerId must not be blank" }

        val learner = learnerProfileDao.findById(learnerId)
            ?: throw IllegalArgumentException("Unknown learnerId")
        require(learner.isActive) { "Inactive learner profiles cannot submit activities" }

        val expectedPath = canonicalLearnerPath(learner.pathway)
            ?: throw IllegalArgumentException("Unsupported learner pathway")
        require(contentPack.learnerPath == expectedPath) {
            "Content pack does not belong to the selected learner pathway"
        }

        val validation = ContentPackValidator.validate(contentPack)
        require(validation.isValid) {
            "Invalid content pack: ${validation.errors.joinToString("; ")}"
        }

        val activity = contentPack.activities.singleOrNull { it.activityId == activityId }
            ?: throw IllegalArgumentException("Activity is not present exactly once in the content pack")

        val result = ActivityEvaluator.evaluate(activity, selectedOptionId)
        val attemptId = attemptIdFactory()
        require(attemptId.isNotBlank()) { "attemptId must not be blank" }
        val createdAt = nowEpochMillis()
        require(createdAt >= 0L) { "Attempt timestamp must not be negative" }

        learningAttemptDao.insert(
            LearningAttemptEntity(
                attemptId = attemptId,
                learnerId = learnerId,
                activityId = activity.activityId,
                skillId = activity.skillId,
                isCorrect = result.isCorrect,
                usedHint = usedHint,
                createdAtEpochMillis = createdAt
            )
        )
        return result
    }

    private fun canonicalLearnerPath(pathway: String): String? =
        when (pathway.trim().uppercase(Locale.ROOT)) {
            "KG2" -> "KG2"
            "GRADE 2", "GRADE_2", "GRADE2" -> "GRADE_2"
            else -> null
        }
}
