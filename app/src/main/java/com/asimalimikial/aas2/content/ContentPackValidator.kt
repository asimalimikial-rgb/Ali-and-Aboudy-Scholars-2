package com.asimalimikial.aas2.content

object ContentPackValidator {
    private const val CURRENT_SCHEMA_VERSION = 1
    private val supportedLearnerPaths = setOf("KG2", "GRADE_2")

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
            }
        }
        return ContentPackValidation(isValid = errors.isEmpty(), errors = errors)
    }
}
