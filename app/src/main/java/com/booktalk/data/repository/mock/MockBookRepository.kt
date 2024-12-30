package com.booktalk.data.repository.mock

import androidx.paging.PagingData
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus
import com.booktalk.domain.repository.BookRepository
import com.booktalk.domain.util.NetworkResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MockBookRepository @Inject constructor() : BookRepository {
    private val mockBooks = listOf(
        Book(
            id = "1",
            title = "The Great Gatsby",
            authors = listOf("F. Scott Fitzgerald"),
            description = "A story of the fabulously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.",
            publishedDate = "1925", // for Gatsby
            publisher = "Charles Scribner's Sons",
            language = "en",
            isbn = "978-0743273565",
            imageUrl = "https://example.com/gatsby.jpg",
            categories = listOf("Fiction"),
            averageRating = 4.5f,
            pageCount = 180,
            currentPage = 0,
            status = ReadingStatus.WANT_TO_READ,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        Book(
            id = "2",
            title = "1984",
            authors = listOf("George Orwell"),
            description = "A dystopian social science fiction novel that follows the life of Winston Smith.",
            publishedDate = "1949",
            publisher = "Secker and Warburg",
            language = "en",
            isbn = "978-0451524935",
            imageUrl = "https://example.com/1984.jpg",
            categories = listOf("Science Fiction"),
            averageRating = 4.8f,
            pageCount = 328,
            currentPage = 0,
            status = ReadingStatus.WANT_TO_READ,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    )

    override suspend fun getAllBooks(): Flow<NetworkResult<List<Book>>> = flow {
        delay(1000)
        emit(NetworkResult.Success(mockBooks))
    }

    override suspend fun searchBooks(query: String): Flow<NetworkResult<List<Book>>> = flow {
        delay(1000)
        emit(NetworkResult.Success(
            mockBooks.filter { book ->
                book.title?.contains(query, ignoreCase = true) == true ||
                book.authors?.firstOrNull()?.contains(query, ignoreCase = true) == true
            }
        ))
    }

    override fun getSearchBooksPagingFlow(query: String): Flow<PagingData<Book>> = flow {
        delay(1000)
        emit(PagingData.from(mockBooks.filter { book ->
            book.title?.contains(query, ignoreCase = true) == true ||
            book.authors?.firstOrNull()?.contains(query, ignoreCase = true) == true
        }))
    }

    override fun getCategoryBooksPagingFlow(category: String): Flow<PagingData<Book>> = flow {
        delay(1000)
        emit(PagingData.from(mockBooks.filter { it.categories?.contains(category) == true }))
    }

    override suspend fun getBookById(id: String): Flow<NetworkResult<Book>> = flow {
        delay(1000)
        emit(NetworkResult.Success(mockBooks.find { it.id == id } ?: throw NoSuchElementException("Book not found")))
    }

    override suspend fun getBookByIsbn(isbn: String): Flow<NetworkResult<Book>> = flow {
        delay(1000)
        emit(NetworkResult.Success(mockBooks.find { it.isbn == isbn } ?: throw NoSuchElementException("Book not found")))
    }

    override suspend fun getBooksByCategory(category: String): Flow<NetworkResult<List<Book>>> = flow {
        delay(1000)
        emit(NetworkResult.Success(mockBooks.filter { it.categories?.contains(category) == true }))
    }

    override suspend fun getUserBooks(userId: String): Flow<List<Book>> = flow {
        delay(1000)
        emit(mockBooks)
    }

    override suspend fun getUserBooksByStatus(userId: String, status: ReadingStatus): Flow<List<Book>> = flow {
        delay(1000)
        emit(mockBooks.filter { it.status == status })
    }

    override suspend fun updateBookStatus(userId: String, bookId: String, status: ReadingStatus) {
        delay(1000)
        // In a real implementation, this would update the database
    }

    override suspend fun updateReadingProgress(
        userId: String,
        bookId: String,
        currentPage: Int,
        totalPages: Int
    ) {
        delay(1000)
        // In a real implementation, this would update the database
    }

    override suspend fun rateBook(userId: String, bookId: String, rating: Float) {
        delay(1000)
        // In a real implementation, this would update the database
    }

    override suspend fun addBookNote(userId: String, bookId: String, note: String) {
        delay(1000)
        // In a real implementation, this would update the database
    }

    override suspend fun getCurrentlyReading(userId: String): Flow<List<Book>> = flow {
        delay(1000)
        emit(mockBooks.filter { it.status == ReadingStatus.CURRENTLY_READING })
    }

    override suspend fun getFinishedBooks(userId: String): Flow<List<Book>> = flow {
        delay(1000)
        emit(mockBooks.filter { it.status == ReadingStatus.READ })
    }
}
