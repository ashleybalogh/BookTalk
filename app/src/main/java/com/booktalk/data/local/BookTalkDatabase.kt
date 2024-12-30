package com.booktalk.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.booktalk.data.local.converter.Converters
import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.local.dao.RemoteKeyDao
import com.booktalk.data.local.dao.UserBookDao
import com.booktalk.data.local.entity.BookEntity
import com.booktalk.data.local.entity.RemoteKey
import com.booktalk.data.local.entity.UserBookEntity

@Database(
    entities = [
        BookEntity::class,
        UserBookEntity::class,
        RemoteKey::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class BookTalkDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun userBookDao(): UserBookDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val DATABASE_NAME = "booktalk.db"
    }
}
