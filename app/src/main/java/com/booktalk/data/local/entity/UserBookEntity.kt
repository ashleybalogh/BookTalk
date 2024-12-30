package com.booktalk.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.booktalk.domain.model.book.ReadingStatus

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
    indices = [
        Index(value = ["bookId"]),
        Index(value = ["userId", "status"])
    ]
)
data class UserBookEntity(
    val userId: String,
    val bookId: String,
    val status: ReadingStatus = ReadingStatus.NONE,
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val startDate: Long? = null,
    val finishDate: Long? = null,
    val lastReadDate: Long? = null,
    val notes: String? = null,
    val rating: Float? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
