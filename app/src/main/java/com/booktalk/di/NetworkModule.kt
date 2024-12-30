package com.booktalk.di

import android.content.Context
import com.booktalk.BuildConfig
import com.booktalk.data.local.TokenManager
import com.booktalk.data.remote.api.BookService
import com.booktalk.data.remote.api.AuthApiService
import com.booktalk.data.remote.interceptor.AuthInterceptor
import com.booktalk.data.remote.interceptor.CacheInterceptor
import com.booktalk.data.remote.interceptor.LoggingInterceptor
import com.booktalk.data.remote.interceptor.NetworkInterceptor
import com.booktalk.data.remote.interceptor.RateLimitInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheDir = File(context.cacheDir, "http_cache")
        val cacheSize = 50L * 1024L * 1024L // 50 MB
        return Cache(cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            .add(BuildConfig.API_HOST, *BuildConfig.SSL_PINS)
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(@ApplicationContext context: Context): NetworkInterceptor {
        return NetworkInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideRateLimitInterceptor(): RateLimitInterceptor {
        return RateLimitInterceptor()
    }

    @Provides
    @Singleton
    fun provideCacheInterceptor(@ApplicationContext context: Context): CacheInterceptor {
        return CacheInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor()
    }

    @Provides
    @Singleton
    @AuthOkHttpClient
    fun provideAuthOkHttpClient(
        cache: Cache,
        certificatePinner: CertificatePinner,
        networkInterceptor: NetworkInterceptor,
        rateLimitInterceptor: RateLimitInterceptor,
        cacheInterceptor: CacheInterceptor,
        authInterceptor: AuthInterceptor,
        loggingInterceptor: LoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .certificatePinner(certificatePinner)
            .addInterceptor(networkInterceptor)
            .addInterceptor(rateLimitInterceptor)
            .addInterceptor(cacheInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkInterceptor: NetworkInterceptor,
        rateLimitInterceptor: RateLimitInterceptor,
        loggingInterceptor: LoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .addInterceptor(rateLimitInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBookService(retrofit: Retrofit): BookService {
        return retrofit.create(BookService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}
