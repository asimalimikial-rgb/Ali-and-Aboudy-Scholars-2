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
    fun selectsArabicActivitiesForArabicDeviceLanguage() {
        val pack = samplePack()
        val activities = ContentPackSelectionPolicy.activitiesForLanguage(pack, "ar-SD")

        assertEquals(listOf("activity-ar"), activities.map { it.activityId })
        assertTrue(activities.all { it.language == "ar" })
    }

    @Test
    fun fallsBackToEnglishWhenPreferredLanguageIsUnavailable() {
        val pack = samplePack(activities = listOf(sampleActivity("activity-en", "en")))

        val activities = ContentPackSelectionPolicy.activitiesForLanguage(pack, "ar")

        assertEquals(listOf("activity-en"), activities.map { it.activityId })
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
            ActivityOption(optionId = "a", text = "A"),
            ActivityOption(optionId = "b", text = "B")
        ),
        correctOptionId = "a",
        feedbackCorrect = "Correct",
        feedbackIncorrect = "Try again",
        difficulty = 1
    )
}
