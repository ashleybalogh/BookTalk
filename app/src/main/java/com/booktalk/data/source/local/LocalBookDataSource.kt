package com.booktalk.data.source.local

import androidx.paging.PagingData
import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.mapper.toBook
import com.booktalk.data.mapper.toBookEntity
import com.booktalk.data.source.BookDataSource
import com.booktalk.domain.model.book.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalBookDataSource @Inject constructor(
    private val bookDao: BookDao
) : BookDataSource {

    override suspend fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks().map { it.toBook() }
    }

    override suspend fun searchBooks(query: String): List<Book> {
        return bookDao.searchBooks(query).first().map { it.toBook() }
    }

    override suspend fun getBookById(id: String): Book? {
        return bookDao.getBookById(id)?.toBook()
    }

    override suspend fun getBookByIsbn(isbn: String): Book? {
        return bookDao.getBookByIsbn(isbn)?.toBook()
    }

    override suspend fun getBooksByCategory(category: String): List<Book> {
        return bookDao.getBooksByCategory(category).first().map { it.toBook() }
    }

    override suspend fun getRecommendedBooks(userId: String): List<Book> {
        // Local data source doesn't implement recommendations
        return emptyList()
    }

    override suspend fun saveBook(book: Book) {
        bookDao.insertBook(book.toBookEntity())
    }

    override suspend fun insertBooks(books: List<Book>) {
        bookDao.insertBooks(books.map { it.toBookEntity() })
    }

    override fun getSearchBooksPagingFlow(query: String): Flow<PagingData<Book>> {
        throw NotImplementedError("Paging not implemented in local data source")
    }

    override fun getCategoryBooksPagingFlow(category: String): Flow<PagingData<Book>> {
        throw NotImplementedError("Paging not implemented in local data source")
    }
}
