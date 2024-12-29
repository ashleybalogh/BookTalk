package com.booktalk.data.source.remote

import com.booktalk.data.remote.api.BookApi
import com.booktalk.data.remote.api.BookSearchResponse
import com.booktalk.data.mapper.toBook
import com.booktalk.data.source.BookDataSource
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.util.NetworkResult
import com.booktalk.data.remote.util.toNetworkResult
import com.booktalk.data.remote.model.BookDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteBookDataSource @Inject constructor(
    private val bookApi: BookApi
) : BookDataSource {
    override suspend fun searchBooks(query: String): List<Book> {
        val response = bookApi.searchBooks(query)
        return when (val result = response.toNetworkResult()) {
            is NetworkResult.Success -> result.data.items?.map { it.toBook() } ?: emptyList()
            is NetworkResult.Error -> throw Exception(result.message ?: "Failed to search books")
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun getBookById(id: String): Book {
        val response = bookApi.getBookById(id)
        return when (val result = response.toNetworkResult()) {
            is NetworkResult.Success -> result.data.toBook()
            is NetworkResult.Error -> throw Exception(result.message ?: "Failed to get book")
            is NetworkResult.Loading -> throw Exception("Loading state not expected")
        }
    }

    override suspend fun getBooksByCategory(category: String): List<Book> {
        val response = bookApi.getBooksByCategory(category = category)
        return when (val result = response.toNetworkResult()) {
            is NetworkResult.Success -> result.data.items?.map { it.toBook() } ?: emptyList()
            is NetworkResult.Error -> throw Exception(result.message ?: "Failed to get books by category")
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun getRecommendedBooks(userId: String): List<Book> {
        val response = bookApi.getRecommendedBooks()
        return when (val result = response.toNetworkResult()) {
            is NetworkResult.Success -> result.data.items?.map { it.toBook() } ?: emptyList()
            is NetworkResult.Error -> throw Exception(result.message ?: "Failed to get recommended books")
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun getAllBooks(): List<Book> {
        val response = bookApi.getAllBooks()
        return when (val result = response.toNetworkResult()) {
            is NetworkResult.Success -> result.data.items?.map { it.toBook() } ?: emptyList()
            is NetworkResult.Error -> throw Exception(result.message ?: "Failed to get all books")
            is NetworkResult.Loading -> emptyList()
        }
    }

    override suspend fun saveBooks(books: List<Book>) {
        // Remote data source doesn't handle local storage
        throw UnsupportedOperationException("Remote data source doesn't support saving books")
    }

    override suspend fun saveBook(book: Book) {
        // Remote data source doesn't handle local storage
        throw UnsupportedOperationException("Remote data source doesn't support saving book")
    }

    override suspend fun deleteBook(book: Book) {
        // Remote data source doesn't handle local storage
        throw UnsupportedOperationException("Remote data source doesn't support deleting book")
    }

    override suspend fun updateBook(book: Book) {
        // Remote data source doesn't handle local storage
        throw UnsupportedOperationException("Remote data source doesn't support updating book")
    }

    override suspend fun getUpdatedAt(): Long {
        return System.currentTimeMillis()
    }

    override suspend fun shouldRefreshCache(): Boolean {
        return true 
    }

    override suspend fun setCacheExpiry(expiryTime: Long) {
        // Remote data source doesn't handle caching
        throw UnsupportedOperationException("Remote data source doesn't support setting cache expiry")
    }
}
