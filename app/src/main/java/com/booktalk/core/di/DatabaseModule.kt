package com.booktalk.core.di

import android.content.Context
import androidx.room.Room
import com.booktalk.data.local.BookTalkDatabase
import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.local.dao.UserBookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BookTalkDatabase {
        return Room.databaseBuilder(
            context,
            BookTalkDatabase::class.java,
            BookTalkDatabase.DATABASE_NAME
        )
        .addMigrations(*BookTalkDatabase.MIGRATIONS)
        .build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: BookTalkDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideUserBookDao(database: BookTalkDatabase): UserBookDao {
        return database.userBookDao()
    }
}
