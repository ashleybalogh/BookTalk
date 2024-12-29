package com.booktalk.domain.model.auth

data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null,
    val emailVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)
