package com.asimalimikial.aas2.content

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentPackValidatorTest {
    private fun baseSkill() = SkillDefinition(
        skillId = "ali.math.counting.001",
        learnerPath = "KG2",
        domain = "Numeracy",
        observableOutcome = "Counts a small set of objects accurately"
    )

    private fun validActivity() = ActivityDefinition(
        activityId = "ali.math.counting.activity.001",
        skillId = "ali.math.counting.001",
        activityType = "SINGLE_CHOICE",
        language = "en",
        prompt = "How many apples can you see?",
        options = listOf(
            AnswerOption(optionId = "two", text = "2"),
            AnswerOption(optionId = "three", text = "3"),
            AnswerOption(optionId = "four", text = "4")
        ),
        correctOptionId = "three",
        feedbackCorrect = "Well done!",
        feedbackIncorrect = "Count the apples one more time.",
        difficulty = 1
    )

    private fun validPack(
        skills: List<SkillDefinition> = listOf(baseSkill()),
        activities: List<ActivityDefinition> = emptyList()
    ) = ContentPack(
        packId = "ali-numeracy-foundation",
        version = "0.1.0",
        schemaVersion = 2,
        learnerPath = "KG2",
        skills = skills,
        activities = activities
    )

    @Test
    fun validPackIsAccepted() {
        assertTrue(ContentPackValidator.validate(validPack()).isValid)
    }

    @Test
    fun validSingleChoiceActivityIsAccepted() {
        val result = ContentPackValidator.validate(validPack(activities = listOf(validActivity())))
        assertTrue(result.errors.joinToString(), result.isValid)
    }

    @Test
    fun duplicateSkillIdsAreRejected() {
        val skill = baseSkill()
        val result = ContentPackValidator.validate(validPack(skills = listOf(skill, skill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("skillId values must be unique") })
    }

    @Test
    fun mixedLearnerPathsAreRejected() {
        val mismatchedSkill = baseSkill().copy(
            skillId = "aboudy.math.001",
            learnerPath = "GRADE_2"
        )
        val result = ContentPackValidator.validate(validPack(skills = listOf(mismatchedSkill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("different learner path") })
    }

    @Test
    fun emptySkillListIsRejected() {
        val result = ContentPackValidator.validate(validPack(skills = emptyList()))
        assertFalse(result.isValid)
    }

    @Test
    fun unsupportedSchemaAndLearnerPathAreRejected() {
        val result = ContentPackValidator.validate(
            validPack().copy(schemaVersion = 99, learnerPath = "UNKNOWN")
        )
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.startsWith("Unsupported schemaVersion") })
        assertTrue(result.errors.any { it.startsWith("Unsupported learnerPath") })
    }

    @Test
    fun blankRequiredSkillFieldsAreRejected() {
        val invalidSkill = baseSkill().copy(
            skillId = " ",
            domain = " ",
            observableOutcome = ""
        )
        val result = ContentPackValidator.validate(validPack(skills = listOf(invalidSkill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("skillId must not be blank") })
        assertTrue(result.errors.any { it.contains("empty domain") })
        assertTrue(result.errors.any { it.contains("empty observable outcome") })
    }

    @Test
    fun activityWithUnknownSkillOrIncorrectAnswerReferenceIsRejected() {
        val invalidActivity = validActivity().copy(
            skillId = "missing-skill",
            correctOptionId = "not-an-option"
        )
        val result = ContentPackValidator.validate(validPack(activities = listOf(invalidActivity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unknown skillId") })
        assertTrue(result.errors.any { it.contains("exactly one correct option") })
    }

    @Test
    fun duplicateOptionIdsAndInvalidDifficultyAreRejected() {
        val activity = validActivity().copy(
            options = listOf(
                AnswerOption(optionId = "same", text = "A"),
                AnswerOption(optionId = "same", text = "B")
            ),
            correctOptionId = "same",
            difficulty = 6
        )
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("optionId values must be unique") })
        assertTrue(result.errors.any { it.contains("difficulty must be between 1 and 5") })
    }

    @Test
    fun previousSchemaVersionIsRejectedExplicitly() {
        val result = ContentPackValidator.validate(validPack().copy(schemaVersion = 1))
        assertFalse(result.isValid)
        assertTrue(result.errors.single().contains("Unsupported schemaVersion"))
    }
}
