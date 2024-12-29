package com.booktalk.ui.discovery.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val categories = listOf(
    "Fiction",
    "Non-Fiction",
    "Mystery",
    "Science Fiction",
    "Fantasy",
    "Romance",
    "Thriller",
    "Horror",
    "Biography",
    "History",
    "Science",
    "Technology",
    "Business",
    "Self-Help",
    "Poetry",
    "Drama",
    "Comics",
    "Art",
    "Travel",
    "Cooking"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChips(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    onCategoryCleared: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
    ) {
        items(categories) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = {
                    if (category == selectedCategory) {
                        onCategoryCleared()
                    } else {
                        onCategorySelected(category)
                    }
                },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}
