package com.potpilot.cookbook.data

import android.content.Context

/**
 * Tiny manual dependency container — no DI framework needed at this size.
 * Holds the singleton repositories for the app.
 */
class AppContainer(context: Context) {
    val cookbook = CookbookRepository(context.applicationContext)
    val settings = SettingsRepository(context.applicationContext)
}
