package com.potpilot.cookbook.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { LIGHT, DARK, SYSTEM }

private val Context.dataStore by preferencesDataStore(name = "settings")

/** Persists user preferences: theme mode, dynamic color, keep-screen-on and favorites. */
class SettingsRepository(private val context: Context) {

    private val themeKey = stringPreferencesKey("theme_mode")
    private val dynamicKey = booleanPreferencesKey("dynamic_color")
    private val keepAwakeKey = booleanPreferencesKey("keep_awake")
    private val favoritesKey = stringSetPreferencesKey("favorites")

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        runCatching { ThemeMode.valueOf(prefs[themeKey] ?: ThemeMode.SYSTEM.name) }
            .getOrDefault(ThemeMode.SYSTEM)
    }

    val dynamicColor: Flow<Boolean> = context.dataStore.data.map { it[dynamicKey] ?: true }

    /** Keep the screen on while viewing a recipe. Defaults to true — handy with messy hands. */
    val keepAwake: Flow<Boolean> = context.dataStore.data.map { it[keepAwakeKey] ?: true }

    val favorites: Flow<Set<String>> = context.dataStore.data.map { it[favoritesKey] ?: emptySet() }

    suspend fun setThemeMode(mode: ThemeMode) =
        context.dataStore.edit { it[themeKey] = mode.name }

    suspend fun setDynamicColor(enabled: Boolean) =
        context.dataStore.edit { it[dynamicKey] = enabled }

    suspend fun setKeepAwake(enabled: Boolean) =
        context.dataStore.edit { it[keepAwakeKey] = enabled }

    suspend fun toggleFavorite(id: String) = context.dataStore.edit { prefs ->
        val current = prefs[favoritesKey] ?: emptySet()
        prefs[favoritesKey] = if (id in current) current - id else current + id
    }
}
