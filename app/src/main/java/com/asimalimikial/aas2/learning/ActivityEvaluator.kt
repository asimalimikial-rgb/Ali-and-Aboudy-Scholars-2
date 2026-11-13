package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ActivityDefinition

/**
 * Result of evaluating one submitted single-choice response.
 * Invalid option IDs are rejected instead of being treated as incorrect answers.
 */
data class ActivityEvaluationResult(
    val activityId: String,
    val selectedOptionId: String,
    val isCorrect: Boolean,
    val feedback: String
)

object ActivityEvaluator {
    fun evaluate(
        activity: ActivityDefinition,
        selectedOptionId: String
    ): ActivityEvaluationResult {
        require(selectedOptionId.isNotBlank()) { "A selected option is required" }
        require(activity.activityId.isNotBlank()) { "Activity ID must not be blank" }
        require(activity.options.any { it.optionId == selectedOptionId }) {
            "Selected option does not belong to this activity"
        }
        require(activity.options.count { it.optionId == activity.correctOptionId } == 1) {
            "Activity must define exactly one existing correct option"
        }

        val correct = selectedOptionId == activity.correctOptionId
        return ActivityEvaluationResult(
            activityId = activity.activityId,
            selectedOptionId = selectedOptionId,
            isCorrect = correct,
            feedback = if (correct) activity.feedbackCorrect else activity.feedbackIncorrect
        )
    }
}
