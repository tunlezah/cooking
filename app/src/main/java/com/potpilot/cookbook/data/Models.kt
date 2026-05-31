package com.potpilot.cookbook.data

import kotlinx.serialization.Serializable

/** A single simple Instant Pot recipe. Matches assets/recipes.json. */
@Serializable
data class Recipe(
    val id: String,
    val title: String,
    val category: String,
    val function: String,
    val servings: Int,
    val prep: Int,
    val cook: Int,
    val release: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val tags: List<String> = emptyList(),
    val tip: String = "",
) {
    /** A rough total of hands-on prep plus the pressure/cook countdown (excludes come-to-pressure & release). */
    val activeMinutes: Int get() = prep + cook

    val isSimple: Boolean get() = ingredients.size <= 6 || prep <= 10
}

@Serializable
data class RecipesFile(val recipes: List<Recipe>)

/** An Instant Pot Duo program/button. Matches the "functions" array in assets/functions.json. */
@Serializable
data class PotFunction(
    val id: String,
    val name: String,
    val group: String,
    val summary: String,
    val defaultTime: String,
    val pressure: String,
    val levels: String,
    val release: String,
    val bestFor: String,
)

/** A general operating concept (release methods, fill lines, etc.). */
@Serializable
data class PotConcept(
    val title: String,
    val body: String,
)

@Serializable
data class FunctionsFile(
    val functions: List<PotFunction>,
    val concepts: List<PotConcept>,
)
