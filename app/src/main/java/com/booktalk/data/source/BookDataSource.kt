package com.booktalk.data.source

import androidx.paging.PagingData
import com.booktalk.domain.model.book.Book
import kotlinx.coroutines.flow.Flow

interface BookDataSource {
    suspend fun getAllBooks(): List<Book>
    suspend fun searchBooks(query: String): List<Book>
    suspend fun getBookById(id: String): Book?
    suspend fun getBookByIsbn(isbn: String): Book?
    suspend fun getBooksByCategory(category: String): List<Book>
    suspend fun getRecommendedBooks(userId: String): List<Book>
    suspend fun saveBook(book: Book)
    suspend fun insertBooks(books: List<Book>)
    fun getSearchBooksPagingFlow(query: String): Flow<PagingData<Book>> = throw NotImplementedError()
    fun getCategoryBooksPagingFlow(category: String): Flow<PagingData<Book>> = throw NotImplementedError()
}
