package com.booktalk.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from database version 2 to 3.
 * Changes:
 * - Adds discovery-related fields to books table:
 *   - popularity (Float)
 *   - maturityRating (TEXT)
 *   - keywords (TEXT - JSON array)
 *   - similarBooks (TEXT - JSON array)
 */
object Migration2to3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create temporary table with new schema
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS books_temp (
                id TEXT NOT NULL PRIMARY KEY,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                description TEXT,
                coverUrl TEXT,
                isbn TEXT,
                pageCount INTEGER,
                publishedDate TEXT,
                publisher TEXT,
                categories TEXT NOT NULL,
                category TEXT,
                language TEXT,
                maturityRating TEXT,
                averageRating REAL NOT NULL,
                ratingsCount INTEGER NOT NULL,
                popularity REAL NOT NULL,
                keywords TEXT NOT NULL,
                similarBooks TEXT NOT NULL,
                readingStatus TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)

        // Copy existing data with default values for new columns
        database.execSQL("""
            INSERT INTO books_temp
            SELECT 
                id,
                title,
                author,
                description,
                coverUrl,
                isbn,
                pageCount,
                publishedDate,
                publisher,
                categories,
                category,
                language,
                NULL as maturityRating,
                averageRating,
                ratingsCount,
                averageRating as popularity,
                '[]' as keywords,
                '[]' as similarBooks,
                readingStatus,
                createdAt,
                updatedAt
            FROM books
        """)

        // Drop the old table
        database.execSQL("DROP TABLE books")

        // Rename the new table
        database.execSQL("ALTER TABLE books_temp RENAME TO books")

        // Create indices for improved query performance
        database.execSQL("CREATE INDEX IF NOT EXISTS index_books_popularity ON books(popularity)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_books_averageRating ON books(averageRating)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_books_category ON books(category)")
    }
}
