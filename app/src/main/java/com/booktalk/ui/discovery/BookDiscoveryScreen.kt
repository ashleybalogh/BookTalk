package com.booktalk.ui.discovery

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.booktalk.ui.discovery.components.*

@Composable
fun BookDiscoveryScreen(
    viewModel: BookDiscoveryViewModel = hiltViewModel(),
    onBookClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val categoryBooks = viewModel.categoryBooks.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            onClearQuery = viewModel::clearSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .padding(16.dp)
            )
        }

        if (uiState.searchQuery.isNotEmpty()) {
            SectionHeader(title = "Search Results")
            BookGrid(
                books = searchResults.itemSnapshotList.items,
                onBookClick = { book -> onBookClick(book.id!!) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            if (uiState.selectedCategory != null) {
                SectionHeader(title = "${uiState.selectedCategory} Books")
                BookGrid(
                    books = categoryBooks.itemSnapshotList.items,
                    onBookClick = { book -> onBookClick(book.id!!) },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                CategoryChips(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = viewModel::onCategorySelected,
                    onCategoryCleared = viewModel::clearCategory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                SectionHeader(title = "Recent Books")
                BookGrid(
                    books = emptyList(),
                    onBookClick = { book -> onBookClick(book.id!!) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
