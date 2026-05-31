package com.potpilot.cookbook.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.potpilot.cookbook.data.Recipe
import com.potpilot.cookbook.ui.components.RecipeCard

@Composable
fun FavoritesScreen(
    allRecipes: List<Recipe>,
    favorites: Set<String>,
    keepAwake: Boolean,
    twoPane: Boolean,
    onToggleFavorite: (String) -> Unit,
) {
    val favRecipes = remember(allRecipes, favorites) { allRecipes.filter { it.id in favorites } }
    var selectedId by remember { mutableStateOf<String?>(null) }
    val selected = selectedId?.let { id -> favRecipes.firstOrNull { it.id == id } }

    if (favRecipes.isEmpty()) {
        EmptyFavorites()
        return
    }

    val grid: @Composable (Modifier) -> Unit = { mod ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = mod,
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(favRecipes, key = { it.id }) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    selected = recipe.id == selectedId,
                    isFavorite = true,
                    onClick = { selectedId = recipe.id },
                )
            }
        }
    }

    if (twoPane) {
        Row(Modifier.fillMaxSize()) {
            grid(Modifier.weight(1f).fillMaxSize())
            Box(Modifier.weight(1.3f).fillMaxSize()) {
                if (selected != null) {
                    RecipeDetail(
                        recipe = selected,
                        isFavorite = true,
                        keepAwake = keepAwake,
                        onToggleFavorite = { onToggleFavorite(selected.id) },
                        showBack = false,
                        onBack = {},
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Pick a favourite", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    } else {
        if (selected != null) {
            BackHandler { selectedId = null }
            RecipeDetail(
                recipe = selected,
                isFavorite = true,
                keepAwake = keepAwake,
                onToggleFavorite = { onToggleFavorite(selected.id) },
                showBack = true,
                onBack = { selectedId = null },
            )
        } else {
            grid(Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun EmptyFavorites() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("★", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
            Text(
                "No favourites yet\nTap the star on any recipe to save it here.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
