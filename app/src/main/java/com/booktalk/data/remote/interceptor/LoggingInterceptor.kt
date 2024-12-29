package com.booktalk.data.remote.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingInterceptor @Inject constructor() : Interceptor {
    companion object {
        private const val TAG = "BookTalkAPI"
        private const val MAX_BODY_LENGTH = 1000 // Truncate long response bodies
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestStartTime = System.nanoTime()

        // Log request
        Log.d(TAG, "Request: ${request.method} ${request.url}")
        request.headers.forEach { (name, value) ->
            Log.d(TAG, "Header: $name: $value")
        }

        val response = chain.proceed(request)
        val requestDuration = (System.nanoTime() - requestStartTime) / 1e6 // Convert to milliseconds

        // Log response
        Log.d(TAG, "Response: ${response.code} for ${request.url}")
        Log.d(TAG, "Duration: ${requestDuration}ms")
        
        response.headers.forEach { (name, value) ->
            Log.d(TAG, "Header: $name: $value")
        }

        // Log response body for non-binary content types
        val contentType = response.body?.contentType()?.toString()
        if (contentType?.contains("json") == true || contentType?.contains("text") == true) {
            val bodyString = response.peekBody(MAX_BODY_LENGTH.toLong()).string()
            Log.d(TAG, "Body: ${bodyString.take(MAX_BODY_LENGTH)}")
        }

        return response
    }
}
