package com.asimalimikial.aas2.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "learning_attempts",
    foreignKeys = [
        ForeignKey(
            entity = LearnerProfileEntity::class,
            parentColumns = ["learnerId"],
            childColumns = ["learnerId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["learnerId"]),
        Index(value = ["skillId"]),
        Index(value = ["learnerId", "createdAtEpochMillis"])
    ]
)
data class LearningAttemptEntity(
    @PrimaryKey val attemptId: String,
    val learnerId: String,
    val activityId: String,
    val skillId: String,
    val isCorrect: Boolean?,
    val usedHint: Boolean,
    val createdAtEpochMillis: Long,
    /**
     * Nullable so attempts created before schema version 2 remain readable.
     */
    val contentPackId: String? = null,
    val contentPackVersion: String? = null,
    val selectedOptionId: String? = null,
    val feedbackText: String? = null
)
