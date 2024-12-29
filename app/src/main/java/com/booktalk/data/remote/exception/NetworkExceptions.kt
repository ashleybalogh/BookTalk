package com.booktalk.data.remote.exception

import java.io.IOException

sealed class NetworkExceptions(message: String? = null, cause: Throwable? = null) : IOException(message, cause) {
    data class NoNetworkException(override val message: String = "No network connection") : NetworkExceptions(message)
    data class RateLimitException(override val message: String = "Rate limit exceeded") : NetworkExceptions(message)
    data class UnauthorizedException(override val message: String = "Unauthorized") : NetworkExceptions(message)
    data class ClientException(override val message: String = "Client error") : NetworkExceptions(message)
    data class UnknownException(override val message: String = "Unknown error") : NetworkExceptions(message)
}
