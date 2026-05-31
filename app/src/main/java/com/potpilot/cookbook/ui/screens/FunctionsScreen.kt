package com.potpilot.cookbook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potpilot.cookbook.data.PotConcept
import com.potpilot.cookbook.data.PotFunction
import com.potpilot.cookbook.ui.components.InfoChip

@Composable
fun FunctionsScreen(
    functions: List<PotFunction>,
    concepts: List<PotConcept>,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(Modifier.padding(vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Instant Pot Duo programs", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "The 7-in-1 Duo's buttons and what each one is for.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        items(functions, key = { it.id }) { fn -> FunctionCard(fn) }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                "Good to know",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
            )
        }

        items(concepts, key = { it.title }) { concept -> ConceptCard(concept) }
    }
}

@Composable
private fun FunctionCard(fn: PotFunction) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(fn.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            Surface(
                color = if (fn.group == "Pressure") MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    if (fn.group == "Pressure") "Pressure program" else "No-pressure mode",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                )
            }
            Text(fn.summary, style = MaterialTheme.typography.bodyMedium)
            LabeledRow("Default", fn.defaultTime)
            LabeledRow("Pressure", fn.pressure)
            LabeledRow("Settings", fn.levels)
            LabeledRow("Release", fn.release)
            InfoChip("Best for: ${fn.bestFor}", modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun LabeledRow(label: String, value: String) {
    Column {
        Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ConceptCard(concept: PotConcept) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(concept.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(concept.body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
