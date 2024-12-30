package com.booktalk.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.booktalk.data.local.BookTalkDatabase
import com.booktalk.data.local.entity.BookEntity
import com.booktalk.data.local.entity.RemoteKey
import com.booktalk.data.mapper.toBook
import com.booktalk.data.mapper.toBookEntity
import com.booktalk.data.remote.api.BookService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class BookRemoteMediator @Inject constructor(
    private val query: String,
    private val category: String?,
    private val bookService: BookService,
    private val bookTalkDatabase: BookTalkDatabase
) : RemoteMediator<Int, BookEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookEntity>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val response = if (category != null) {
                bookService.getBooksByCategory(category, page)
            } else {
                bookService.searchBooks(query, page)
            }

            val books = response.body()?.items?.map { it.toBook() } ?: emptyList()
            val endOfPaginationReached = books.isEmpty()

            bookTalkDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    bookTalkDatabase.remoteKeyDao().clearRemoteKeys()
                    bookTalkDatabase.bookDao().deleteAllBooks()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = books.map { book ->
                    RemoteKey(
                        bookId = book.id ?: "",
                        prevKey = prevKey,
                        nextKey = nextKey,
                        query = query,
                        category = category
                    )
                }
                bookTalkDatabase.remoteKeyDao().insertAll(keys)
                bookTalkDatabase.bookDao().insertBooks(books.map { it.toBookEntity() })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, BookEntity>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { book ->
                bookTalkDatabase.remoteKeyDao().remoteKeysBookId(book.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, BookEntity>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { book ->
                bookTalkDatabase.remoteKeyDao().remoteKeysBookId(book.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, BookEntity>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { bookId ->
                bookTalkDatabase.remoteKeyDao().remoteKeysBookId(bookId)
            }
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
