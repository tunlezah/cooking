package com.potpilot.cookbook.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potpilot.cookbook.ui.screens.FavoritesScreen
import com.potpilot.cookbook.ui.screens.FunctionsScreen
import com.potpilot.cookbook.ui.screens.RecipesScreen
import com.potpilot.cookbook.ui.screens.SettingsScreen

private enum class Destination(val label: String, val icon: ImageVector) {
    RECIPES("Recipes", Icons.AutoMirrored.Filled.MenuBook),
    FAVORITES("Favourites", Icons.Filled.Star),
    FUNCTIONS("Functions", Icons.Filled.Tune),
    SETTINGS("Settings", Icons.Filled.Settings),
}

@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
@Composable
fun CookbookApp(
    viewModel: CookbookViewModel,
    windowSizeClass: WindowSizeClass,
) {
    var destination by remember { mutableStateOf(Destination.RECIPES) }
    val settings by viewModel.settingsState.collectAsStateWithLifecycle()
    val twoPane = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            Destination.entries.forEach { dest ->
                item(
                    selected = destination == dest,
                    onClick = { destination = dest },
                    icon = { Icon(dest.icon, contentDescription = dest.label) },
                    label = { Text(dest.label) },
                )
            }
        },
    ) {
        Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
            when (destination) {
                Destination.RECIPES -> RecipesScreen(
                    recipes = viewModel.recipes,
                    categories = viewModel.categories,
                    favorites = settings.favorites,
                    keepAwake = settings.keepAwake,
                    twoPane = twoPane,
                    onToggleFavorite = viewModel::toggleFavorite,
                )

                Destination.FAVORITES -> FavoritesScreen(
                    allRecipes = viewModel.recipes,
                    favorites = settings.favorites,
                    keepAwake = settings.keepAwake,
                    twoPane = twoPane,
                    onToggleFavorite = viewModel::toggleFavorite,
                )

                Destination.FUNCTIONS -> FunctionsScreen(
                    functions = viewModel.functions,
                    concepts = viewModel.concepts,
                )

                Destination.SETTINGS -> SettingsScreen(
                    state = settings,
                    recipeCount = viewModel.recipes.size,
                    onTheme = viewModel::setTheme,
                    onDynamic = viewModel::setDynamicColor,
                    onKeepAwake = viewModel::setKeepAwake,
                )
            }
        }
    }
}
