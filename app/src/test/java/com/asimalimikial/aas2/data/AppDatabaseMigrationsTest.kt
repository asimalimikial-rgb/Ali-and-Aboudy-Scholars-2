package com.asimalimikial.aas2.data

import androidx.sqlite.db.SupportSQLiteDatabase
import java.lang.reflect.Proxy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppDatabaseMigrationsTest {
    @Test
    fun migrationAddsOnlyNullableSnapshotColumnsWithoutDroppingLegacyRows() {
        val executedSql = mutableListOf<String>()
        val database = Proxy.newProxyInstance(
            SupportSQLiteDatabase::class.java.classLoader,
            arrayOf(SupportSQLiteDatabase::class.java)
        ) { _, method, args ->
            if (method.name == "execSQL" && args?.firstOrNull() is String) {
                executedSql += args[0] as String
            }
            null
        } as SupportSQLiteDatabase

        assertEquals(1, MIGRATION_1_2.startVersion)
        assertEquals(2, MIGRATION_1_2.endVersion)

        MIGRATION_1_2.migrate(database)

        assertEquals(
            listOf(
                "ALTER TABLE `learning_attempts` ADD COLUMN `contentPackId` TEXT",
                "ALTER TABLE `learning_attempts` ADD COLUMN `contentPackVersion` TEXT",
                "ALTER TABLE `learning_attempts` ADD COLUMN `selectedOptionId` TEXT",
                "ALTER TABLE `learning_attempts` ADD COLUMN `feedbackText` TEXT"
            ),
            executedSql
        )
        assertTrue(executedSql.all { it.startsWith("ALTER TABLE") })
        assertTrue(executedSql.all { it.endsWith(" TEXT") })
        assertTrue(executedSql.none { it.contains("DROP TABLE", ignoreCase = true) })
    }
}
