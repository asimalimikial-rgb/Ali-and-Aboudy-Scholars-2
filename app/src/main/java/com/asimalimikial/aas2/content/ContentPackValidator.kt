package com.asimalimikial.aas2.content

object ContentPackValidator {
    private const val CURRENT_SCHEMA_VERSION = 1
    private val supportedLearnerPaths = setOf("KG2", "GRADE_2")
    private val supportedActivityTypes = setOf("SINGLE_CHOICE")
    private val supportedLanguages = setOf("en", "ar")

    fun validate(pack: ContentPack): ContentPackValidation {
        val errors = buildList {
            if (pack.packId.isBlank()) add("packId must not be blank")
            if (pack.version.isBlank()) add("version must not be blank")
            if (pack.schemaVersion != CURRENT_SCHEMA_VERSION) {
                add("Unsupported schemaVersion: ${pack.schemaVersion}")
            }
            if (pack.learnerPath !in supportedLearnerPaths) {
                add("Unsupported learnerPath: ${pack.learnerPath}")
            }
            if (pack.skills.isEmpty()) add("A content pack must contain at least one skill")

            val skillIds = pack.skills.map { it.skillId }
            if (skillIds.any(String::isBlank)) add("skillId must not be blank")
            if (skillIds.size != skillIds.toSet().size) add("skillId values must be unique")

            pack.skills.forEachIndexed { index, skill ->
                if (skill.learnerPath != pack.learnerPath) {
                    add("Skill at index $index belongs to a different learner path")
                }
                if (skill.domain.isBlank()) add("Skill at index $index has an empty domain")
                if (skill.observableOutcome.isBlank()) {
                    add("Skill at index $index has an empty observable outcome")
                }
                if (skill.sourceReferenceIds.any(String::isBlank)) {
                    add("Skill at index $index contains a blank source reference ID")
                }
            }

            val activityIds = pack.activities.map { it.activityId }
            if (activityIds.any(String::isBlank)) add("activityId must not be blank")
            if (activityIds.size != activityIds.toSet().size) {
                add("activityId values must be unique")
            }

            val knownSkillIds = skillIds.toSet()
            pack.activities.forEachIndexed { index, activity ->
                if (activity.skillId !in knownSkillIds) {
                    add("Activity at index $index references an unknown skillId")
                }
                if (activity.activityType !in supportedActivityTypes) {
                    add("Activity at index $index has an unsupported activityType")
                }
                if (activity.language !in supportedLanguages) {
                    add("Activity at index $index has an unsupported language")
                }
                if (activity.prompt.isBlank()) add("Activity at index $index has an empty prompt")
                if (activity.feedbackCorrect.isBlank()) {
                    add("Activity at index $index has empty correct feedback")
                }
                if (activity.feedbackIncorrect.isBlank()) {
                    add("Activity at index $index has empty incorrect feedback")
                }
                if (activity.difficulty !in 1..5) {
                    add("Activity at index $index difficulty must be between 1 and 5")
                }

                val optionIds = activity.options.map { it.optionId }
                if (activity.options.size < 2) {
                    add("Activity at index $index must contain at least two options")
                }
                if (optionIds.any(String::isBlank)) {
                    add("Activity at index $index contains a blank optionId")
                }
                if (optionIds.size != optionIds.toSet().size) {
                    add("Activity at index $index optionId values must be unique")
                }
                if (activity.options.any { it.text.isBlank() && it.imageAssetPath.isNullOrBlank() }) {
                    add("Activity at index $index contains an option without text or image")
                }
                if (optionIds.count { it == activity.correctOptionId } != 1) {
                    add("Activity at index $index must identify exactly one correct option")
                }
            }
        }
        return ContentPackValidation(isValid = errors.isEmpty(), errors = errors)
    }
}
