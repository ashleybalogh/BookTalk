package com.booktalk.ui.discovery.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreChips(
    selectedGenre: String?,
    onGenreSelected: (String) -> Unit,
    onGenreCleared: () -> Unit,
    modifier: Modifier = Modifier
) {
    val genres = listOf("Fiction", "Mystery", "Romance", "Science Fiction", "Biography", "History")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
    ) {
        items(genres) { genre ->
            FilterChip(
                selected = genre == selectedGenre,
                onClick = {
                    if (genre == selectedGenre) {
                        onGenreCleared()
                    } else {
                        onGenreSelected(genre)
                    }
                },
                label = { Text(genre) },
                modifier = Modifier.height(32.dp)
            )
        }
    }
}
