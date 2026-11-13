package com.asimalimikial.aas2.content

import kotlinx.serialization.Serializable

@Serializable
data class ContentPack(
    val packId: String,
    val version: String,
    val schemaVersion: Int,
    val learnerPath: String,
    val skills: List<SkillDefinition>,
    val activities: List<ActivityDefinition> = emptyList()
)

@Serializable
data class SkillDefinition(
    val skillId: String,
    val learnerPath: String,
    val domain: String,
    val observableOutcome: String,
    val sourceReferenceIds: List<String> = emptyList()
)

@Serializable
data class ActivityDefinition(
    val activityId: String,
    val skillId: String,
    val activityType: String,
    val language: String,
    val prompt: String,
    val options: List<AnswerOption>,
    val correctOptionId: String,
    val feedbackCorrect: String,
    val feedbackIncorrect: String,
    val difficulty: Int
)

@Serializable
data class AnswerOption(
    val optionId: String,
    val text: String,
    val imageAssetPath: String? = null
)

data class ContentPackValidation(
    val isValid: Boolean,
    val errors: List<String>
)
