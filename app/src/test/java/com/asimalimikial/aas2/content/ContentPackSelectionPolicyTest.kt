package com.asimalimikial.aas2.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentPackSelectionPolicyTest {
    @Test
    fun canonicalizesKnownLearnerPathways() {
        assertEquals("KG2", ContentPackSelectionPolicy.canonicalLearnerPath("kg2"))
        assertEquals("GRADE_2", ContentPackSelectionPolicy.canonicalLearnerPath("Grade 2"))
        assertEquals("GRADE_2", ContentPackSelectionPolicy.canonicalLearnerPath("GRADE2"))
        assertNull(ContentPackSelectionPolicy.canonicalLearnerPath("Grade 3"))
    }

    @Test
    fun mapsEachLearnerPathwayToItsFoundationPack() {
        assertEquals("aas2-kg2-foundation", ContentPackSelectionPolicy.packIdForPathway("KG2"))
        assertEquals("aas2-grade2-foundation", ContentPackSelectionPolicy.packIdForPathway("Grade 2"))
        assertNull(ContentPackSelectionPolicy.packIdForPathway("Grade 3"))
    }

    @Test
    fun selectsOnlyEnglishLearningActivitiesRegardlessOfDeviceLocale() {
        val pack = samplePack()
        val activities = ContentPackSelectionPolicy.englishActivities(pack)

        assertEquals(listOf("activity-en"), activities.map { it.activityId })
        assertTrue(activities.all { it.language == "en" })
    }

    @Test
    fun doesNotFallBackToArabicWhenEnglishContentIsMissing() {
        val pack = samplePack(activities = listOf(sampleActivity("activity-ar", "ar")))

        assertTrue(ContentPackSelectionPolicy.englishActivities(pack).isEmpty())
    }

    @Test
    fun startsWithTheFirstUnpractisedActivity() {
        val activities = listOf(
            sampleActivity("activity-1", "en"),
            sampleActivity("activity-2", "en")
        )

        assertEquals(
            "activity-2",
            ContentPackSelectionPolicy.nextActivity(activities, setOf("activity-1"))?.activityId
        )
    }

    @Test
    fun restartsFromTheFirstActivityWhenAllActivitiesWerePractised() {
        val activities = listOf(sampleActivity("activity-1", "en"))

        assertEquals(
            "activity-1",
            ContentPackSelectionPolicy.nextActivity(activities, setOf("activity-1"))?.activityId
        )
    }

    @Test
    fun returnsNullWhenNoActivitiesExist() {
        assertNull(ContentPackSelectionPolicy.nextActivity(emptyList(), emptySet()))
    }

    @Test
    fun groupsKg2DomainsIntoAgeAppropriateLearningAreas() {
        assertEquals("Mathematics", ContentPackSelectionPolicy.subjectForDomain("Early Numeracy", "KG2"))
        assertEquals("Mathematics", ContentPackSelectionPolicy.subjectForDomain("Mathematics", "KG2"))
        assertEquals("English & Literacy", ContentPackSelectionPolicy.subjectForDomain("Language and Communication", "KG2"))
        assertEquals("English & Literacy", ContentPackSelectionPolicy.subjectForDomain("English", "KG2"))
        assertEquals("Discovery", ContentPackSelectionPolicy.subjectForDomain("Science and Discovery", "KG2"))
    }

    @Test
    fun keepsGradeTwoCoreSubjectsDistinct() {
        assertEquals("English", ContentPackSelectionPolicy.subjectForDomain("English Language", "Grade 2"))
        assertEquals("Mathematics", ContentPackSelectionPolicy.subjectForDomain("Mathematics", "Grade 2"))
        assertEquals("Science", ContentPackSelectionPolicy.subjectForDomain("Science", "Grade 2"))
    }

    private fun samplePack(
        activities: List<ActivityDefinition> = listOf(
            sampleActivity("activity-en", "en"),
            sampleActivity("activity-ar", "ar")
        )
    ) = ContentPack(
        packId = "test-pack",
        version = "0.1.0",
        schemaVersion = 3,
        learnerPath = "KG2",
        skills = emptyList(),
        activities = activities,
        sourceReferences = emptyList()
    )

    private fun sampleActivity(activityId: String, language: String) = ActivityDefinition(
        activityId = activityId,
        skillId = "test-skill",
        activityType = "SINGLE_CHOICE",
        language = language,
        prompt = "Test prompt",
        options = listOf(
            AnswerOption(optionId = "a", text = "A"),
            AnswerOption(optionId = "b", text = "B")
        ),
        correctOptionId = "a",
        feedbackCorrect = "Correct",
        feedbackIncorrect = "Try again",
        difficulty = 1
    )
}
