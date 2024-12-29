package com.booktalk.data.remote.exception

data class ApiException(
    val code: Int = 0,
    override val message: String = "Unknown error",
    val errorBody: String? = null
) : Exception(message)

sealed class ApiExceptions(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkError(message: String? = null, cause: Throwable? = null) : ApiExceptions(message, cause)
    class ServerError(message: String? = null, cause: Throwable? = null) : ApiExceptions(message, cause)
    class ClientError(message: String? = null, cause: Throwable? = null) : ApiExceptions(message, cause)
    class UnknownError(message: String? = null, cause: Throwable? = null) : ApiExceptions(message, cause)
}
