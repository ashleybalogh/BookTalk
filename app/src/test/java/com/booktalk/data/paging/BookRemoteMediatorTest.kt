package com.booktalk.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.local.dao.RemoteKeyDao
import com.booktalk.data.local.entity.BookEntity
import com.booktalk.data.local.entity.RemoteKey
import com.booktalk.data.model.BookSearchResponse
import com.booktalk.data.remote.api.BookService
import com.booktalk.data.remote.dto.BookDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
class BookRemoteMediatorTest {
    private lateinit var bookService: BookService
    private lateinit var bookDao: BookDao
    private lateinit var remoteKeyDao: RemoteKeyDao
    private lateinit var remoteMediator: BookRemoteMediator

    @Before
    fun setup() {
        bookService = mockk()
        bookDao = mockk(relaxed = true)
        remoteKeyDao = mockk(relaxed = true)
        remoteMediator = BookRemoteMediator(
            bookService = bookService,
            bookDao = bookDao,
            remoteKeyDao = remoteKeyDao,
            query = "test"
        )
    }

    @Test
    fun `refresh load returns success result when more data is present`() = runTest {
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
                pageSize = any()
            )
        } returns Response.success(response)

        val result = remoteMediator.load(
            LoadType.REFRESH,
            PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(20),
                leadingPlaceholderCount = 0
            )
        )

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached.not())
    }

    @Test
    fun `refresh load success and endOfPaginationReached when no more data`() = runTest {
        val response = BookSearchResponse(
            items = emptyList(),
            total = 0,
            page = 1,
            pageSize = 20,
            hasMore = false
        )

        coEvery {
            bookService.searchBooks(
                query = "test",
                page = 1,
                pageSize = any()
            )
        } returns Response.success(response)

        val result = remoteMediator.load(
            LoadType.REFRESH,
            PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(20),
                leadingPlaceholderCount = 0
            )
        )

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `refresh load returns error result when error occurs`() = runTest {
        coEvery {
            bookService.searchBooks(
                query = "test",
                page = 1,
                pageSize = any()
            )
        } throws Exception("Test exception")

        val result = remoteMediator.load(
            LoadType.REFRESH,
            PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(20),
                leadingPlaceholderCount = 0
            )
        )

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}
