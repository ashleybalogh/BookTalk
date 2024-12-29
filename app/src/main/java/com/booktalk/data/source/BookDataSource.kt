package com.booktalk.data.source

import com.booktalk.domain.model.book.Book
import com.booktalk.domain.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface BookDataSource {
    suspend fun searchBooks(query: String): List<Book>
    suspend fun getBookById(id: String): Book
    suspend fun getBooksByCategory(category: String): List<Book>
    suspend fun getRecommendedBooks(userId: String): List<Book>
    suspend fun saveBooks(books: List<Book>)
    suspend fun saveBook(book: Book)
    suspend fun getAllBooks(): List<Book>
    suspend fun deleteBook(book: Book)
    suspend fun updateBook(book: Book)
    suspend fun getUpdatedAt(): Long // For data synchronization
    suspend fun shouldRefreshCache(): Boolean // Cache control
    suspend fun setCacheExpiry(expiryTime: Long) // Set cache expiry time
}
