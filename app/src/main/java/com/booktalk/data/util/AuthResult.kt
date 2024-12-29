package com.booktalk.data.util

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()

    companion object {
        fun <T> success(data: T): AuthResult<T> = Success(data)
        fun <T> error(message: String): AuthResult<T> = Error(message)
        fun <T> error(exception: Exception): AuthResult<T> = Error(exception.message ?: "An error occurred")
    }
}
