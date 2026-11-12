package com.asimalimikial.aas2.data

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LearningAttemptDaoTest {
    private lateinit var database: AppDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun attemptsAreVisibleOnlyToTheirOwnLearner() = runBlocking {
        database.learnerProfileDao().insertDefaultsIfMissing(
            listOf(
                LearnerProfileEntity("ali", "Ali", "KG2"),
                LearnerProfileEntity("aboudy", "Aboudy", "Grade 2")
            )
        )

        database.learningAttemptDao().insert(
            LearningAttemptEntity(
                attemptId = "ali-attempt-1",
                learnerId = "ali",
                activityId = "ali-activity-1",
                skillId = "ali.math.counting.001",
                isCorrect = true,
                usedHint = false,
                createdAtEpochMillis = 1_000L
            )
        )
        database.learningAttemptDao().insert(
            LearningAttemptEntity(
                attemptId = "aboudy-attempt-1",
                learnerId = "aboudy",
                activityId = "aboudy-activity-1",
                skillId = "aboudy.math.place-value.001",
                isCorrect = false,
                usedHint = true,
                createdAtEpochMillis = 2_000L
            )
        )

        val aliAttempts = database.learningAttemptDao()
            .observeForLearner("ali")
            .first()
        val aboudyAttempts = database.learningAttemptDao()
            .observeForLearner("aboudy")
            .first()

        assertEquals(listOf("ali-attempt-1"), aliAttempts.map { it.attemptId })
        assertEquals(listOf("aboudy-attempt-1"), aboudyAttempts.map { it.attemptId })
        assertEquals(1, database.learningAttemptDao().countForLearner("ali"))
        assertEquals(1, database.learningAttemptDao().countForLearner("aboudy"))
    }

    @Test
    fun attemptForUnknownLearnerIsRejectedByForeignKey() = runBlocking {
        try {
            database.learningAttemptDao().insert(
                LearningAttemptEntity(
                    attemptId = "orphan-attempt",
                    learnerId = "unknown-learner",
                    activityId = "activity-1",
                    skillId = "skill-1",
                    isCorrect = true,
                    usedHint = false,
                    createdAtEpochMillis = 3_000L
                )
            )
            fail("An attempt without a learner profile must be rejected")
        } catch (_: SQLiteConstraintException) {
            // Expected: the learnerId foreign key protects data integrity.
        }
    }
}
