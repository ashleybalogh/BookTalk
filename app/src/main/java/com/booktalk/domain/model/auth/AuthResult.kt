package com.booktalk.domain.model.auth

sealed class AuthResult<out T> {
    data class Success<T>(
        val data: T
    ) : AuthResult<T>()
    
    data class Error(
        val message: String,
        val code: Int? = null
    ) : AuthResult<Nothing>()
    
    data object Loading : AuthResult<Nothing>()
}
