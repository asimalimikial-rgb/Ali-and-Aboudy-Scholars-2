package com.asimalimikial.aas2.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LearnerSelectionPolicyTest {
    private val profiles = listOf(
        LearnerProfileEntity("ali", "Ali", "KG2"),
        LearnerProfileEntity("aboudy", "Aboudy", "Grade 2"),
        LearnerProfileEntity("archived", "Archived", "KG2", isActive = false)
    )

    @Test
    fun onlyActiveProfilesAreAvailableForSelection() {
        assertEquals(listOf("ali", "aboudy"), LearnerSelectionPolicy.activeProfiles(profiles).map { it.learnerId })
    }

    @Test
    fun selectedProfileMustExistAndBeActive() {
        assertEquals("ali", LearnerSelectionPolicy.selectedProfile("ali", profiles)?.learnerId)
        assertNull(LearnerSelectionPolicy.selectedProfile("missing", profiles))
        assertNull(LearnerSelectionPolicy.selectedProfile("archived", profiles))
    }

    @Test
    fun selectionIsNotClearedWhileProfilesAreLoading() {
        assertFalse(LearnerSelectionPolicy.shouldClearSelection("ali", emptyList()))
    }

    @Test
    fun staleBlankAndInactiveSelectionsAreClearedAfterProfilesLoad() {
        assertTrue(LearnerSelectionPolicy.shouldClearSelection("missing", profiles))
        assertTrue(LearnerSelectionPolicy.shouldClearSelection("archived", profiles))
        assertTrue(LearnerSelectionPolicy.shouldClearSelection("", profiles))
    }

    @Test
    fun validActiveSelectionIsPreserved() {
        assertFalse(LearnerSelectionPolicy.shouldClearSelection("ali", profiles))
    }
}
