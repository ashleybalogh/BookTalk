package com.booktalk.ui.discovery.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.booktalk.domain.model.book.Book
import com.booktalk.ui.components.ErrorScreen
import com.booktalk.ui.components.LoadingScreen

@Composable
fun PagingBookGrid(
    books: LazyPagingItems<Book>,
    onBookClick: (String) -> Unit,
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
            books.itemCount == 0 -> {
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
                    items(books.itemCount) { index ->
                        val book = books[index]
                        book?.let {
                            BookGridItem(
                                book = it,
                                onBookClick = { onBookClick(it.id ?: "") }
                            )
                        }
                    }
                }
            }
        }
    }
}
