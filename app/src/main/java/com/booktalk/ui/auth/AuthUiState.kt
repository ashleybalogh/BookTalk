package com.booktalk.ui.auth

import com.booktalk.domain.model.auth.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val displayName: String = "",
    val error: String? = null,
    val message: String? = null
)
