package com.booktalk.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.booktalk.data.remote.api.BookService
import com.booktalk.data.mapper.toBook
import com.booktalk.domain.model.book.Book
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
private const val PAGE_SIZE = 20

class BookSearchPagingSource(
    private val bookService: BookService,
    private val query: String
) : PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = bookService.searchBooks(
                query = query,
                page = position,
                pageSize = params.loadSize.coerceAtMost(PAGE_SIZE)
            )
            
            if (!response.isSuccessful) {
                return LoadResult.Error(HttpException(response))
            }

            val books = response.body()?.items?.map { it.toBook() } ?: emptyList()
            val hasMore = response.body()?.hasMore ?: false

            LoadResult.Page(
                data = books,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (!hasMore) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
