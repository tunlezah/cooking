package com.potpilot.cookbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potpilot.cookbook.data.AppContainer
import com.potpilot.cookbook.data.ThemeMode
import com.potpilot.cookbook.ui.CookbookApp
import com.potpilot.cookbook.ui.CookbookViewModel
import com.potpilot.cookbook.ui.theme.CookbookTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CookbookViewModel by viewModels {
        CookbookViewModel.factory(AppContainer(applicationContext))
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.settingsState.collectAsStateWithLifecycle()
            val darkTheme = when (settings.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            CookbookTheme(darkTheme = darkTheme, dynamicColor = settings.dynamicColor) {
                val windowSizeClass = calculateWindowSizeClass(this)
                CookbookApp(viewModel = viewModel, windowSizeClass = windowSizeClass)
            }
        }
    }
}
