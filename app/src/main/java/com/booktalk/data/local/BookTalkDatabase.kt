package com.booktalk.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.booktalk.data.local.converter.DateConverter
import com.booktalk.data.local.converter.ListConverter
import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.local.dao.UserBookDao
import com.booktalk.data.local.entity.BookEntity
import com.booktalk.data.local.entity.UserBookEntity
import com.booktalk.data.local.migrations.Migration1to2
import com.booktalk.data.local.migrations.Migration2to3

@Database(
    entities = [
        BookEntity::class,
        UserBookEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(DateConverter::class, ListConverter::class)
abstract class BookTalkDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun userBookDao(): UserBookDao

    companion object {
        const val DATABASE_NAME = "booktalk_db"

        val MIGRATIONS: Array<Migration> = arrayOf(
            Migration1to2,
            Migration2to3
        )
    }
}
