package com.booktalk.data.local.dao

import androidx.room.*
import com.booktalk.data.local.entity.ReadingStatus
import com.booktalk.data.local.entity.UserBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBookDao {
    @Query("SELECT * FROM user_books WHERE userId = :userId")
    fun getUserBooks(userId: String): Flow<List<UserBookEntity>>

    @Query("SELECT * FROM user_books WHERE userId = :userId AND status = :status")
    fun getUserBooksByStatus(userId: String, status: ReadingStatus): Flow<List<UserBookEntity>>

    @Query("SELECT * FROM user_books WHERE userId = :userId AND bookId = :bookId")
    suspend fun getUserBook(userId: String, bookId: String): UserBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserBook(userBook: UserBookEntity)

    @Update
    suspend fun updateUserBook(userBook: UserBookEntity)

    @Delete
    suspend fun deleteUserBook(userBook: UserBookEntity)

    @Query("""
        SELECT * FROM user_books 
        WHERE userId = :userId 
        AND startDate IS NOT NULL 
        AND finishDate IS NULL
        ORDER BY startDate DESC
    """)
    fun getCurrentlyReading(userId: String): Flow<List<UserBookEntity>>

    @Query("""
        SELECT * FROM user_books 
        WHERE userId = :userId 
        AND finishDate IS NOT NULL
        ORDER BY finishDate DESC
    """)
    fun getFinishedBooks(userId: String): Flow<List<UserBookEntity>>

    @Transaction
    suspend fun updateReadingProgress(
        userId: String,
        bookId: String,
        currentPage: Int,
        totalPages: Int
    ) {
        val userBook = getUserBook(userId, bookId)
        userBook?.let {
            val updated = it.copy(
                currentPage = currentPage,
                totalPages = totalPages,
                updatedAt = System.currentTimeMillis()
            )
            updateUserBook(updated)
        }
    }
}
