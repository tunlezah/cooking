package com.potpilot.cookbook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potpilot.cookbook.data.Recipe
import com.potpilot.cookbook.ui.components.InfoChip
import com.potpilot.cookbook.ui.components.NumberedList
import com.potpilot.cookbook.ui.components.SectionHeader
import com.potpilot.cookbook.ui.components.StepsList
import com.potpilot.cookbook.ui.components.categoryEmoji
import com.potpilot.cookbook.ui.util.KeepScreenOn

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecipeDetail(
    recipe: Recipe,
    isFavorite: Boolean,
    keepAwake: Boolean,
    onToggleFavorite: () -> Unit,
    showBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Keep the display awake while a recipe is open (honours the user setting).
    KeepScreenOn(enabled = keepAwake)

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(recipe.title) },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(inner)
                // Keep the reading column comfortable even on a wide tablet.
                .widthIn(max = 720.dp)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Quick-facts row
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoChip("${categoryEmoji(recipe.category)} ${recipe.category}")
                InfoChip("⚙ ${recipe.function}")
                InfoChip("🍽 Serves ${recipe.servings}")
                InfoChip("⏱ ${recipe.prep} min prep")
                InfoChip("🔥 ${recipe.cook} min cook")
                InfoChip("♨ ${recipe.release}")
            }

            // Pressure-release explainer card
            ReleaseHint(recipe.release)

            SectionHeader("Ingredients")
            NumberedList(items = recipe.ingredients, numbered = false)

            SectionHeader("Method")
            StepsList(steps = recipe.steps)

            if (recipe.tip.isNotBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("💡 Tip", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(recipe.tip, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            if (recipe.tags.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    recipe.tags.forEach { InfoChip("#$it") }
                }
            }
        }
    }
}

@Composable
private fun ReleaseHint(release: String) {
    val text = when {
        release.startsWith("Natural", ignoreCase = true) && release.contains("then", ignoreCase = true) ->
            "Let pressure release naturally for the stated time, then turn the valve to Venting to quick-release the rest."
        release.startsWith("Natural", ignoreCase = true) ->
            "Let the pressure come down on its own — the float valve drops when it's safe to open."
        release.equals("Quick", ignoreCase = true) ->
            "When the timer ends, carefully turn the valve to Venting. Keep hands and face clear of the steam."
        else ->
            "No pressure release needed for this program."
    }
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Release: ${release}", style = MaterialTheme.typography.titleMedium)
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
