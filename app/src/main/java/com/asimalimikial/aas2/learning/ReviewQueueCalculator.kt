package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.data.LearningAttemptEntity

/**
 * A question needs review after two consecutive incorrect scored responses.
 * A later correct response clears it. Status is derived from local attempt history,
 * so no Room schema migration or additional child data is required.
 */
object ReviewQueueCalculator {
    fun activitiesNeedingReview(
        learnerId: String,
        contentPackId: String,
        activities: List<ActivityDefinition>,
        attempts: List<LearningAttemptEntity>
    ): List<ActivityDefinition> {
        val reviewIds = attempts.asSequence()
            .filter {
                it.learnerId == learnerId &&
                    it.contentPackId == contentPackId &&
                    it.isCorrect != null
            }
            .groupBy { it.activityId }
            .filterValues { rows ->
                val lastTwo = rows.sortedWith(
                    compareBy<LearningAttemptEntity> { it.createdAtEpochMillis }
                        .thenBy { it.attemptId }
                ).takeLast(2)
                lastTwo.size == 2 && lastTwo.all { it.isCorrect == false }
            }
            .keys

        return activities.filter { it.activityId in reviewIds }
    }
}
