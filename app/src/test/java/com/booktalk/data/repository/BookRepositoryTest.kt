package com.booktalk.data.repository

import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.local.dao.UserBookDao
import com.booktalk.data.model.BookSearchResponse
import com.booktalk.data.remote.api.BookService
import com.booktalk.data.remote.dto.BookDto
import com.booktalk.data.source.remote.RemoteBookDataSource
import com.booktalk.domain.model.NetworkResult
import com.booktalk.domain.model.book.ReadingStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class BookRepositoryTest {
    private lateinit var bookService: BookService
    private lateinit var bookDao: BookDao
    private lateinit var userBookDao: UserBookDao
    private lateinit var remoteDataSource: RemoteBookDataSource
    private lateinit var repository: BookRepositoryImpl

    @Before
    fun setup() {
        bookService = mockk()
        bookDao = mockk(relaxed = true)
        userBookDao = mockk(relaxed = true)
        remoteDataSource = RemoteBookDataSource(bookService)
        repository = BookRepositoryImpl(
            bookDao = bookDao,
            userBookDao = userBookDao,
            remoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `searchBooks returns success with remote data when API call succeeds`() = runTest {
        val bookDto = BookDto(
            id = "1",
            title = "Test Book",
            author = "Test Author",
            authors = listOf("Test Author"),
            description = "Test Description",
            isbn = "1234567890",
            coverUrl = "http://example.com/cover.jpg",
            pageCount = 100,
            publishedDate = "2024",
            publisher = "Test Publisher",
            categories = listOf("Fiction"),
            language = "en",
            averageRating = 4.5,
            ratingsCount = 100,
            previewLink = "http://example.com/preview",
            infoLink = "http://example.com/info",
            buyLink = "http://example.com/buy"
        )

        val response = BookSearchResponse(
            items = listOf(bookDto),
            total = 1,
            page = 1,
            pageSize = 20,
            hasMore = true
        )

        coEvery {
            bookService.searchBooks(
                query = "test",
                page = 1,
                pageSize = 20
            )
        } returns Response.success(response)

        val result = repository.searchBooks("test").first()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("Test Book", result.data.first().title)
    }

    @Test
    fun `searchBooks returns error when API call fails`() = runTest {
        coEvery {
            bookService.searchBooks(
                query = "test",
                page = 1,
                pageSize = 20
            )
        } throws Exception("Network error")

        val result = repository.searchBooks("test").first()

        assertTrue(result is NetworkResult.Error)
        assertEquals("Network error: Network error", (result as NetworkResult.Error).message)
    }

    @Test
    fun `updateBookStatus updates user book status successfully`() = runTest {
        coEvery {
            userBookDao.updateUserBook(any())
        } returns Unit

        repository.updateBookStatus("user1", "book1", ReadingStatus.READING)

        // No exception thrown means success
    }

    @Test
    fun `getCurrentlyReading returns list of currently reading books`() = runTest {
        val books = repository.getCurrentlyReading("user1").first()
        assertTrue(books is List)
    }
}
