package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.data.LearningAttemptEntity

/**
 * A question needs review when the learner's latest scored response for it was incorrect.
 * This is derived from the existing attempt history; no Room schema migration is required.
 */
object ReviewQueueCalculator {
    fun activitiesNeedingReview(
        learnerId: String,
        contentPackId: String,
        activities: List<ActivityDefinition>,
        attempts: List<LearningAttemptEntity>
    ): List<ActivityDefinition> {
        val latestAttemptByActivity = attempts.asSequence()
            .filter {
                it.learnerId == learnerId &&
                    it.contentPackId == contentPackId &&
                    it.isCorrect != null
            }
            .groupBy { it.activityId }
            .mapValues { (_, rows) ->
                rows.maxWithOrNull(
                    compareBy<LearningAttemptEntity> { it.createdAtEpochMillis }
                        .thenBy { it.attemptId }
                )
            }

        val reviewIds = latestAttemptByActivity
            .filterValues { it?.isCorrect == false }
            .keys

        return activities.filter { it.activityId in reviewIds }
    }
}
