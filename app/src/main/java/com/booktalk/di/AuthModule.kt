package com.booktalk.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.booktalk.data.local.secure.SecureStorage
import com.booktalk.data.remote.api.AuthApiService
import com.booktalk.data.repository.AuthRepositoryImpl
import com.booktalk.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient
import com.booktalk.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    
    @Provides
    @Singleton
    fun provideSecureStorage(@ApplicationContext context: Context): SecureStorage {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return SecureStorage(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthRetrofit(@AuthOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        secureStorage: SecureStorage
    ): AuthRepository {
        return AuthRepositoryImpl(authApiService, secureStorage)
    }
}
