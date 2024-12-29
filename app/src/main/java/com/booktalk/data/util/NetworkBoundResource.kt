package com.booktalk.data.util

import com.booktalk.domain.util.NetworkResult
import kotlinx.coroutines.flow.*

/**
 * A generic class that can provide a resource backed by both the local database and the network.
 * It follows the offline-first principle where data is always served from the local database,
 * and network requests are used to keep the local data fresh.
 */
abstract class NetworkBoundResource<ResultType> {

    fun asFlow() = flow<NetworkResult<ResultType>> {
        // Start by emitting the loading state
        emit(NetworkResult.loading())

        // First, load from local database
        val localData = loadFromDb().firstOrNull()
        if (localData != null) {
            emit(NetworkResult.success(localData))
        }

        // Check if we should fetch from network
        if (shouldFetch(localData)) {
            try {
                // Fetch from network
                val networkResult = fetchFromNetwork()
                // Save the result
                saveNetworkResult(networkResult)
                // Emit the new data from local database
                emitAll(loadFromDb().map { NetworkResult.success(it) })
            } catch (e: Exception) {
                // On error, emit the error if we don't have local data
                if (localData == null) {
                    emit(NetworkResult.error(e, e.message))
                }
                // If we have local data, keep emitting it despite the network error
                else {
                    emitAll(loadFromDb().map { NetworkResult.success(it) })
                }
            }
        } else {
            // If we shouldn't fetch, just keep emitting local data
            emitAll(loadFromDb().map { NetworkResult.success(it) })
        }
    }

    /**
     * Returns true if the data should be fetched from the network.
     * This can be based on various factors like:
     * - Data staleness
     * - Network availability
     * - User preferences
     */
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    /**
     * Loads data from the local database.
     */
    protected abstract fun loadFromDb(): Flow<ResultType>

    /**
     * Fetches data from the network.
     */
    protected abstract suspend fun fetchFromNetwork(): ResultType

    /**
     * Saves the network result to the local database.
     */
    protected abstract suspend fun saveNetworkResult(result: ResultType)

    companion object {
        const val DEFAULT_CACHE_TIMEOUT_HOURS = 24L
    }
}

/**
 * Helper class to manage cache timestamps
 */
class CacheManager {
    private val timestamps = mutableMapOf<String, Long>()

    fun shouldRefresh(key: String, timeoutHours: Long = NetworkBoundResource.DEFAULT_CACHE_TIMEOUT_HOURS): Boolean {
        val lastUpdate = timestamps[key] ?: return true
        val currentTime = System.currentTimeMillis()
        val timeoutMillis = timeoutHours * 60 * 60 * 1000
        return currentTime - lastUpdate > timeoutMillis
    }

    fun updateTimestamp(key: String) {
        timestamps[key] = System.currentTimeMillis()
    }

    fun clearTimestamp(key: String) {
        timestamps.remove(key)
    }

    fun clearAll() {
        timestamps.clear()
    }
}
