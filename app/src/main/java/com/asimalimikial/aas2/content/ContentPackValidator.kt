package com.asimalimikial.aas2.content

import java.net.URI

object ContentPackValidator {
    // Version 3 adds a source catalog with explicit provenance and reuse metadata.
    private const val CURRENT_SCHEMA_VERSION = 3
    private val supportedLearnerPaths = setOf("KG2", "GRADE_2")
    private val supportedActivityTypes = setOf("SINGLE_CHOICE")
    private val supportedLanguages = setOf("en", "ar")
    private val supportedUsageRights = setOf(
        "REFERENCE_ONLY",
        "REDISTRIBUTION_ALLOWED",
        "ADAPTATION_ALLOWED",
        "PERMISSION_REQUIRED",
        "PROHIBITED"
    )

    fun validate(pack: ContentPack): ContentPackValidation {
        val errors = buildList {
            if (pack.packId.isBlank()) add("packId must not be blank")
            if (pack.version.isBlank()) add("version must not be blank")
            if (pack.schemaVersion != CURRENT_SCHEMA_VERSION) add("Unsupported schemaVersion: ${pack.schemaVersion}")
            if (pack.learnerPath !in supportedLearnerPaths) add("Unsupported learnerPath: ${pack.learnerPath}")
            if (pack.skills.isEmpty()) add("A content pack must contain at least one skill")

            val sourceIds = pack.sourceReferences.map { it.sourceReferenceId }
            if (sourceIds.any(String::isBlank)) add("sourceReferenceId must not be blank")
            if (sourceIds.size != sourceIds.toSet().size) add("sourceReferenceId values must be unique")

            pack.sourceReferences.forEachIndexed { index, source ->
                if (source.title.isBlank()) add("Source at index $index has an empty title")
                if (source.publisher.isBlank()) add("Source at index $index has an empty publisher")
                if (!isValidHttpsUrl(source.url)) {
                    add("Source at index $index must have a valid HTTPS URL")
                }
                if (source.license.isBlank()) add("Source at index $index has no license or rights statement")
                if (source.licenseUrl != null && !isValidHttpsUrl(source.licenseUrl)) {
                    add("Source at index $index has an invalid HTTPS license URL")
                }
                if (source.usageRights !in supportedUsageRights) {
                    add("Source at index $index has unsupported usageRights")
                }
                if (source.usageRights in setOf("REDISTRIBUTION_ALLOWED", "ADAPTATION_ALLOWED") &&
                    source.attribution.isNullOrBlank()
                ) {
                    add("Source at index $index requires attribution metadata")
                }
                if (source.verifiedAt != null && !isValidIsoDate(source.verifiedAt)) {
                    add("Source at index $index verifiedAt must be a valid YYYY-MM-DD date")
                }
            }

            val skillIds = pack.skills.map { it.skillId }
            if (skillIds.any(String::isBlank)) add("skillId must not be blank")
            if (skillIds.size != skillIds.toSet().size) add("skillId values must be unique")

            pack.skills.forEachIndexed { index, skill ->
                if (skill.learnerPath != pack.learnerPath) add("Skill at index $index belongs to a different learner path")
                if (skill.domain.isBlank()) add("Skill at index $index has an empty domain")
                if (skill.observableOutcome.isBlank()) add("Skill at index $index has an empty observable outcome")
                if (skill.sourceReferenceIds.any(String::isBlank)) add("Skill at index $index contains a blank source reference ID")
                if (skill.sourceReferenceIds.size != skill.sourceReferenceIds.toSet().size) {
                    add("Skill at index $index contains duplicate source reference IDs")
                }
                if (skill.sourceReferenceIds.any { it !in sourceIds }) {
                    add("Skill at index $index references an unknown sourceReferenceId")
                }
            }

            val activityIds = pack.activities.map { it.activityId }
            if (activityIds.any(String::isBlank)) add("activityId must not be blank")
            if (activityIds.size != activityIds.toSet().size) add("activityId values must be unique")

            val knownSkillIds = skillIds.toSet()
            pack.activities.forEachIndexed { index, activity ->
                if (activity.skillId !in knownSkillIds) add("Activity at index $index references an unknown skillId")
                if (activity.activityType !in supportedActivityTypes) add("Activity at index $index has an unsupported activityType")
                if (activity.language !in supportedLanguages) add("Activity at index $index has an unsupported language")
                if (activity.prompt.isBlank()) add("Activity at index $index has an empty prompt")
                if (activity.feedbackCorrect.isBlank()) add("Activity at index $index has empty correct feedback")
                if (activity.feedbackIncorrect.isBlank()) add("Activity at index $index has empty incorrect feedback")
                if (activity.difficulty !in 1..5) add("Activity at index $index difficulty must be between 1 and 5")

                val optionIds = activity.options.map { it.optionId }
                if (activity.options.size < 2) add("Activity at index $index must contain at least two options")
                if (optionIds.any(String::isBlank)) add("Activity at index $index contains a blank optionId")
                if (optionIds.size != optionIds.toSet().size) add("Activity at index $index optionId values must be unique")
                if (activity.options.any { it.text.isBlank() && it.imageAssetPath.isNullOrBlank() }) {
                    add("Activity at index $index contains an option without text or image")
                }
                if (activity.options.any { option ->
                        val path = option.imageAssetPath
                        path != null && (
                            path.isBlank() ||
                                path.startsWith("/") ||
                                path.contains('\\') ||
                                path.contains("://") ||
                                path.split("/").any { it == ".." }
                            )
                    }
                ) {
                    add("Activity at index $index contains an unsafe image asset path")
                }
                if (optionIds.count { it == activity.correctOptionId } != 1) {
                    add("Activity at index $index must identify exactly one correct option")
                }
            }
        }
        return ContentPackValidation(isValid = errors.isEmpty(), errors = errors)
    }
    private fun isValidHttpsUrl(value: String): Boolean {
        val uri = runCatching { URI(value) }.getOrNull() ?: return false
        return uri.scheme.equals("https", ignoreCase = true) &&
            !uri.host.isNullOrBlank() &&
            uri.rawUserInfo == null
    }

    private fun isValidIsoDate(value: String): Boolean {
        if (!Regex("""^\\d{4}-\\d{2}-\\d{2}$""").matches(value)) return false

        val year = value.substring(0, 4).toInt()
        val month = value.substring(5, 7).toInt()
        val day = value.substring(8, 10).toInt()
        if (year !in 1..9999 || month !in 1..12) return false

        val daysInMonth = when (month) {
            2 -> if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        return day in 1..daysInMonth
    }

}
