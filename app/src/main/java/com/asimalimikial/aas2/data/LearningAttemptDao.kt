package com.asimalimikial.aas2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningAttemptDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(attempt: LearningAttemptEntity)

    @Query(
        """
        SELECT * FROM learning_attempts
        WHERE learnerId = :learnerId
        ORDER BY createdAtEpochMillis ASC, attemptId ASC
        """
    )
    fun observeForLearner(learnerId: String): Flow<List<LearningAttemptEntity>>

    @Query("SELECT COUNT(*) FROM learning_attempts WHERE learnerId = :learnerId")
    suspend fun countForLearner(learnerId: String): Int
}
