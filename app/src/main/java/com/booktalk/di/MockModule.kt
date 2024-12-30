package com.booktalk.di

import com.booktalk.BuildConfig
import com.booktalk.data.repository.mock.MockAuthRepository
import com.booktalk.data.repository.mock.MockBookRepository
import com.booktalk.domain.repository.AuthRepository
import com.booktalk.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Mock

@Module
@InstallIn(SingletonComponent::class)
object MockModule {
    @Mock
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return if (BuildConfig.DEBUG) {
            MockAuthRepository()
        } else {
            throw IllegalStateException("MockModule should not be used in release builds")
        }
    }

    @Mock
    @Provides
    @Singleton
    fun provideBookRepository(): BookRepository {
        return if (BuildConfig.DEBUG) {
            MockBookRepository()
        } else {
            throw IllegalStateException("MockModule should not be used in release builds")
        }
    }
}
