package com.booktalk.domain.repository

import androidx.paging.PagingData
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus
import com.booktalk.domain.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getAllBooks(): Flow<NetworkResult<List<Book>>>
    suspend fun searchBooks(query: String): Flow<NetworkResult<List<Book>>>
    fun getSearchBooksPagingFlow(query: String): Flow<PagingData<Book>>
    fun getCategoryBooksPagingFlow(category: String): Flow<PagingData<Book>>
    suspend fun getBookById(id: String): Flow<NetworkResult<Book>>
    suspend fun getBookByIsbn(isbn: String): Flow<NetworkResult<Book>>
    suspend fun getBooksByCategory(category: String): Flow<NetworkResult<List<Book>>>
    suspend fun getUserBooks(userId: String): Flow<List<Book>>
    suspend fun getUserBooksByStatus(userId: String, status: ReadingStatus): Flow<List<Book>>
    suspend fun updateReadingProgress(userId: String, bookId: String, currentPage: Int, totalPages: Int)
    suspend fun updateBookStatus(userId: String, bookId: String, status: ReadingStatus)
    suspend fun rateBook(userId: String, bookId: String, rating: Float)
    suspend fun addBookNote(userId: String, bookId: String, note: String)
    suspend fun getCurrentlyReading(userId: String): Flow<List<Book>>
    suspend fun getFinishedBooks(userId: String): Flow<List<Book>>
}
