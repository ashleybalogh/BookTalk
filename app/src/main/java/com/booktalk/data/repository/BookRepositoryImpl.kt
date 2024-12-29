package com.booktalk.data.repository

import com.booktalk.data.local.entity.ReadingStatus
import com.booktalk.data.local.entity.UserBookEntity
import com.booktalk.data.remote.exception.ApiException
import com.booktalk.data.source.local.LocalBookDataSource
import com.booktalk.data.source.remote.RemoteBookDataSource
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.repository.BookRepository
import com.booktalk.domain.util.NetworkResult
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val localDataSource: LocalBookDataSource,
    private val remoteDataSource: RemoteBookDataSource
) : BookRepository {

    override suspend fun getAllBooks(): Flow<NetworkResult<List<Book>>> = flow {
        emit(NetworkResult.loading())
        try {
            val books = localDataSource.getAllBooks()
            emit(NetworkResult.success(books))
        } catch (e: Exception) {
            emit(NetworkResult.error(error = e, message = e.message))
        }
    }

    override suspend fun searchBooks(query: String): Flow<NetworkResult<List<Book>>> = flow {
        emit(NetworkResult.loading())
        try {
            val books = localDataSource.searchBooks(query)
            emit(NetworkResult.success(books))
        } catch (e: Exception) {
            emit(NetworkResult.error(error = e, message = e.message))
        }
    }

    override suspend fun getBookById(bookId: String): Flow<NetworkResult<Book>> = flow {
        emit(NetworkResult.loading())
        try {
            val book = localDataSource.getBookById(bookId)
            if (book != null) {
                emit(NetworkResult.success(book))
            } else {
                emit(NetworkResult.error(message = "Book not found"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.error(error = e, message = e.message))
        }
    }

    override suspend fun getBookByIsbn(isbn: String): Flow<NetworkResult<Book>> = flow {
        emit(NetworkResult.loading())
        try {
            val book = localDataSource.getBookById(isbn) // We'll use local first, then remote if not found
            emit(NetworkResult.success(book))
        } catch (e: Exception) {
            try {
                val remoteBook = remoteDataSource.getBookById(isbn)
                emit(NetworkResult.success(remoteBook))
            } catch (e: Exception) {
                emit(NetworkResult.error(error = e, message = e.message))
            }
        }
    }

    override suspend fun getBooksByCategory(category: String): Flow<NetworkResult<List<Book>>> = flow {
        emit(NetworkResult.loading())
        try {
            val books = localDataSource.getBooksByCategory(category)
            emit(NetworkResult.success(books))
        } catch (e: Exception) {
            try {
                val remoteBooks = remoteDataSource.getBooksByCategory(category)
                emit(NetworkResult.success(remoteBooks))
            } catch (e: Exception) {
                emit(NetworkResult.error(error = e, message = e.message))
            }
        }
    }

    override suspend fun insertBook(book: Book) {
        localDataSource.saveBook(book)
    }

    override suspend fun insertBooks(books: List<Book>) {
        localDataSource.saveBooks(books)
    }

    override suspend fun updateBook(book: Book) {
        localDataSource.updateBook(book)
    }

    override suspend fun deleteBook(book: Book) {
        localDataSource.deleteBook(book)
    }

    override fun getUserBooks(userId: String): Flow<List<UserBookEntity>> =
        localDataSource.getUserBooks(userId)

    override fun getUserBooksByStatus(userId: String, status: ReadingStatus): Flow<List<UserBookEntity>> =
        localDataSource.getUserBooksByStatus(userId, status)

    override suspend fun getUserBook(userId: String, bookId: String): UserBookEntity? =
        localDataSource.getUserBook(userId, bookId)

    override suspend fun insertUserBook(userBook: UserBookEntity) {
        localDataSource.insertUserBook(userBook)
    }

    override suspend fun updateUserBook(userBook: UserBookEntity) {
        localDataSource.updateUserBook(userBook)
    }

    override suspend fun deleteUserBook(userBook: UserBookEntity) {
        localDataSource.deleteUserBook(userBook)
    }

    override fun getCurrentlyReading(userId: String): Flow<List<UserBookEntity>> =
        getUserBooksByStatus(userId, ReadingStatus.READING)

    override fun getFinishedBooks(userId: String): Flow<List<UserBookEntity>> =
        localDataSource.getUserBooksByStatus(userId, ReadingStatus.FINISHED)

    override suspend fun getReadingList(): Flow<NetworkResult<List<Book>>> = flow {
        emit(NetworkResult.loading())
        try {
            val books = localDataSource.getAllBooks()
            emit(NetworkResult.success(books))
        } catch (e: Exception) {
            emit(NetworkResult.error(error = e, message = e.message))
        }
    }

    override suspend fun updateReadingProgress(userId: String, bookId: String, currentPage: Int, totalPages: Int) {
        val userBook = getUserBook(userId, bookId)
        userBook?.let {
            val newStatus = when {
                currentPage >= totalPages -> ReadingStatus.FINISHED
                currentPage > 0 -> ReadingStatus.READING
                else -> it.status // Keep current status if no progress
            }

            val updatedUserBook = it.copy(
                currentPage = currentPage,
                totalPages = totalPages,
                status = newStatus,
                startDate = if (newStatus == ReadingStatus.READING && it.startDate == null)
                    System.currentTimeMillis() else it.startDate,
                finishDate = if (newStatus == ReadingStatus.FINISHED)
                    System.currentTimeMillis() else null
            )
            updateUserBook(updatedUserBook)
        }
    }

    override suspend fun clearCache() {
        localDataSource.clearCache()
    }
}
