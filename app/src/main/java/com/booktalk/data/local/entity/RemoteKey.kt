package com.booktalk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val bookId: String,
    val prevKey: Int?,
    val nextKey: Int?,
    val query: String?,
    val category: String?
)
