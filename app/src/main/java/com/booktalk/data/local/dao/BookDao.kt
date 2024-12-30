package com.booktalk.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.booktalk.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooksFlow(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooksPaging(): PagingSource<Int, BookEntity>

    @Query("""
        SELECT * FROM books 
        WHERE title LIKE '%' || :query || '%' 
        OR authors LIKE '%' || :query || '%'
        OR isbn LIKE '%' || :query || '%'
        ORDER BY 
            CASE 
                WHEN title LIKE :query || '%' THEN 1
                WHEN authors LIKE :query || '%' THEN 2
                ELSE 3
            END,
            CASE WHEN averageRating IS NULL THEN 0 ELSE averageRating END DESC,
            ratingsCount DESC
    """)
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("""
        SELECT * FROM books 
        WHERE title LIKE '%' || :query || '%' 
        OR authors LIKE '%' || :query || '%'
        OR isbn LIKE '%' || :query || '%'
        ORDER BY 
            CASE 
                WHEN title LIKE :query || '%' THEN 1
                WHEN authors LIKE :query || '%' THEN 2
                ELSE 3
            END,
            CASE WHEN averageRating IS NULL THEN 0 ELSE averageRating END DESC,
            ratingsCount DESC
    """)
    fun searchBooksPaging(query: String): PagingSource<Int, BookEntity>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?

    @Query("SELECT * FROM books WHERE isbn = :isbn")
    suspend fun getBookByIsbn(isbn: String): BookEntity?

    @Query("SELECT * FROM books WHERE categories LIKE '%' || :category || '%' ORDER BY averageRating DESC, ratingsCount DESC")
    fun getBooksByCategory(category: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE categories LIKE '%' || :category || '%' ORDER BY averageRating DESC, ratingsCount DESC")
    fun getBooksByCategoryPaging(category: String): PagingSource<Int, BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()

    @Query("DELETE FROM books WHERE title LIKE '%' || :query || '%' OR authors LIKE '%' || :query || '%'")
    suspend fun deleteBooksByQuery(query: String)

    @Query("DELETE FROM books WHERE categories LIKE '%' || :category || '%'")
    suspend fun deleteBooksByCategory(category: String)
}
