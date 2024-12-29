package com.booktalk.di

import com.booktalk.mock.ApiServer
import com.booktalk.mock.NoOpApiServer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    @Singleton
    abstract fun bindApiServer(apiServer: NoOpApiServer): ApiServer
}
