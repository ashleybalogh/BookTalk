package com.booktalk.data.model

import com.booktalk.data.local.entity.BookEntity

fun BookDto.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        author = author,
        description = description,
        coverUrl = coverUrl,
        genres = genres,
        rating = rating,
        pageCount = pageCount,
        publishedDate = publishedDate
    )
}
