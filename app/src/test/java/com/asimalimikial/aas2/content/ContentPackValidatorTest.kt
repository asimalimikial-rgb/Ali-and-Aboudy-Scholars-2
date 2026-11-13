package com.asimalimikial.aas2.content

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentPackValidatorTest {
    @Test
    fun validPackIsAccepted() {
        val pack = ContentPack(
            packId = "ali-numeracy-foundation",
            version = "0.1.0",
            schemaVersion = 1,
            learnerPath = "KG2",
            skills = listOf(
                SkillDefinition(
                    skillId = "ali.math.counting.001",
                    learnerPath = "KG2",
                    domain = "Numeracy",
                    observableOutcome = "Counts a small set of objects accurately"
                )
            )
        )

        assertTrue(ContentPackValidator.validate(pack).isValid)
    }

    @Test
    fun duplicateSkillIdsAreRejected() {
        val skill = SkillDefinition(
            skillId = "shared.id",
            learnerPath = "KG2",
            domain = "Numeracy",
            observableOutcome = "Counts objects"
        )
        val pack = ContentPack(
            packId = "invalid-pack",
            version = "0.1.0",
            schemaVersion = 1,
            learnerPath = "KG2",
            skills = listOf(skill, skill)
        )

        assertFalse(ContentPackValidator.validate(pack).isValid)
    }

    @Test
    fun mixedLearnerPathsAreRejected() {
        val pack = ContentPack(
            packId = "mixed-pack",
            version = "0.1.0",
            schemaVersion = 1,
            learnerPath = "KG2",
            skills = listOf(
                SkillDefinition(
                    skillId = "aboudy.math.001",
                    learnerPath = "GRADE_2",
                    domain = "Mathematics",
                    observableOutcome = "Uses place value"
                )
            )
        )

        assertFalse(ContentPackValidator.validate(pack).isValid)
    }

    @Test
    fun emptySkillListIsRejected() {
        val pack = ContentPack(
            packId = "empty-pack",
            version = "0.1.0",
            schemaVersion = 1,
            learnerPath = "KG2",
            skills = emptyList()
        )

        assertFalse(ContentPackValidator.validate(pack).isValid)
    }

    @Test
    fun unsupportedSchemaAndLearnerPathAreRejected() {
        val pack = ContentPack(
            packId = "future-pack",
            version = "0.1.0",
            schemaVersion = 99,
            learnerPath = "UNKNOWN",
            skills = listOf(
                SkillDefinition(
                    skillId = "skill.001",
                    learnerPath = "UNKNOWN",
                    domain = "Science",
                    observableOutcome = "Observes a simple phenomenon"
                )
            )
        )

        val result = ContentPackValidator.validate(pack)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.startsWith("Unsupported schemaVersion") })
        assertTrue(result.errors.any { it.startsWith("Unsupported learnerPath") })
    }

    @Test
    fun blankRequiredSkillFieldsAreRejected() {
        val pack = ContentPack(
            packId = "invalid-skill-pack",
            version = "0.1.0",
            schemaVersion = 1,
            learnerPath = "KG2",
            skills = listOf(
                SkillDefinition(
                    skillId = " ",
                    learnerPath = "KG2",
                    domain = " ",
                    observableOutcome = ""
                )
            )
        )

        val result = ContentPackValidator.validate(pack)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("skillId must not be blank") })
        assertTrue(result.errors.any { it.contains("empty domain") })
        assertTrue(result.errors.any { it.contains("empty observable outcome") })
    }
}
