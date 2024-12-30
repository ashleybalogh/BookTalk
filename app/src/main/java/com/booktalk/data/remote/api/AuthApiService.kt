package com.booktalk.data.remote.api

import com.booktalk.data.remote.dto.auth.AuthResponse
import com.booktalk.data.remote.dto.auth.LoginRequest
import com.booktalk.data.remote.dto.auth.RegisterRequest
import com.booktalk.data.remote.dto.auth.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String): Response<AuthResponse>

    @GET("auth/user")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<UserResponse>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    @POST("auth/password/reset")
    suspend fun resetPassword(@Body email: String): Response<Unit>

    @POST("auth/password/update")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body oldPassword: String,
        @Body newPassword: String
    ): Response<Unit>

    @POST("auth/password/verify")
    suspend fun verifyPasswordResetCode(@Body code: String): Response<Unit>

    @PUT("auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body displayName: String?,
        @Body photoUrl: String?
    ): Response<UserResponse>

    @PUT("auth/email")
    suspend fun updateEmail(
        @Header("Authorization") token: String,
        @Body newEmail: String,
        @Body password: String
    ): Response<Unit>

    @POST("auth/email/verify")
    suspend fun sendEmailVerification(@Header("Authorization") token: String): Response<Unit>

    @DELETE("auth/account")
    suspend fun deleteAccount(@Header("Authorization") token: String): Response<Unit>
}
