package com.booktalk.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.booktalk.data.remote.api.BookService
import com.booktalk.data.mapper.toBook
import com.booktalk.data.paging.BookSearchPagingSource
import com.booktalk.data.paging.CategoryBooksPagingSource
import com.booktalk.data.source.BookDataSource
import com.booktalk.domain.model.book.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteBookDataSource @Inject constructor(
    private val bookService: BookService
) : BookDataSource {

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 3
        private const val MAX_SIZE = 100
    }

    override suspend fun getAllBooks(): List<Book> {
        return searchBooks("")
    }

    override suspend fun searchBooks(query: String): List<Book> {
        return try {
            val response = bookService.searchBooks(query, page = 1, pageSize = PAGE_SIZE)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.items?.map { it.toBook() } ?: emptyList()
            } else {
                throw Exception(response.errorBody()?.string() ?: "Failed to search books")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override fun getSearchBooksPagingFlow(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BookSearchPagingSource(bookService, query) }
        ).flow
    }

    override fun getCategoryBooksPagingFlow(category: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CategoryBooksPagingSource(bookService, category) }
        ).flow
    }

    override suspend fun getBookById(id: String): Book? {
        return try {
            val response = bookService.getBookById(id)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.book?.toBook()
            } else {
                throw Exception(response.errorBody()?.string() ?: "Failed to get book by ID")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override suspend fun getBookByIsbn(isbn: String): Book? {
        return try {
            val response = bookService.getBookByIsbn(isbn)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.book?.toBook()
            } else {
                throw Exception(response.errorBody()?.string() ?: "Failed to get book by ISBN")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override suspend fun getBooksByCategory(category: String): List<Book> {
        return try {
            val response = bookService.getBooksByCategory(category, page = 1, pageSize = PAGE_SIZE)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.items?.map { it.toBook() } ?: emptyList()
            } else {
                throw Exception(response.errorBody()?.string() ?: "Failed to get books by category")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override suspend fun getRecommendedBooks(userId: String): List<Book> {
        return try {
            val response = bookService.getRecommendedBooks(userId, 1)
            if (response.isSuccessful) {
                response.body()?.items?.map { it.toBook() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveBook(book: Book) {
        // Remote data source doesn't implement saving
    }

    override suspend fun insertBooks(books: List<Book>) {
        // Remote data source doesn't implement batch insert
    }
}
