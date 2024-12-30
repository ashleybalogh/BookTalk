package com.booktalk.data.repository

import androidx.paging.PagingData
import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.local.dao.UserBookDao
import com.booktalk.data.local.entity.UserBookEntity
import com.booktalk.data.mapper.toBook
import com.booktalk.data.mapper.toBookEntity
import com.booktalk.data.source.remote.RemoteBookDataSource
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus
import com.booktalk.domain.repository.BookRepository
import com.booktalk.domain.util.NetworkResult
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
    private val userBookDao: UserBookDao,
    private val remoteDataSource: RemoteBookDataSource
) : BookRepository {

    override suspend fun getAllBooks(): Flow<NetworkResult<List<Book>>> = flow {
        emit(NetworkResult.loading())
        try {
            val remoteBooks = remoteDataSource.getAllBooks()
            bookDao.insertBooks(remoteBooks.map { it.toBookEntity() })
            emit(NetworkResult.success(remoteBooks))
        } catch (e: Exception) {
            val localBooks = bookDao.getAllBooks().map { it.toBook() }
            if (localBooks.isNotEmpty()) {
                emit(NetworkResult.success(localBooks))
            } else {
                emit(NetworkResult.error(e.message ?: "Failed to load books"))
            }
        }
    }

    override suspend fun searchBooks(query: String): Flow<NetworkResult<List<Book>>> = flow {
        emit(NetworkResult.loading())
        try {
            val searchResults = remoteDataSource.searchBooks(query)
            emit(NetworkResult.success(searchResults))
        } catch (e: Exception) {
            emit(NetworkResult.error(e.message ?: "Failed to search books"))
        }
    }

    override fun getSearchBooksPagingFlow(query: String): Flow<PagingData<Book>> {
        return remoteDataSource.getSearchBooksPagingFlow(query)
    }

    override fun getCategoryBooksPagingFlow(category: String): Flow<PagingData<Book>> {
        return remoteDataSource.getCategoryBooksPagingFlow(category)
    }

    override suspend fun getBookById(id: String): Flow<NetworkResult<Book>> = flow {
        emit(NetworkResult.loading())
        try {
            val remoteBook = remoteDataSource.getBookById(id)
            if (remoteBook != null) {
                bookDao.insertBook(remoteBook.toBookEntity())
                emit(NetworkResult.success(remoteBook))
            } else {
                val localBook = bookDao.getBookById(id)?.toBook()
                if (localBook != null) {
                    emit(NetworkResult.success(localBook))
                } else {
                    emit(NetworkResult.error("Book not found"))
                }
            }
        } catch (e: Exception) {
            val localBook = bookDao.getBookById(id)?.toBook()
            if (localBook != null) {
                emit(NetworkResult.success(localBook))
            } else {
                emit(NetworkResult.error(e.message ?: "Failed to get book"))
            }
        }
    }

    override suspend fun getBookByIsbn(isbn: String): Flow<NetworkResult<Book>> = flow {
        emit(NetworkResult.loading())
        try {
            val remoteBook = remoteDataSource.getBookByIsbn(isbn)
            if (remoteBook != null) {
                bookDao.insertBook(remoteBook.toBookEntity())
                emit(NetworkResult.success(remoteBook))
            } else {
                val localBook = bookDao.getBookByIsbn(isbn)?.toBook()
                if (localBook != null) {
                    emit(NetworkResult.success(localBook))
                } else {
                    emit(NetworkResult.error("Book not found"))
                }
            }
        } catch (e: Exception) {
            val localBook = bookDao.getBookByIsbn(isbn)?.toBook()
            if (localBook != null) {
                emit(NetworkResult.success(localBook))
            } else {
                emit(NetworkResult.error(e.message ?: "Failed to get book"))
            }
        }
    }

    override suspend fun getBooksByCategory(category: String): Flow<NetworkResult<List<Book>>> = flow {
        emit(NetworkResult.loading())
        try {
            val remoteBooks = remoteDataSource.getBooksByCategory(category)
            emit(NetworkResult.success(remoteBooks))
        } catch (e: Exception) {
            emit(NetworkResult.error(e.message ?: "Failed to get books by category"))
        }
    }

    override suspend fun getUserBooks(userId: String): Flow<List<Book>> {
        return userBookDao.getUserBooks(userId)
            .map { userBooks ->
                userBooks.mapNotNull { userBook ->
                    bookDao.getBookById(userBook.bookId)?.toBook()
                }
            }
    }

    override suspend fun getUserBooksByStatus(userId: String, status: ReadingStatus): Flow<List<Book>> {
        return userBookDao.getUserBooksByStatus(userId, status)
            .map { userBooks ->
                userBooks.mapNotNull { userBook ->
                    bookDao.getBookById(userBook.bookId)?.toBook()
                }
            }
    }

    override suspend fun updateReadingProgress(userId: String, bookId: String, currentPage: Int, totalPages: Int) {
        userBookDao.updateReadingProgress(userId, bookId, currentPage, totalPages)
    }

    override suspend fun updateBookStatus(userId: String, bookId: String, status: ReadingStatus) {
        val userBook = userBookDao.getUserBook(userId, bookId) ?: UserBookEntity(
            userId = userId,
            bookId = bookId,
            status = status
        )
        userBookDao.insertUserBook(userBook.copy(status = status))
    }

    override suspend fun rateBook(userId: String, bookId: String, rating: Float) {
        val userBook = userBookDao.getUserBook(userId, bookId) ?: UserBookEntity(
            userId = userId,
            bookId = bookId
        )
        userBookDao.insertUserBook(userBook.copy(rating = rating))
    }

    override suspend fun addBookNote(userId: String, bookId: String, note: String) {
        val userBook = userBookDao.getUserBook(userId, bookId) ?: UserBookEntity(
            userId = userId,
            bookId = bookId
        )
        userBookDao.insertUserBook(userBook.copy(notes = note))
    }

    override suspend fun getCurrentlyReading(userId: String): Flow<List<Book>> {
        return userBookDao.getCurrentlyReading(userId)
            .map { userBooks ->
                userBooks.mapNotNull { userBook ->
                    bookDao.getBookById(userBook.bookId)?.toBook()
                }
            }
    }

    override suspend fun getFinishedBooks(userId: String): Flow<List<Book>> {
        return userBookDao.getFinishedBooks(userId)
            .map { userBooks ->
                userBooks.mapNotNull { userBook ->
                    bookDao.getBookById(userBook.bookId)?.toBook()
                }
            }
    }
}
