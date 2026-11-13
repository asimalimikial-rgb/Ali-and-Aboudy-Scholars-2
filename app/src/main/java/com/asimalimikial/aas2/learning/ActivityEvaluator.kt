package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ActivityDefinition

/** Result of evaluating one submitted single-choice response. */
data class ActivityEvaluationResult(
    val activityId: String,
    val selectedOptionId: String,
    val isCorrect: Boolean,
    val feedback: String
)

object ActivityEvaluator {
    private val supportedLanguages = setOf("en", "ar")

    fun evaluate(activity: ActivityDefinition, selectedOptionId: String): ActivityEvaluationResult {
        require(selectedOptionId.isNotBlank()) { "A selected option is required" }
        require(activity.activityId.isNotBlank()) { "Activity ID must not be blank" }
        require(activity.skillId.isNotBlank()) { "Skill ID must not be blank" }
        require(activity.activityType == "SINGLE_CHOICE") { "Unsupported activity type" }
        require(activity.language in supportedLanguages) { "Unsupported activity language" }
        require(activity.prompt.isNotBlank()) { "Activity prompt must not be blank" }
        require(activity.difficulty in 1..5) { "Activity difficulty must be between 1 and 5" }
        require(activity.options.size >= 2) { "A single-choice activity needs at least two options" }

        val optionIds = activity.options.map { it.optionId }
        require(optionIds.all(String::isNotBlank)) { "Option IDs must not be blank" }
        require(optionIds.size == optionIds.toSet().size) { "Option IDs must be unique" }
        require(optionIds.count { it == selectedOptionId } == 1) {
            "Selected option does not belong uniquely to this activity"
        }
        require(optionIds.count { it == activity.correctOptionId } == 1) {
            "Activity must define exactly one existing correct option"
        }

        val correct = selectedOptionId == activity.correctOptionId
        val feedback = if (correct) activity.feedbackCorrect else activity.feedbackIncorrect
        require(feedback.isNotBlank()) { "Evaluation feedback must not be blank" }

        return ActivityEvaluationResult(
            activityId = activity.activityId,
            selectedOptionId = selectedOptionId,
            isCorrect = correct,
            feedback = feedback
        )
    }
}
