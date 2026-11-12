package com.asimalimikial.aas2.content

import kotlinx.serialization.Serializable

@Serializable
data class ContentPack(
    val packId: String,
    val version: String,
    val schemaVersion: Int,
    val learnerPath: String,
    val skills: List<SkillDefinition>
)

@Serializable
data class SkillDefinition(
    val skillId: String,
    val learnerPath: String,
    val domain: String,
    val observableOutcome: String,
    val sourceReferenceIds: List<String> = emptyList()
)

data class ContentPackValidation(
    val isValid: Boolean,
    val errors: List<String>
)
