package com.booktalk.data.paging

import androidx.paging.PagingSource
import com.booktalk.data.remote.api.BookService
import com.booktalk.data.remote.dto.BookDto
import com.booktalk.data.model.BookSearchResponse
import com.booktalk.domain.model.book.Book
import com.booktalk.data.mapper.toBook
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import retrofit2.HttpException

class BookSearchPagingSourceTest {
    private lateinit var bookService: BookService
    private lateinit var pagingSource: BookSearchPagingSource

    @Before
    fun setup() {
        bookService = mockk()
        pagingSource = BookSearchPagingSource(
            bookService = bookService,
            query = "test"
        )
    }

    @Test
    fun `load returns page when successful load`() = runTest {
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

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(listOf(bookDto.toBook()), page.data)
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load returns error when service call fails`() = runTest {
        coEvery { 
            bookService.searchBooks(
                query = "test",
                page = 1,
                pageSize = 20
            )
        } throws IOException("Network error")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals("Network error", (result as PagingSource.LoadResult.Error).throwable.message)
    }

    @Test
    fun `load returns error when response is unsuccessful`() = runTest {
        coEvery { 
            bookService.searchBooks(
                query = "test",
                page = 1,
                pageSize = 20
            )
        } returns Response.error(404, mockk(relaxed = true))

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        assertTrue((result as PagingSource.LoadResult.Error).throwable is HttpException)
    }
}
