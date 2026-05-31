package com.potpilot.cookbook.ui.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Keeps the device screen awake while this composable is in composition, then clears the flag
 * automatically when it leaves (e.g. the user navigates away). Ideal for the recipe-cooking
 * screen so the display doesn't sleep mid-recipe with messy hands.
 *
 * @param enabled when false, the flag is never set (honours the user's setting).
 */
@Composable
fun KeepScreenOn(enabled: Boolean = true) {
    if (!enabled) return
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = context.findActivity()?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
