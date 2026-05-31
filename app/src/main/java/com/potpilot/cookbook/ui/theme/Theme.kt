package com.potpilot.cookbook.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Terracotta,
    onPrimary = OnTerracotta,
    primaryContainer = TerracottaContainer,
    onPrimaryContainer = OnTerracottaContainer,
    secondary = WarmTaupe,
    onSecondary = OnWarmTaupe,
    secondaryContainer = WarmTaupeContainer,
    onSecondaryContainer = OnWarmTaupeContainer,
    tertiary = Saffron,
    onTertiary = OnSaffron,
    tertiaryContainer = SaffronContainer,
    onTertiaryContainer = OnSaffronContainer,
    background = WarmBackground,
    onBackground = OnWarmBackground,
    surface = WarmBackground,
    onSurface = OnWarmBackground,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = OnWarmSurfaceVariant,
    outline = WarmOutline,
)

private val DarkColors = darkColorScheme(
    primary = TerracottaDark,
    onPrimary = OnTerracottaDark,
    primaryContainer = TerracottaContainerDark,
    onPrimaryContainer = OnTerracottaContainerDark,
    secondary = WarmTaupeDark,
    onSecondary = OnWarmTaupeDark,
    secondaryContainer = WarmTaupeContainerDark,
    tertiary = SaffronDark,
    onTertiary = OnSaffronDark,
    tertiaryContainer = SaffronContainerDark,
    background = WarmBackgroundDark,
    onBackground = OnWarmBackgroundDark,
    surface = WarmBackgroundDark,
    onSurface = OnWarmBackgroundDark,
    surfaceVariant = WarmSurfaceVariantDark,
    onSurfaceVariant = OnWarmSurfaceVariantDark,
    outline = WarmOutlineDark,
)

@Composable
fun CookbookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        // Material You dynamic color is available on Android 12+ (works on the Android 13 tablet).
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CookbookTypography,
        shapes = CookbookShapes,
        content = content,
    )
}
