package com.booktalk.domain.model

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val exception: Exception) : AuthResult<Nothing>()
    
    companion object {
        fun <T> success(data: T): AuthResult<T> = Success(data)
        fun <T> error(exception: Exception): AuthResult<T> = Error(exception)
    }
}
