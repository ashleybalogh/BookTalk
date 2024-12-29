package com.booktalk.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "user_books",
    primaryKeys = ["userId", "bookId"],
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bookId")]
)
data class UserBookEntity(
    val userId: String,
    val bookId: String,
    val status: ReadingStatus,
    val currentPage: Int = 0,
    val totalPages: Int,
    val startDate: Long? = null,
    val finishDate: Long? = null,
    val rating: Float? = null,
    val review: String? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
