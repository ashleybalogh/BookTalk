package com.booktalk.core.di

import com.booktalk.data.source.BookDataSource
import com.booktalk.data.source.local.LocalBookDataSource
import com.booktalk.data.source.remote.RemoteBookDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    @Named("local")
    abstract fun bindLocalDataSource(
        localBookDataSource: LocalBookDataSource
    ): BookDataSource

    @Binds
    @Singleton
    @Named("remote")
    abstract fun bindRemoteDataSource(
        remoteBookDataSource: RemoteBookDataSource
    ): BookDataSource
}
