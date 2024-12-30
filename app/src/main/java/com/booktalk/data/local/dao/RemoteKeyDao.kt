package com.booktalk.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.booktalk.data.local.entity.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE bookId = :bookId")
    suspend fun remoteKeysBookId(bookId: String): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @Query("DELETE FROM remote_keys WHERE query = :query")
    suspend fun clearRemoteKeysByQuery(query: String)

    @Query("DELETE FROM remote_keys WHERE category = :category")
    suspend fun clearRemoteKeysByCategory(category: String)
}
