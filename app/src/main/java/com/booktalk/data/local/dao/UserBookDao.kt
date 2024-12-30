package com.booktalk.data.local.dao

import androidx.room.*
import com.booktalk.domain.model.book.ReadingStatus
import com.booktalk.data.local.entity.UserBookEntity
import com.booktalk.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBookDao {
    @Query("SELECT * FROM user_books WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getUserBooks(userId: String): Flow<List<UserBookEntity>>

    @Query("SELECT * FROM user_books WHERE userId = :userId AND status = :status ORDER BY updatedAt DESC")
    fun getUserBooksByStatus(userId: String, status: ReadingStatus): Flow<List<UserBookEntity>>

    @Query("SELECT * FROM user_books WHERE userId = :userId AND bookId = :bookId")
    suspend fun getUserBook(userId: String, bookId: String): UserBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserBook(userBook: UserBookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserBooks(userBooks: List<UserBookEntity>)

    @Update
    suspend fun updateUserBook(userBook: UserBookEntity)

    @Delete
    suspend fun deleteUserBook(userBook: UserBookEntity)

    @Query("DELETE FROM user_books WHERE userId = :userId")
    suspend fun deleteUserBooks(userId: String)

    @Query("DELETE FROM user_books")
    suspend fun deleteAllUserBooks()

    @Transaction
    @Query("""
        SELECT * FROM books b
        INNER JOIN user_books ub ON b.id = ub.bookId
        WHERE ub.userId = :userId
        AND ub.status = :status
        ORDER BY ub.updatedAt DESC
    """)
    fun getBooksByStatus(userId: String, status: ReadingStatus): Flow<Map<BookEntity, UserBookEntity>>

    @Query("""
        SELECT * FROM user_books 
        WHERE userId = :userId 
        AND startDate IS NOT NULL 
        AND finishDate IS NULL
        ORDER BY 
            CASE WHEN lastReadDate IS NULL THEN 1 ELSE 0 END,
            lastReadDate DESC,
            startDate DESC
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
        if (userBook != null) {
            val updatedBook = userBook.copy(
                currentPage = currentPage,
                totalPages = totalPages,
                lastReadDate = System.currentTimeMillis(),
                status = if (currentPage >= totalPages) ReadingStatus.READ else ReadingStatus.CURRENTLY_READING,
                finishDate = if (currentPage >= totalPages) System.currentTimeMillis() else null,
                updatedAt = System.currentTimeMillis()
            )
            updateUserBook(updatedBook)
        }
    }
}
