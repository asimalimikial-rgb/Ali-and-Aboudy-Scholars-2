package com.asimalimikial.aas2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LearnerProfileDao {
    @Query("SELECT * FROM learner_profiles ORDER BY learnerId")
    fun observeProfiles(): Flow<List<LearnerProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDefaultsIfMissing(profiles: List<LearnerProfileEntity>)

    @Query("SELECT * FROM learner_profiles WHERE learnerId = :learnerId LIMIT 1")
    suspend fun findById(learnerId: String): LearnerProfileEntity?
}
