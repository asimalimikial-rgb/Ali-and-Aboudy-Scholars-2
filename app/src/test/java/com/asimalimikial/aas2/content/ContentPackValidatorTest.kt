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

    private fun validSource() = ContentSourceReference(
        sourceReferenceId = "source-1",
        title = "Test source",
        publisher = "Test publisher",
        url = "https://example.org/source",
        license = "Reference only; reuse not assumed"
    )

    private fun validPack(
        skills: List<SkillDefinition> = listOf(baseSkill()),
        activities: List<ActivityDefinition> = emptyList(),
        sourceReferences: List<ContentSourceReference> = emptyList()
    ) = ContentPack(
        packId = "ali-numeracy-foundation",
        version = "0.1.0",
        schemaVersion = 3,
        learnerPath = "KG2",
        skills = skills,
        activities = activities,
        sourceReferences = sourceReferences
    )

    @Test fun validPackIsAccepted() {
        assertTrue(ContentPackValidator.validate(validPack()).isValid)
    }

    @Test fun validSingleChoiceActivityIsAccepted() {
        val result = ContentPackValidator.validate(validPack(activities = listOf(validActivity())))
        assertTrue(result.errors.joinToString(), result.isValid)
    }

    @Test fun arabicActivityIsAcceptedForRtlRendering() {
        val arabicActivity = validActivity().copy(
            language = "ar",
            prompt = "كم تفاحة ترى؟",
            options = listOf(AnswerOption("two", "٢"), AnswerOption("three", "٣")),
            correctOptionId = "three",
            feedbackCorrect = "أحسنت!",
            feedbackIncorrect = "حاول العد مرة أخرى."
        )
        val result = ContentPackValidator.validate(validPack(activities = listOf(arabicActivity)))
        assertTrue(result.errors.joinToString(), result.isValid)
    }

    @Test fun validSourceReferenceResolvesSkillProvenance() {
        val source = validSource()
        val skill = baseSkill().copy(sourceReferenceIds = listOf(source.sourceReferenceId))
        val result = ContentPackValidator.validate(
            validPack(skills = listOf(skill), sourceReferences = listOf(source))
        )
        assertTrue(result.errors.joinToString(), result.isValid)
    }

    @Test fun unknownSourceReferenceIsRejected() {
        val skill = baseSkill().copy(sourceReferenceIds = listOf("missing-source"))
        val result = ContentPackValidator.validate(validPack(skills = listOf(skill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unknown sourceReferenceId") })
    }

    @Test fun sourceLicenseAndSecureUrlAreRequired() {
        val invalidSource = validSource().copy(url = "http://example.org/source", license = " ")
        val result = ContentPackValidator.validate(validPack(sourceReferences = listOf(invalidSource)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("HTTPS URL") })
        assertTrue(result.errors.any { it.contains("no license or rights statement") })
    }


    @Test fun malformedHttpsUrlsWithoutHostsAreRejected() {
        val invalidSource = validSource().copy(
            url = "https://",
            licenseUrl = "https:///license"
        )
        val result = ContentPackValidator.validate(validPack(sourceReferences = listOf(invalidSource)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("valid HTTPS URL") })
        assertTrue(result.errors.any { it.contains("invalid HTTPS license URL") })
    }

    @Test fun invalidCalendarDateIsRejected() {
        val invalidSource = validSource().copy(verifiedAt = "2025-02-29")
        val result = ContentPackValidator.validate(validPack(sourceReferences = listOf(invalidSource)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("valid YYYY-MM-DD date") })
    }

    @Test fun validLeapDayIsAcceptedAsVerificationDate() {
        val source = validSource().copy(verifiedAt = "2024-02-29")
        val result = ContentPackValidator.validate(validPack(sourceReferences = listOf(source)))
        assertTrue(result.errors.joinToString(), result.isValid)
    }

    @Test fun redistributionRequiresAttribution() {
        val source = validSource().copy(
            license = "CC BY 4.0",
            licenseUrl = "https://creativecommons.org/licenses/by/4.0/",
            usageRights = "REDISTRIBUTION_ALLOWED"
        )
        val result = ContentPackValidator.validate(validPack(sourceReferences = listOf(source)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("requires attribution") })
    }

    @Test fun duplicateSkillIdsAreRejected() {
        val skill = baseSkill()
        val result = ContentPackValidator.validate(validPack(skills = listOf(skill, skill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("skillId values must be unique") })
    }

    @Test fun mixedLearnerPathsAreRejected() {
        val mismatchedSkill = baseSkill().copy(skillId = "aboudy.math.001", learnerPath = "GRADE_2")
        val result = ContentPackValidator.validate(validPack(skills = listOf(mismatchedSkill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("different learner path") })
    }

    @Test fun emptySkillListIsRejected() {
        assertFalse(ContentPackValidator.validate(validPack(skills = emptyList())).isValid)
    }

    @Test fun unsupportedSchemaAndLearnerPathAreRejected() {
        val result = ContentPackValidator.validate(validPack().copy(schemaVersion = 99, learnerPath = "UNKNOWN"))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.startsWith("Unsupported schemaVersion") })
        assertTrue(result.errors.any { it.startsWith("Unsupported learnerPath") })
    }

    @Test fun blankRequiredSkillFieldsAreRejected() {
        val invalidSkill = baseSkill().copy(skillId = " ", domain = " ", observableOutcome = "")
        val result = ContentPackValidator.validate(validPack(skills = listOf(invalidSkill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("skillId must not be blank") })
        assertTrue(result.errors.any { it.contains("empty domain") })
        assertTrue(result.errors.any { it.contains("empty observable outcome") })
    }

    @Test fun duplicateSourceReferenceIdsAreRejected() {
        val skill = baseSkill().copy(sourceReferenceIds = listOf("source-a", "source-a"))
        val result = ContentPackValidator.validate(validPack(skills = listOf(skill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("duplicate source reference IDs") })
    }

    @Test fun activityWithUnknownSkillOrIncorrectAnswerReferenceIsRejected() {
        val invalidActivity = validActivity().copy(skillId = "missing-skill", correctOptionId = "not-an-option")
        val result = ContentPackValidator.validate(validPack(activities = listOf(invalidActivity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unknown skillId") })
        assertTrue(result.errors.any { it.contains("exactly one correct option") })
    }

    @Test fun unsafeImageAssetPathsAreRejected() {
        val activity = validActivity().copy(
            options = listOf(
                AnswerOption("one", "1", "../private/file.png"),
                AnswerOption("two", "2")
            ),
            correctOptionId = "two"
        )
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unsafe image asset path") })
    }

    @Test fun duplicateOptionIdsAndInvalidDifficultyAreRejected() {
        val activity = validActivity().copy(
            options = listOf(AnswerOption("same", "A"), AnswerOption("same", "B")),
            correctOptionId = "same",
            difficulty = 6
        )
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("optionId values must be unique") })
        assertTrue(result.errors.any { it.contains("difficulty must be between 1 and 5") })
    }

    @Test fun previousSchemaVersionIsRejectedExplicitly() {
        val result = ContentPackValidator.validate(validPack().copy(schemaVersion = 2))
        assertFalse(result.isValid)
        assertTrue(result.errors.single().contains("Unsupported schemaVersion"))
    }
}
