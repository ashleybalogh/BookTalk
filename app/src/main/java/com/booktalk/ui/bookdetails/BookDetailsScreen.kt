package com.booktalk.ui.bookdetails

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.booktalk.domain.model.book.Book
import com.booktalk.ui.components.ErrorScreen
import com.booktalk.ui.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    viewModel: BookDetailsViewModel,
    onBackClick: () -> Unit,
    onStartReading: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingScreen()
                }
                uiState.error != null -> {
                    ErrorScreen(
                        message = uiState.error!!,
                        onRetry = { viewModel.loadBook(uiState.book?.id ?: "") }
                    )
                }
                uiState.book != null -> {
                    BookDetailsContent(
                        book = uiState.book!!,
                        onStartReading = { onStartReading(uiState.book!!) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookDetailsContent(
    book: Book,
    onStartReading: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = book.imageUrl,
            contentDescription = "Cover of ${book.title}",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(240.dp)
                .fillMaxWidth()
        )

        Text(
            text = book.title ?: "Untitled",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = book.authors?.joinToString(", ") ?: "Unknown Author",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        if (!book.description.isNullOrBlank()) {
            Text(
                text = book.description!!,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }

        Button(
            onClick = onStartReading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Reading")
        }
    }
}
