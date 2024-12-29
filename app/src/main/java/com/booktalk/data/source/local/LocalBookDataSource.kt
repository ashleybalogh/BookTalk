package com.booktalk.data.source.local

import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.local.dao.UserBookDao
import com.booktalk.data.local.entity.ReadingStatus
import com.booktalk.data.local.entity.UserBookEntity
import com.booktalk.data.mapper.toBook
import com.booktalk.data.mapper.toEntity
import com.booktalk.domain.model.book.Book
import com.booktalk.data.source.BookDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalBookDataSource @Inject constructor(
    private val bookDao: BookDao,
    private val userBookDao: UserBookDao
) : BookDataSource {

    override suspend fun searchBooks(query: String): List<Book> =
        bookDao.searchBooks(query)
            .first()
            .map { it.toBook() }

    override suspend fun getBooksByCategory(category: String): List<Book> =
        bookDao.getBooksByCategory(category)
            .first()
            .map { it.toBook() }

    override suspend fun getRecommendedBooks(userId: String): List<Book> =
        bookDao.getRecommendedBooks(userId)
            .first()
            .map { it.toBook() }

    override suspend fun getAllBooks(): List<Book> =
        bookDao.getAllBooks()
            .map { it.toBook() }

    override suspend fun getBookById(id: String): Book =
        bookDao.getBookById(id)?.toBook() 
            ?: throw IllegalArgumentException("Book not found with id: $id")

    override suspend fun saveBooks(books: List<Book>) {
        bookDao.insertBooks(books.map { it.toEntity() })
    }

    override suspend fun saveBook(book: Book) {
        bookDao.insertBook(book.toEntity())
    }

    override suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book.toEntity())
    }

    override suspend fun updateBook(book: Book) {
        bookDao.updateBook(book.toEntity())
    }

    override suspend fun getUpdatedAt(): Long = 
        System.currentTimeMillis() // Using current time as we don't have a timestamp field

    override suspend fun shouldRefreshCache(): Boolean = 
        System.currentTimeMillis() - getUpdatedAt() > CACHE_EXPIRY_TIME

    override suspend fun setCacheExpiry(expiryTime: Long) {
        // Implementation depends on how you want to store the cache expiry time
        // Could be in SharedPreferences or another storage mechanism
    }

    // User book operations
    fun getUserBooks(userId: String): Flow<List<UserBookEntity>> =
        userBookDao.getUserBooks(userId)

    fun getUserBooksByStatus(userId: String, status: ReadingStatus): Flow<List<UserBookEntity>> =
        userBookDao.getUserBooksByStatus(userId, status)

    suspend fun getUserBook(userId: String, bookId: String): UserBookEntity? =
        userBookDao.getUserBook(userId, bookId)

    suspend fun insertUserBook(userBook: UserBookEntity) {
        userBookDao.insertUserBook(userBook)
    }

    suspend fun updateUserBook(userBook: UserBookEntity) {
        userBookDao.updateUserBook(userBook)
    }

    suspend fun deleteUserBook(userBook: UserBookEntity) {
        userBookDao.deleteUserBook(userBook)
    }

    suspend fun clearCache() {
        bookDao.getAllBooks().forEach { bookDao.deleteBook(it) }
        userBookDao.getUserBooks("").first().forEach { userBookDao.deleteUserBook(it) }
    }

    companion object {
        private const val CACHE_EXPIRY_TIME = 24 * 60 * 60 * 1000L // 24 hours
    }
}
