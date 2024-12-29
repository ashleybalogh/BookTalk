package com.booktalk.data.remote.interceptor

import com.booktalk.data.remote.exception.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import kotlin.math.min
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateLimitInterceptor @Inject constructor(
    private val maxRequestsPerSecond: Int = 5
) : Interceptor {
    private var tokens = maxRequestsPerSecond.toDouble()
    private var lastRefillTime = System.nanoTime()
    private val refillRate = maxRequestsPerSecond.toDouble()
    private val bucketSize = maxRequestsPerSecond.toDouble()

    @Synchronized
    private fun refillTokens() {
        val now = System.nanoTime()
        val timePassed = now - lastRefillTime
        val refill = (timePassed / TimeUnit.SECONDS.toNanos(1).toDouble()) * refillRate
        tokens = min(bucketSize, tokens + refill)
        lastRefillTime = now
    }

    @Synchronized
    private fun consumeToken(): Boolean {
        refillTokens()
        return if (tokens >= 1.0) {
            tokens -= 1.0
            true
        } else {
            false
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // Check if this is a priority request
        val isPriority = chain.request().header("X-Priority") != null
        
        // Priority requests bypass rate limiting
        if (!isPriority) {
            while (!consumeToken()) {
                Thread.sleep(100) // Wait for token to be available
            }
        }

        val response = chain.proceed(chain.request())
        
        // Handle rate limit response
        if (response.code == 429) {
            val retryAfter = response.header("Retry-After")?.toLongOrNull()
            if (retryAfter != null) {
                // Adjust tokens based on retry after
                tokens = 0.0
                lastRefillTime = System.nanoTime() + TimeUnit.SECONDS.toNanos(retryAfter)
            }
        }
        
        return response
    }
}
