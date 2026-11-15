package com.asimalimikial.aas2.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ActivitySessionKeyTest {
    private fun key(
        learnerId: String = "ali",
        packId: String = "kg2-pack",
        packVersion: String = "1.0.0",
        activityId: String = "count-1"
    ) = ActivitySessionKey(
        learnerId = learnerId,
        contentPackId = packId,
        contentPackVersion = packVersion,
        activityId = activityId
    )

    @Test
    fun sameLearnerPackVersionAndActivityHaveStableIdentity() {
        assertEquals(key(), key())
    }

    @Test
    fun switchingLearnerCreatesANewActivitySessionIdentity() {
        assertNotEquals(key(learnerId = "ali"), key(learnerId = "aboudy"))
    }

    @Test
    fun switchingActivityCreatesANewActivitySessionIdentity() {
        assertNotEquals(key(activityId = "count-1"), key(activityId = "count-2"))
    }

    @Test
    fun changingContentPackVersionCreatesANewActivitySessionIdentity() {
        assertNotEquals(key(packVersion = "1.0.0"), key(packVersion = "1.0.1"))
    }
}
