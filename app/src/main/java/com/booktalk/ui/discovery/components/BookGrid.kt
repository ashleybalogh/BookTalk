package com.booktalk.ui.discovery.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.booktalk.domain.model.book.Book
import com.booktalk.ui.components.ErrorScreen
import com.booktalk.ui.components.LoadingScreen

@Composable
fun BookGrid(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    onRetry: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            isLoading -> {
                LoadingScreen()
            }
            error != null -> {
                ErrorScreen(
                    message = error,
                    onRetry = onRetry
                )
            }
            books.isEmpty() -> {
                Text(
                    text = "No books found",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(books) { book ->
                        BookGridItem(
                            book = book,
                            onBookClick = { onBookClick(book) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookGridItem(
    book: Book,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable { onBookClick() }
    ) {
        Column {
            AsyncImage(
                model = book.imageUrl ?: "",
                contentDescription = book.title ?: "Book cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = book.title ?: "Untitled",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (book.authors?.isNotEmpty() == true) {
                    Text(
                        text = book.authors.first(),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
