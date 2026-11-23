package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.content.AnswerOption
import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.ContentSourceReference
import com.asimalimikial.aas2.content.SkillDefinition
import com.asimalimikial.aas2.data.LearnerProfileDao
import com.asimalimikial.aas2.data.LearnerProfileEntity
import com.asimalimikial.aas2.data.LearningAttemptDao
import com.asimalimikial.aas2.data.LearningAttemptEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivitySubmissionServiceTest {
    private fun pack(path: String = "KG2") = ContentPack(
        packId = "test-pack",
        version = "0.1.0",
        schemaVersion = 3,
        learnerPath = path,
        skills = listOf(
            SkillDefinition(
                skillId = "counting",
                learnerPath = path,
                domain = "Numeracy",
                observableOutcome = "Counts a small set",
                sourceReferenceIds = listOf("test-source")
            )
        ),
        sourceReferences = listOf(
            ContentSourceReference(
                sourceReferenceId = "test-source",
                title = "Test fixture source",
                publisher = "AAS2 tests",
                url = "https://example.org/test-source",
                license = "Reference only; test fixture",
                usageRights = "REFERENCE_ONLY",
                verifiedAt = "2026-11-18"
            )
        ),
        activities = listOf(
            ActivityDefinition(
                activityId = "count-1",
                skillId = "counting",
                activityType = "SINGLE_CHOICE",
                language = "en",
                prompt = "How many?",
                options = listOf(AnswerOption("one", "1"), AnswerOption("two", "2")),
                correctOptionId = "two",
                feedbackCorrect = "Well done",
                feedbackIncorrect = "Try again",
                difficulty = 1
            )
        )
    )

    private class FakeLearnerProfileDao(
        private val profiles: List<LearnerProfileEntity>
    ) : LearnerProfileDao {
        override fun observeProfiles(): Flow<List<LearnerProfileEntity>> = flowOf(profiles)

        override suspend fun insertDefaultsIfMissing(profiles: List<LearnerProfileEntity>) = Unit

        override suspend fun findById(learnerId: String): LearnerProfileEntity? =
            profiles.firstOrNull { it.learnerId == learnerId }
    }

    private class RecordingLearningAttemptDao : LearningAttemptDao {
        val savedAttempts = mutableListOf<LearningAttemptEntity>()

        override suspend fun insert(attempt: LearningAttemptEntity) {
            savedAttempts += attempt
        }

        override fun observeForLearner(learnerId: String): Flow<List<LearningAttemptEntity>> =
            flowOf(savedAttempts.filter { it.learnerId == learnerId })

        override fun observeForLearnerAndSkill(
            learnerId: String,
            skillId: String
        ): Flow<List<LearningAttemptEntity>> =
            flowOf(savedAttempts.filter { it.learnerId == learnerId && it.skillId == skillId })

        override suspend fun countForLearner(learnerId: String): Int =
            savedAttempts.count { it.learnerId == learnerId }

        override suspend fun countScoredAttemptsForSkill(learnerId: String, skillId: String): Int =
            savedAttempts.count {
                it.learnerId == learnerId && it.skillId == skillId && it.isCorrect != null
            }
    }

    @Test
    fun validSubmissionIsEvaluatedAndFullSnapshotPersistedForSelectedLearner() = runBlocking<Unit> {
        val attempts = RecordingLearningAttemptDao()
        val service = ActivitySubmissionService(
            learnerProfileDao = FakeLearnerProfileDao(listOf(LearnerProfileEntity("ali", "Ali", "KG2"))),
            learningAttemptDao = attempts,
            nowEpochMillis = { 1234L },
            attemptIdFactory = { "attempt-1" }
        )

        val result = service.submit("ali", pack(), "count-1", "two", usedHint = true)

        assertTrue(result.isCorrect)
        assertEquals("Well done", result.feedback)
        assertEquals(1, attempts.savedAttempts.size)
        val saved = attempts.savedAttempts.single()
        assertEquals("attempt-1", saved.attemptId)
        assertEquals("ali", saved.learnerId)
        assertEquals("count-1", saved.activityId)
        assertEquals("counting", saved.skillId)
        assertEquals(true, saved.isCorrect)
        assertTrue(saved.usedHint)
        assertEquals(1234L, saved.createdAtEpochMillis)
        assertEquals("test-pack", saved.contentPackId)
        assertEquals("0.1.0", saved.contentPackVersion)
        assertEquals("two", saved.selectedOptionId)
        assertEquals("Well done", saved.feedbackText)
    }

    @Test
    fun incorrectSubmissionPersistsSelectedOptionAndIncorrectFeedback() = runBlocking<Unit> {
        val attempts = RecordingLearningAttemptDao()
        val service = ActivitySubmissionService(
            learnerProfileDao = FakeLearnerProfileDao(listOf(LearnerProfileEntity("ali", "Ali", "KG2"))),
            learningAttemptDao = attempts,
            nowEpochMillis = { 2000L },
            attemptIdFactory = { "attempt-2" }
        )

        val result = service.submit("ali", pack(), "count-1", "one")

        assertFalse(result.isCorrect)
        val saved = attempts.savedAttempts.single()
        assertEquals(false, saved.isCorrect)
        assertEquals("one", saved.selectedOptionId)
        assertEquals("Try again", saved.feedbackText)
    }

    @Test
    fun retrySubmissionsAreStoredAsSeparateAttempts() = runBlocking<Unit> {
        val attempts = RecordingLearningAttemptDao()
        var attemptNumber = 0
        val service = ActivitySubmissionService(
            learnerProfileDao = FakeLearnerProfileDao(listOf(LearnerProfileEntity("ali", "Ali", "KG2"))),
            learningAttemptDao = attempts,
            nowEpochMillis = { 3000L + attemptNumber },
            attemptIdFactory = { "attempt-${++attemptNumber}" }
        )

        val firstResult = service.submit("ali", pack(), "count-1", "one")
        val secondResult = service.submit("ali", pack(), "count-1", "two")

        assertFalse(firstResult.isCorrect)
        assertTrue(secondResult.isCorrect)
        assertEquals(2, attempts.savedAttempts.size)
        assertEquals(listOf("attempt-1", "attempt-2"), attempts.savedAttempts.map { it.attemptId })
        assertEquals(listOf(false, true), attempts.savedAttempts.map { it.isCorrect })
    }

    @Test(expected = IllegalArgumentException::class)
    fun unknownLearnerCannotSubmit() = runBlocking<Unit> {
        val service = ActivitySubmissionService(
            learnerProfileDao = FakeLearnerProfileDao(emptyList()),
            learningAttemptDao = RecordingLearningAttemptDao()
        )
        service.submit("missing", pack(), "count-1", "two")
    }

    @Test(expected = IllegalArgumentException::class)
    fun learnerCannotSubmitAnotherPathwaysContentPack() = runBlocking<Unit> {
        val service = ActivitySubmissionService(
            learnerProfileDao = FakeLearnerProfileDao(listOf(LearnerProfileEntity("aboudy", "Aboudy", "Grade 2"))),
            learningAttemptDao = RecordingLearningAttemptDao()
        )
        service.submit("aboudy", pack("KG2"), "count-1", "two")
    }

    @Test(expected = IllegalArgumentException::class)
    fun inactiveLearnerCannotSubmit() = runBlocking<Unit> {
        val service = ActivitySubmissionService(
            learnerProfileDao = FakeLearnerProfileDao(
                listOf(LearnerProfileEntity("ali", "Ali", "KG2", isActive = false))
            ),
            learningAttemptDao = RecordingLearningAttemptDao()
        )
        service.submit("ali", pack(), "count-1", "two")
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidAnswerIsNotPersisted() = runBlocking<Unit> {
        val attempts = RecordingLearningAttemptDao()
        val service = ActivitySubmissionService(
            learnerProfileDao = FakeLearnerProfileDao(listOf(LearnerProfileEntity("ali", "Ali", "KG2"))),
            learningAttemptDao = attempts
        )
        service.submit("ali", pack(), "count-1", "not-an-option")
        assertTrue(attempts.savedAttempts.isEmpty())
    }

    @Test
    fun submissionsRemainIsolatedAcrossAliAndAboudy() = runBlocking<Unit> {
        val attempts = RecordingLearningAttemptDao()
        val service = ActivitySubmissionService(
            learnerProfileDao = FakeLearnerProfileDao(
                listOf(
                    LearnerProfileEntity("ali", "Ali", "KG2"),
                    LearnerProfileEntity("aboudy", "Aboudy", "Grade 2")
                )
            ),
            learningAttemptDao = attempts,
            attemptIdFactory = { "attempt-${attempts.savedAttempts.size + 1}" }
        )

        service.submit("ali", pack("KG2"), "count-1", "two")
        service.submit("aboudy", pack("GRADE_2"), "count-1", "one")

        assertEquals(listOf("ali", "aboudy"), attempts.savedAttempts.map { it.learnerId })
        assertEquals(1, attempts.countForLearner("ali"))
        assertEquals(1, attempts.countForLearner("aboudy"))
        assertEquals(listOf("ali"), attempts.savedAttempts.filter { it.learnerId == "ali" }.map { it.learnerId })
        assertEquals(listOf("aboudy"), attempts.savedAttempts.filter { it.learnerId == "aboudy" }.map { it.learnerId })
    }
}
