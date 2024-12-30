package com.booktalk.domain.repository

import com.booktalk.data.remote.util.NetworkResult
import com.booktalk.domain.model.book.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBooks(query: String): Flow<NetworkResult<List<Book>>>
    suspend fun getBooksByGenre(genre: String): Flow<NetworkResult<List<Book>>>
    suspend fun getAllBooks(): Flow<NetworkResult<List<Book>>>
    suspend fun getBookById(id: String): Flow<NetworkResult<Book>>
    suspend fun getRecommendedBooks(): Flow<NetworkResult<List<Book>>>
}
