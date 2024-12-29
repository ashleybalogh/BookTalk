package com.booktalk.domain.util

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(
        val error: Throwable? = null,
        val message: String? = null
    ) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()

    fun onSuccess(action: (T) -> Unit): NetworkResult<T> {
        if (this is Success) action(data)
        return this
    }

    fun onError(action: (String?) -> Unit): NetworkResult<T> {
        if (this is Error) action(message)
        return this
    }

    fun onLoading(action: () -> Unit): NetworkResult<T> {
        if (this is Loading) action()
        return this
    }

    companion object {
        fun <T> success(data: T): NetworkResult<T> = Success(data)
        
        fun error(
            error: Throwable? = null,
            message: String? = null
        ): NetworkResult<Nothing> = Error(error, message)
        
        fun <T> loading(): NetworkResult<T> = Loading
    }
}
