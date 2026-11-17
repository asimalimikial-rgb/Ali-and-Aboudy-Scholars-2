package com.asimalimikial.aas2.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Additive migration: legacy attempts remain intact and receive null snapshot
 * fields because their original answer/content metadata was not persisted.
 */
internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `learning_attempts` ADD COLUMN `contentPackId` TEXT")
        db.execSQL("ALTER TABLE `learning_attempts` ADD COLUMN `contentPackVersion` TEXT")
        db.execSQL("ALTER TABLE `learning_attempts` ADD COLUMN `selectedOptionId` TEXT")
        db.execSQL("ALTER TABLE `learning_attempts` ADD COLUMN `feedbackText` TEXT")
    }
}
