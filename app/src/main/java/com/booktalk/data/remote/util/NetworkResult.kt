package com.booktalk.data.remote.util

import com.booktalk.domain.util.NetworkResult
import retrofit2.Response

/**
 * Extension functions for working with NetworkResult and Retrofit responses
 */

/**
 * Converts a Retrofit Response to a NetworkResult
 */
fun <T> Response<T>.toNetworkResult(): NetworkResult<T> {
    return try {
        if (isSuccessful && body() != null) {
            NetworkResult.success(body()!!)
        } else {
            NetworkResult.error(
                error = ApiException(code(), message()),
                message = message()
            )
        }
    } catch (e: Exception) {
        NetworkResult.error(
            error = e,
            message = e.message
        )
    }
}

/**
 * Converts an Exception to a NetworkResult
 */
fun <T> Exception.toNetworkResult(): NetworkResult<T> {
    return NetworkResult.error(
        error = this,
        message = message
    )
}

/**
 * Safely executes a network call and returns a NetworkResult
 */
suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        apiCall().toNetworkResult()
    } catch (e: Exception) {
        e.toNetworkResult()
    }
}
