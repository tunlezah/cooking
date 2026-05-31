package com.potpilot.cookbook.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyRowItems
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.potpilot.cookbook.data.Recipe
import com.potpilot.cookbook.ui.components.RecipeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    recipes: List<Recipe>,
    categories: List<String>,
    favorites: Set<String>,
    keepAwake: Boolean,
    twoPane: Boolean,
    onToggleFavorite: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf<String?>(null) }
    var selectedId by remember { mutableStateOf<String?>(null) }

    val filtered = remember(recipes, query, category) {
        recipes.filter { r ->
            (category == null || r.category == category) &&
                (query.isBlank() || r.title.contains(query, true) ||
                    r.tags.any { it.contains(query, true) } ||
                    r.ingredients.any { it.contains(query, true) })
        }
    }

    // On a phone, opening a recipe replaces the list; Back returns to it.
    val selected = selectedId?.let { id -> filtered.firstOrNull { it.id == id } ?: recipes.firstOrNull { it.id == id } }

    if (twoPane) {
        Row(Modifier.fillMaxSize()) {
            ListPane(
                query = query,
                onQuery = { query = it },
                category = category,
                categories = categories,
                onCategory = { category = it },
                recipes = filtered,
                favorites = favorites,
                selectedId = selectedId,
                onSelect = { selectedId = it },
                modifier = Modifier.weight(1f),
            )
            Box(Modifier.weight(1.3f).fillMaxSize()) {
                if (selected != null) {
                    RecipeDetail(
                        recipe = selected,
                        isFavorite = selected.id in favorites,
                        keepAwake = keepAwake,
                        onToggleFavorite = { onToggleFavorite(selected.id) },
                        showBack = false,
                        onBack = {},
                    )
                } else {
                    EmptyDetail()
                }
            }
        }
    } else {
        if (selected != null) {
            BackHandler { selectedId = null }
            RecipeDetail(
                recipe = selected,
                isFavorite = selected.id in favorites,
                keepAwake = keepAwake,
                onToggleFavorite = { onToggleFavorite(selected.id) },
                showBack = true,
                onBack = { selectedId = null },
            )
        } else {
            ListPane(
                query = query,
                onQuery = { query = it },
                category = category,
                categories = categories,
                onCategory = { category = it },
                recipes = filtered,
                favorites = favorites,
                selectedId = selectedId,
                onSelect = { selectedId = it },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListPane(
    query: String,
    onQuery: (String) -> Unit,
    category: String?,
    categories: List<String>,
    onCategory: (String?) -> Unit,
    recipes: List<Recipe>,
    favorites: Set<String>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        TextField(
            value = query,
            onValueChange = onQuery,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search recipes & ingredients") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQuery("") }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FilterChip(
                    selected = category == null,
                    onClick = { onCategory(null) },
                    label = { Text("All") },
                )
            }
            lazyRowItems(categories) { cat ->
                FilterChip(
                    selected = category == cat,
                    onClick = { onCategory(if (category == cat) null else cat) },
                    label = { Text(cat) },
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(recipes, key = { it.id }) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    selected = recipe.id == selectedId,
                    isFavorite = recipe.id in favorites,
                    onClick = { onSelect(recipe.id) },
                )
            }
        }
    }
}

@Composable
private fun EmptyDetail() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🍲", style = MaterialTheme.typography.displaySmall)
            Text(
                "Pick a recipe to get cooking",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
