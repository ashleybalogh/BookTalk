package com.booktalk.data.remote.dto.auth

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val displayName: String
)

data class AuthResponse(
    val userId: String,
    val token: String,
    val refreshToken: String,
    val expiresIn: Long
)

data class UserResponse(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?,
    val emailVerified: Boolean,
    val createdAt: Long,
    val lastLoginAt: Long
)
