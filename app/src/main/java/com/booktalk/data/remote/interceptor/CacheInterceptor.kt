package com.booktalk.data.remote.interceptor

import android.content.Context
import com.booktalk.data.remote.exception.NetworkExceptions.NoNetworkException
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        
        if (!isNetworkAvailable(context)) {
            throw NoNetworkException()
        }

        // Don't cache POST/PUT/DELETE requests
        if (request.method != "GET") {
            return chain.proceed(request)
        }
        
        val cacheControl = request.header("Cache-Control")
        
        // If the request specifies its own cache control, respect it
        if (!cacheControl.isNullOrEmpty()) {
            return chain.proceed(request)
        }
        
        // Default cache settings
        val cacheControlBuilder = CacheControl.Builder().apply {
            maxAge(1, TimeUnit.HOURS) // Cache for 1 hour
            maxStale(1, TimeUnit.DAYS) // Accept stale data up to 1 day old when offline
        }
        
        val newRequest = request.newBuilder()
            .cacheControl(cacheControlBuilder.build())
            .build()
            
        return chain.proceed(newRequest)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
