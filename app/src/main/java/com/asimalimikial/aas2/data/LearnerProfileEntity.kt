package com.asimalimikial.aas2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learner_profiles")
data class LearnerProfileEntity(
    @PrimaryKey val learnerId: String,
    val displayName: String,
    val pathway: String,
    val isActive: Boolean = true
)
