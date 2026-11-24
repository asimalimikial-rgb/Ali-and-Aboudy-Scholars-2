package com.asimalimikial.aas2.content

import kotlinx.serialization.Serializable

@Serializable
data class ContentPack(
    val packId: String,
    val version: String,
    val schemaVersion: Int,
    val learnerPath: String,
    val skills: List<SkillDefinition>,
    val activities: List<ActivityDefinition> = emptyList(),
    val sourceReferences: List<ContentSourceReference> = emptyList(),
    val books: List<BookDefinition> = emptyList(),
    val units: List<UnitDefinition> = emptyList(),
    val lessons: List<LessonDefinition> = emptyList()
)

@Serializable
data class BookDefinition(
    val bookId: String,
    val title: String,
    val subject: String,
    val learnerPath: String,
    val sourceReferenceIds: List<String>
)

@Serializable
data class UnitDefinition(
    val unitId: String,
    val bookId: String,
    val title: String,
    val sequence: Int,
    val lessonIds: List<String>
)

@Serializable
data class LessonDefinition(
    val lessonId: String,
    val unitId: String,
    val title: String,
    val sequence: Int,
    val skillIds: List<String>
)

@Serializable
data class SkillDefinition(
    val skillId: String,
    val learnerPath: String,
    val domain: String,
    val observableOutcome: String,
    val sourceReferenceIds: List<String> = emptyList()
)

/**
 * Provenance and reuse metadata for curriculum, content, and asset sources.
 * REFERENCE_ONLY is the safe default until redistribution rights are verified.
 */
@Serializable
data class ContentSourceReference(
    val sourceReferenceId: String,
    val title: String,
    val publisher: String,
    val url: String,
    val license: String,
    val licenseUrl: String? = null,
    val attribution: String? = null,
    val usageRights: String = "REFERENCE_ONLY",
    val verifiedAt: String? = null
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
    val difficulty: Int,
    val bookId: String? = null,
    val unitId: String? = null,
    val lessonId: String? = null,
    val assessmentPhase: String? = null,
    val misconceptionCode: String? = null,
    val optionRationales: Map<String, String> = emptyMap(),
    val promptImageAssetPath: String? = null,
    val promptAudioAssetPath: String? = null,
    val optionAudioAssetPaths: Map<String, String> = emptyMap(),
    val feedbackCorrectAudioAssetPath: String? = null,
    val feedbackIncorrectAudioAssetPath: String? = null,
    /** Progressive English-only support; empty defaults preserve older bundled packs. */
    val hintSequence: List<String> = emptyList(),
    /** Short, sequential English teaching steps; empty for activities without authored help. */
    val helpSteps: List<String> = emptyList(),
    /** Optional visual aids must be bundled locally and match the English support text. */
    // Keep these fields optional so legacy schema 3/4 activities deserialize without migration.
    val hintImageAssetPath: String? = null,
    val helpImageAssetPath: String? = null
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
