package com.booktalk.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.booktalk.domain.model.book.ReadingStatus

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
                currentPage INTEGER NOT NULL,
                totalPages INTEGER NOT NULL,
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
                    WHEN 'IN_PROGRESS' THEN 'IN_PROGRESS'
                    WHEN 'CURRENTLY_READING' THEN 'IN_PROGRESS'
                    WHEN 'FINISHED' THEN 'FINISHED'
                    WHEN 'READ' THEN 'FINISHED'
                    WHEN 'DNF' THEN 'DNF'
                    ELSE 'WANT_TO_READ'
                END as status,
                currentPage,
                totalPages,
                startDate,
                finishDate,
                rating,
                review,
                notes,
                createdAt,
                updatedAt
            FROM user_books
        """)

        // Drop old table
        database.execSQL("DROP TABLE user_books")

        // Rename temp table to final table
        database.execSQL("ALTER TABLE user_books_temp RENAME TO user_books")

        // Recreate the index
        database.execSQL("CREATE INDEX IF NOT EXISTS index_user_books_bookId ON user_books(bookId)")
    }
}
