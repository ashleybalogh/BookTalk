package com.booktalk.data.remote.api

import com.booktalk.data.remote.model.BookDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 40,
        @Query("startIndex") startIndex: Int = 0
    ): Response<BookSearchResponse>

    @GET("volumes/{volumeId}")
    suspend fun getBookById(
        @Path("volumeId") volumeId: String
    ): Response<BookDto>

    @GET("volumes")
    suspend fun getBookByIsbn(
        @Query("q") isbn: String
    ): Response<BookSearchResponse>

    @GET("volumes")
    suspend fun getBooksByCategory(
        @Query("q") query: String = "subject:",
        @Query("subject") category: String,
        @Query("maxResults") maxResults: Int = 40,
        @Query("startIndex") startIndex: Int = 0
    ): Response<BookSearchResponse>

    @GET("volumes")
    suspend fun getRecommendedBooks(
        @Query("q") query: String = "*",
        @Query("orderBy") orderBy: String = "relevance",
        @Query("maxResults") maxResults: Int = 40,
        @Query("startIndex") startIndex: Int = 0
    ): Response<BookSearchResponse>

    @GET("volumes")
    suspend fun getAllBooks(
        @Query("q") query: String = "*",
        @Query("maxResults") maxResults: Int = 40,
        @Query("startIndex") startIndex: Int = 0
    ): Response<BookSearchResponse>
}

data class BookSearchResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<BookDto>?
)
