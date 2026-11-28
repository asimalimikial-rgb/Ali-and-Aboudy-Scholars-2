package com.asimalimikial.aas2.learning

import com.asimalimikial.aas2.content.BookDefinition
import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.ContentSourceReference
import com.asimalimikial.aas2.content.GuidedTaskDefinition
import com.asimalimikial.aas2.content.LessonDefinition
import com.asimalimikial.aas2.content.SkillDefinition
import com.asimalimikial.aas2.content.UnitDefinition
import com.asimalimikial.aas2.data.LearnerProfileDao
import com.asimalimikial.aas2.data.LearnerProfileEntity
import com.asimalimikial.aas2.data.LearningAttemptDao
import com.asimalimikial.aas2.data.LearningAttemptEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GuidedTaskCompletionServiceTest {
    private fun pack(path: String = "KG2") = ContentPack(
        packId = "kg2-test-pack",
        version = "0.2.7",
        schemaVersion = 4,
        learnerPath = path,
        skills = listOf(
            SkillDefinition(
                skillId = "fine-motor",
                learnerPath = path,
                domain = "Early Learning",
                observableOutcome = "Makes controlled marks with adult support",
                sourceReferenceIds = listOf("test-source")
            )
        ),
        sourceReferences = listOf(
            ContentSourceReference(
                sourceReferenceId = "test-source",
                title = "Test source",
                publisher = "AAS2 tests",
                url = "https://example.org/test-source",
                license = "Reference only",
                usageRights = "REFERENCE_ONLY",
                verifiedAt = "2026-11-18"
            )
        ),
        books = listOf(BookDefinition("book-1", "Hop Skip and Jump", "Early Learning", path, listOf("test-source"))),
        units = listOf(UnitDefinition("unit-1", "book-1", "Unit 1", 1, listOf("lesson-1"))),
        lessons = listOf(LessonDefinition("lesson-1", "unit-1", "Lesson 1", 1, listOf("fine-motor"))),
        guidedTasks = listOf(
            GuidedTaskDefinition(
                activityId = "guided-1",
                skillId = "fine-motor",
                title = "Make careful lines and shapes",
                instructions = listOf("Trace a line.", "Draw a circle."),
                observableCriteria = listOf("Makes controlled marks", "Attempts a recognisable shape"),
                assessment = "Adult observation; no fixed right/wrong answer.",
                bookId = "book-1",
                unitId = "unit-1",
                lessonId = "lesson-1"
            )
        )
    )

    private class FakeLearnerProfileDao(private val profiles: List<LearnerProfileEntity>) : LearnerProfileDao {
        override fun observeProfiles(): Flow<List<LearnerProfileEntity>> = flowOf(profiles)
        override suspend fun insertDefaultsIfMissing(profiles: List<LearnerProfileEntity>) = Unit
        override suspend fun findById(learnerId: String): LearnerProfileEntity? =
            profiles.firstOrNull { it.learnerId == learnerId }
    }

    private class RecordingLearningAttemptDao : LearningAttemptDao {
        val saved = mutableListOf<LearningAttemptEntity>()
        override suspend fun insert(attempt: LearningAttemptEntity) { saved += attempt }
        override fun observeForLearner(learnerId: String): Flow<List<LearningAttemptEntity>> =
            flowOf(saved.filter { it.learnerId == learnerId })
        override fun observeForLearnerAndSkill(learnerId: String, skillId: String): Flow<List<LearningAttemptEntity>> =
            flowOf(saved.filter { it.learnerId == learnerId && it.skillId == skillId })
        override suspend fun countForLearner(learnerId: String): Int = saved.count { it.learnerId == learnerId }
        override suspend fun countScoredAttemptsForSkill(learnerId: String, skillId: String): Int =
            saved.count { it.learnerId == learnerId && it.skillId == skillId && it.isCorrect != null }
    }

    @Test
    fun completionPersistsAnUnscoredLearnerScopedObservation() = runBlocking<Unit> {
        val dao = RecordingLearningAttemptDao()
        val service = GuidedTaskCompletionService(
            learnerProfileDao = FakeLearnerProfileDao(listOf(LearnerProfileEntity("ali", "Ali", "KG2"))),
            learningAttemptDao = dao,
            nowEpochMillis = { 1234L },
            attemptIdFactory = { "guided-completion-1" }
        )

        service.complete("ali", pack(), "guided-1", "Held the pencil gently and traced a circle.")

        val saved = dao.saved.single()
        assertEquals("ali", saved.learnerId)
        assertEquals("guided-1", saved.activityId)
        assertEquals("fine-motor", saved.skillId)
        assertNull(saved.isCorrect)
        assertNull(saved.selectedOptionId)
        assertEquals(false, saved.usedHint)
        assertEquals("kg2-test-pack", saved.contentPackId)
        assertEquals("0.2.7", saved.contentPackVersion)
        assertEquals("Held the pencil gently and traced a circle.", saved.feedbackText)
        assertEquals(1234L, saved.createdAtEpochMillis)
    }

    @Test(expected = IllegalArgumentException::class)
    fun inactiveLearnerCannotCompleteGuidedTask() = runBlocking<Unit> {
        val dao = RecordingLearningAttemptDao()
        val service = GuidedTaskCompletionService(
            learnerProfileDao = FakeLearnerProfileDao(
                listOf(LearnerProfileEntity("ali", "Ali", "KG2", isActive = false))
            ),
            learningAttemptDao = dao
        )
        service.complete("ali", pack(), "guided-1")
        assertTrue(dao.saved.isEmpty())
    }

    @Test(expected = IllegalArgumentException::class)
    fun observationNoteCannotExceedFiveHundredCharacters() = runBlocking<Unit> {
        val dao = RecordingLearningAttemptDao()
        val service = GuidedTaskCompletionService(
            learnerProfileDao = FakeLearnerProfileDao(listOf(LearnerProfileEntity("ali", "Ali", "KG2"))),
            learningAttemptDao = dao
        )
        service.complete("ali", pack(), "guided-1", "x".repeat(501))
        assertTrue(dao.saved.isEmpty())
    }
}
