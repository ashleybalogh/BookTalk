package com.booktalk.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from database version 1 to 2.
 * Changes:
 * - Updates the ReadingStatus enum values in user_books table
 * - Converts old status values to new ones
 */
object Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create temporary table with new schema
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS user_books_temp (
                userId TEXT NOT NULL,
                bookId TEXT NOT NULL,
                status TEXT NOT NULL,
                currentPage INTEGER NOT NULL DEFAULT 0,
                totalPages INTEGER NOT NULL DEFAULT 0,
                startDate INTEGER,
                finishDate INTEGER,
                rating REAL,
                review TEXT,
                notes TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                PRIMARY KEY(userId, bookId),
                FOREIGN KEY(bookId) REFERENCES books(id) ON DELETE CASCADE
            )
        """)

        // Copy data with status mapping
        database.execSQL("""
            INSERT INTO user_books_temp
            SELECT 
                userId,
                bookId,
                CASE status
                    WHEN 'NOT_STARTED' THEN 'WANT_TO_READ'
                    WHEN 'IN_PROGRESS' THEN 'CURRENTLY_READING'
                    WHEN 'CURRENTLY_READING' THEN 'CURRENTLY_READING'
                    WHEN 'FINISHED' THEN 'READ'
                    WHEN 'READ' THEN 'READ'
                    WHEN 'DNF' THEN 'DID_NOT_FINISH'
                    ELSE 'WANT_TO_READ'
                END as status,
                COALESCE(currentPage, 0),
                COALESCE(totalPages, 0),
                startDate,
                finishDate,
                rating,
                review,
                notes,
                createdAt,
                updatedAt
            FROM user_books
        """)

        // Drop old table and rename temp table
        database.execSQL("DROP TABLE user_books")
        database.execSQL("ALTER TABLE user_books_temp RENAME TO user_books")
    }
}
