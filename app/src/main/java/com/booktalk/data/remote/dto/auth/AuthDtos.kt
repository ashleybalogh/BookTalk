package com.booktalk.data.remote.dto.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "token") val token: String,
    @Json(name = "refreshToken") val refreshToken: String,
    @Json(name = "userId") val userId: String,
    @Json(name = "expiresIn") val expiresIn: Long
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "displayName") val displayName: String
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id") val id: String,
    @Json(name = "email") val email: String,
    @Json(name = "displayName") val displayName: String,
    @Json(name = "photoUrl") val photoUrl: String?,
    @Json(name = "emailVerified") val emailVerified: Boolean = false,
    @Json(name = "createdAt") val createdAt: Long = System.currentTimeMillis(),
    @Json(name = "lastLoginAt") val lastLoginAt: Long = System.currentTimeMillis()
)
