package com.booktalk.data.remote.util

/**
 * Exception class for API errors
 */
class ApiException(
    val code: Int,
    override val message: String?
) : Exception(message)
