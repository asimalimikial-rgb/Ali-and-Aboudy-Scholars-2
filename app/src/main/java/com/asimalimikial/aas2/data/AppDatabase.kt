package com.asimalimikial.aas2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        LearnerProfileEntity::class,
        LearningAttemptEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun learnerProfileDao(): LearnerProfileDao
    abstract fun learningAttemptDao(): LearningAttemptDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "scholars-learning.db"
                ).build().also { instance = it }
            }
    }
}
