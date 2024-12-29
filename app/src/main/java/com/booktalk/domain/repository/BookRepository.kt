package com.booktalk.domain.repository

import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus
import com.booktalk.domain.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    // Book operations with network results
    suspend fun getAllBooks(): Flow<NetworkResult<List<Book>>>
    suspend fun searchBooks(query: String): Flow<NetworkResult<List<Book>>>
    suspend fun getBookById(bookId: String): Flow<NetworkResult<Book>>
    suspend fun getBookByIsbn(isbn: String): Flow<NetworkResult<Book>>
    suspend fun getBooksByCategory(category: String): Flow<NetworkResult<List<Book>>>
    
    // Local operations
    suspend fun insertBook(book: Book)
    suspend fun insertBooks(books: List<Book>)
    suspend fun updateBook(book: Book)
    suspend fun deleteBook(book: Book)
    
    // User book operations
    fun getUserBooks(userId: String): Flow<List<Book>>
    fun getUserBooksByStatus(userId: String, status: ReadingStatus): Flow<List<Book>>
    suspend fun getUserBook(userId: String, bookId: String): Book?
    suspend fun insertUserBook(userId: String, book: Book, status: ReadingStatus)
    suspend fun updateUserBook(userId: String, book: Book)
    suspend fun deleteUserBook(userId: String, bookId: String)
    
    // Reading status operations
    fun getCurrentlyReading(userId: String): Flow<List<Book>>
    fun getFinishedBooks(userId: String): Flow<List<Book>>
    suspend fun getReadingList(): Flow<NetworkResult<List<Book>>>
    suspend fun updateReadingProgress(userId: String, bookId: String, currentPage: Int, totalPages: Int)
    
    // Cache operations
    suspend fun clearCache()
}
