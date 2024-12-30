package com.booktalk.domain.util

sealed class NetworkResult<T> {
    class Success<T>(val data: T) : NetworkResult<T>()
    class Error<T>(val message: String) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()

    fun onSuccess(action: (T) -> Unit): NetworkResult<T> {
        if (this is Success) action(data)
        return this
    }

    fun onError(action: (String) -> Unit): NetworkResult<T> {
        if (this is Error) action(message)
        return this
    }

    fun onLoading(action: () -> Unit): NetworkResult<T> {
        if (this is Loading) action()
        return this
    }

    companion object {
        fun <T> success(data: T): NetworkResult<T> = Success(data)
        
        fun <T> error(message: String): NetworkResult<T> = Error(message)
        
        fun <T> loading(): NetworkResult<T> = Loading()
    }
}
