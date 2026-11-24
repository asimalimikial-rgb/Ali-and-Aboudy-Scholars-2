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

    private fun validSkill() = baseSkill().copy(sourceReferenceIds = listOf("source-1"))

    private fun validSource() = ContentSourceReference(
        sourceReferenceId = "source-1",
        title = "Test source",
        publisher = "Test publisher",
        url = "https://example.org/source",
        license = "Reference only; reuse not assumed",
        usageRights = "REFERENCE_ONLY",
        verifiedAt = "2026-11-18"
    )

    private fun validPack(
        skills: List<SkillDefinition> = listOf(validSkill()),
        activities: List<ActivityDefinition> = emptyList(),
        sourceReferences: List<ContentSourceReference> = listOf(validSource())
    ) = ContentPack(
        packId = "ali-numeracy-foundation",
        version = "0.1.0",
        schemaVersion = 3,
        learnerPath = "KG2",
        skills = skills,
        activities = activities,
        sourceReferences = sourceReferences
    )


    @Test fun packWithoutSourceReferencesIsRejected() {
        val result = ContentPackValidator.validate(validPack(sourceReferences = emptyList()))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("at least one source reference") })
    }

    @Test fun skillWithoutSourceReferencesIsRejected() {
        val result = ContentPackValidator.validate(validPack(skills = listOf(baseSkill())))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("must reference at least one sourceReferenceId") })
    }

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
        val skill = validSkill()
        val result = ContentPackValidator.validate(validPack(skills = listOf(skill, skill)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("skillId values must be unique") })
    }

    @Test fun mixedLearnerPathsAreRejected() {
        val mismatchedSkill = validSkill().copy(skillId = "aboudy.math.001", learnerPath = "GRADE_2")
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
        val invalidSkill = validSkill().copy(skillId = " ", domain = " ", observableOutcome = "")
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

    @Test fun offlineSvgPromptAndOptionAssetsAreAccepted() {
        val activity = validActivity().copy(
            promptImageAssetPath = "question-art/activity-001.svg",
            options = listOf(
                AnswerOption("two", "2", "option-art/two.svg"),
                AnswerOption("three", "3", "option-art/three.svg"),
                AnswerOption("four", "4", "option-art/four.svg")
            )
        )
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertTrue(result.errors.joinToString(), result.isValid)
    }

    @Test fun unsupportedVisualFileExtensionsAreRejected() {
        val activity = validActivity().copy(promptImageAssetPath = "question-art/activity-001.webp")
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unsafe prompt image asset path") })
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
 
    @Test fun schemaFourBookUnitLessonAndQuestionMetadataAreAccepted() {
        val activity = validActivity().copy(
            bookId = "book-1",
            unitId = "unit-1",
            lessonId = "lesson-1",
            assessmentPhase = "DIAGNOSTIC",
            misconceptionCode = "misconception-1",
            optionRationales = mapOf(
                "two" to "Rationale for two",
                "three" to "Rationale for three",
                "four" to "Rationale for four"
            ),
            promptImageAssetPath = "question-art/activity-001.png",
            promptAudioAssetPath = "speech/test-prompt.ogg",
            optionAudioAssetPaths = mapOf(
                "two" to "speech/test-two.ogg",
                "three" to "speech/test-three.ogg",
                "four" to "speech/test-four.ogg"
            ),
            feedbackCorrectAudioAssetPath = "speech/test-correct.ogg",
            feedbackIncorrectAudioAssetPath = "speech/test-incorrect.ogg",
                )
        val pack = validPack(activities = listOf(activity)).copy(
            schemaVersion = 4,
            books = listOf(
                BookDefinition(
                    bookId = "book-1",
                    title = "Test Science Book",
                    subject = "Science",
                    learnerPath = "KG2",
                    sourceReferenceIds = listOf("source-1")
                )
            ),
            units = listOf(
                UnitDefinition(
                    unitId = "unit-1",
                    bookId = "book-1",
                    title = "Unit 1",
                    sequence = 1,
                    lessonIds = listOf("lesson-1")
                )
            ),
            lessons = listOf(
                LessonDefinition(
                    lessonId = "lesson-1",
                    unitId = "unit-1",
                    title = "Lesson 1",
                    sequence = 1,
                    skillIds = listOf("ali.math.counting.001")
                )
            )
        )
        val result = ContentPackValidator.validate(pack)
        assertTrue(result.errors.joinToString(), result.isValid)
    }

    @Test fun schemaFourRejectsUnsafePromptImagePath() {
        val activity = validActivity().copy(
            bookId = "book-1",
            unitId = "unit-1",
            lessonId = "lesson-1",
            assessmentPhase = "DIAGNOSTIC",
            misconceptionCode = "misconception-1",
            optionRationales = mapOf(
                "two" to "Rationale for two",
                "three" to "Rationale for three",
                "four" to "Rationale for four"
            ),
            promptImageAssetPath = "../outside.png",
            promptAudioAssetPath = "speech/test-prompt.ogg",
            optionAudioAssetPaths = mapOf(
                "two" to "speech/test-two.ogg",
                "three" to "speech/test-three.ogg",
                "four" to "speech/test-four.ogg"
            ),
            feedbackCorrectAudioAssetPath = "speech/test-correct.ogg",
            feedbackIncorrectAudioAssetPath = "speech/test-incorrect.ogg",
                )
        val pack = validPack(activities = listOf(activity)).copy(
            schemaVersion = 4,
            books = listOf(BookDefinition("book-1", "Book", "Science", "KG2", listOf("source-1"))),
            units = listOf(UnitDefinition("unit-1", "book-1", "Unit", 1, listOf("lesson-1"))),
            lessons = listOf(LessonDefinition("lesson-1", "unit-1", "Lesson", 1, listOf("ali.math.counting.001")))
        )
        val result = ContentPackValidator.validate(pack)
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unsafe prompt image asset path") })
    }

    @Test fun schemaFourRejectsUnsupportedAssessmentPhase() {
        val activity = validActivity().copy(
            bookId = "book-1",
            unitId = "unit-1",
            lessonId = "lesson-1",
            assessmentPhase = "GUESSING",
            misconceptionCode = "misconception-1",
            optionRationales = mapOf(
                "two" to "Rationale for two",
                "three" to "Rationale for three",
                "four" to "Rationale for four"
            ),
            promptImageAssetPath = "question-art/activity-001.png",
            promptAudioAssetPath = "speech/test-prompt.ogg",
            optionAudioAssetPaths = mapOf(
                "two" to "speech/test-two.ogg",
                "three" to "speech/test-three.ogg",
                "four" to "speech/test-four.ogg"
            ),
            feedbackCorrectAudioAssetPath = "speech/test-correct.ogg",
            feedbackIncorrectAudioAssetPath = "speech/test-incorrect.ogg",
                )
        val pack = validPack(activities = listOf(activity)).copy(
            schemaVersion = 4,
            books = listOf(BookDefinition("book-1", "Book", "Science", "KG2", listOf("source-1"))),
            units = listOf(UnitDefinition("unit-1", "book-1", "Unit", 1, listOf("lesson-1"))),
            lessons = listOf(LessonDefinition("lesson-1", "unit-1", "Lesson", 1, listOf("ali.math.counting.001")))
        )
        val result = ContentPackValidator.validate(pack)
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("assessmentPhase") })
    }


    @Test
    fun englishHintsAndStepByStepHelpAreAccepted() {
        val activity = validActivity().copy(
            hintSequence = listOf("Count each apple once.", "Touch each apple as you count."),
            helpSteps = listOf("Point to one apple at a time.", "Say one number for each apple.")
        )
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertTrue(result.errors.joinToString(), result.isValid)
    }

    @Test
    fun arabicTextIsRejectedFromEnglishHintsAndHelp() {
        val activity = validActivity().copy(hintSequence = listOf("عدّ كل تفاحة مرة واحدة."))
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("English-only text") })
    }

    @Test
    fun historicalArabicActivitiesCannotContainHintsOrHelp() {
        val activity = validActivity().copy(language = "ar", prompt = "كم تفاحة ترى؟", hintSequence = listOf("Count each apple once."))
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("only allowed on English learner activities") })
    }

    @Test
    fun excessiveHintStepsAreRejected() {
        val activity = validActivity().copy(hintSequence = listOf("One.", "Two.", "Three.", "Four."))
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("at most three progressive hints") })
    }

    @Test
    fun unsafeHintAndHelpImagePathsAreRejected() {
        val activity = validActivity().copy(hintImageAssetPath = "../outside.svg", helpImageAssetPath = "https://example.org/help.svg")
        val result = ContentPackValidator.validate(validPack(activities = listOf(activity)))
        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("unsafe hint image asset path") })
        assertTrue(result.errors.any { it.contains("unsafe help image asset path") })
    }

}
