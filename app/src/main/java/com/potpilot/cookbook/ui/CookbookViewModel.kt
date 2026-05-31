package com.potpilot.cookbook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.potpilot.cookbook.data.AppContainer
import com.potpilot.cookbook.data.PotConcept
import com.potpilot.cookbook.data.PotFunction
import com.potpilot.cookbook.data.Recipe
import com.potpilot.cookbook.data.SettingsRepository
import com.potpilot.cookbook.data.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = true,
    val keepAwake: Boolean = true,
    val favorites: Set<String> = emptySet(),
)

/**
 * Single app-scoped ViewModel. The dataset is small and static, so the cookbook data is
 * exposed directly and only the user preferences/favorites flow reactively.
 */
class CookbookViewModel(private val container: AppContainer) : ViewModel() {

    private val settings: SettingsRepository = container.settings

    val recipes: List<Recipe> get() = container.cookbook.recipes
    val categories: List<String> get() = container.cookbook.categories
    val functions: List<PotFunction> get() = container.cookbook.functions
    val concepts: List<PotConcept> get() = container.cookbook.concepts

    fun recipe(id: String): Recipe? = container.cookbook.byId(id)
    fun inCategory(category: String): List<Recipe> = container.cookbook.inCategory(category)

    val settingsState: StateFlow<SettingsUiState> = combine(
        settings.themeMode,
        settings.dynamicColor,
        settings.keepAwake,
        settings.favorites,
    ) { theme, dynamic, awake, favs ->
        SettingsUiState(theme, dynamic, awake, favs)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setTheme(mode: ThemeMode) = viewModelScope.launch { settings.setThemeMode(mode) }
    fun setDynamicColor(on: Boolean) = viewModelScope.launch { settings.setDynamicColor(on) }
    fun setKeepAwake(on: Boolean) = viewModelScope.launch { settings.setKeepAwake(on) }
    fun toggleFavorite(id: String) = viewModelScope.launch { settings.toggleFavorite(id) }

    companion object {
        fun factory(container: AppContainer) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                CookbookViewModel(container) as T
        }
    }
}
