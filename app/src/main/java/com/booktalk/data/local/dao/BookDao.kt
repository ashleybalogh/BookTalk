package com.booktalk.data.local.dao

import androidx.room.*
import com.booktalk.data.local.entity.BookEntity
import com.booktalk.domain.model.book.ReadingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM books")
    fun getAllBooksFlow(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("""
        SELECT * FROM books 
        WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%' 
        OR EXISTS (
            SELECT 1 FROM json_each(authors) 
            WHERE LOWER(value) LIKE '%' || LOWER(:query) || '%'
        )
        OR LOWER(description) LIKE '%' || LOWER(:query) || '%'
        OR EXISTS (
            SELECT 1 FROM json_each(keywords) 
            WHERE LOWER(value) LIKE '%' || LOWER(:query) || '%'
        )
        ORDER BY 
            CASE 
                WHEN LOWER(title) LIKE LOWER(:query) || '%' THEN 1
                WHEN EXISTS (
                    SELECT 1 FROM json_each(authors) 
                    WHERE LOWER(value) LIKE LOWER(:query) || '%'
                ) THEN 2
                ELSE 3
            END,
            popularity DESC,
            averageRating DESC
    """)
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("""
        SELECT * FROM books 
        WHERE EXISTS (
            SELECT 1 FROM json_each(categories) 
            WHERE value = :category
        )
        ORDER BY popularity DESC, averageRating DESC
    """)
    fun getBooksByCategory(category: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE status = :status")
    fun getBooksByStatus(status: ReadingStatus): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE status IN (:statuses)")
    fun getBooksByStatuses(statuses: List<ReadingStatus>): Flow<List<BookEntity>>

    @Query("SELECT COUNT(*) FROM books WHERE status = :status")
    fun getBookCountByStatus(status: ReadingStatus): Flow<Int>

    @Query("""
        SELECT * FROM books
        WHERE popularity > 0
        ORDER BY popularity DESC, averageRating DESC
        LIMIT 20
    """)
    fun getPopularBooks(): Flow<List<BookEntity>>

    @Query("""
        SELECT * FROM books
        WHERE id IN (
            SELECT json_each.value
            FROM books, json_each(books.similarBooks)
            WHERE books.id = :bookId
        )
    """)
    fun getSimilarBooks(bookId: String): Flow<List<BookEntity>>

    @Query("""
        SELECT * FROM books
        WHERE id IN (
            SELECT DISTINCT b.id
            FROM books b
            INNER JOIN (
                SELECT category
                FROM (
                    SELECT json_each.value as category
                    FROM books, json_each(books.categories)
                    WHERE books.id = :userId
                )
            ) user_categories
            INNER JOIN (
                SELECT b2.id, json_each.value as category
                FROM books b2, json_each(b2.categories)
            ) book_categories
            ON user_categories.category = book_categories.category
            WHERE b.id = book_categories.id
            AND b.id != :userId
        )
        ORDER BY popularity DESC, averageRating DESC
        LIMIT 20
    """)
    fun getRecommendedBooks(userId: String): Flow<List<BookEntity>>
}
