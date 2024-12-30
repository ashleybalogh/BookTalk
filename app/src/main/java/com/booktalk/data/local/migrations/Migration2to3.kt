package com.booktalk.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from database version 2 to 3.
 * Changes:
 * - Updates the books table schema to match the new BookEntity
 * - Removes unused fields and adds new ones
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
                categories TEXT NOT NULL DEFAULT '[]',
                category TEXT,
                language TEXT,
                averageRating REAL NOT NULL DEFAULT 0,
                ratingsCount INTEGER NOT NULL DEFAULT 0,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)

        // Copy existing data
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
                COALESCE(categories, '[]'),
                category,
                language,
                COALESCE(averageRating, 0),
                COALESCE(ratingsCount, 0),
                createdAt,
                updatedAt
            FROM books
        """)

        // Drop old table and rename temp table
        database.execSQL("DROP TABLE books")
        database.execSQL("ALTER TABLE books_temp RENAME TO books")
    }
}
