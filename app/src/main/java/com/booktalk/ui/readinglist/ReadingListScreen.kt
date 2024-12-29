package com.booktalk.ui.readinglist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.booktalk.ui.discovery.components.BookGrid
import com.booktalk.ui.components.ErrorMessage
import com.booktalk.ui.components.LoadingSpinner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingListScreen(
    onBookClick: (String) -> Unit = {},
    viewModel: ReadingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reading List") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingSpinner(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    ErrorMessage(
                        message = uiState.error ?: "Unknown error occurred",
                        onRetry = { viewModel.loadBooks() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.books.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Your reading list is empty",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add books to your reading list to see them here",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                else -> {
                    BookGrid(
                        books = uiState.books,
                        gridState = gridState,
                        onBookClick = onBookClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
