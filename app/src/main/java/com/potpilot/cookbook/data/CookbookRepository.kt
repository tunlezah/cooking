package com.potpilot.cookbook.data

import android.content.Context
import kotlinx.serialization.json.Json

/**
 * Loads the bundled, offline recipe and function datasets from assets and caches them.
 * No network is used — everything ships inside the app.
 */
class CookbookRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    val recipes: List<Recipe> by lazy {
        val text = context.assets.open("recipes.json").bufferedReader().use { it.readText() }
        json.decodeFromString<RecipesFile>(text).recipes
    }

    val functions: List<PotFunction> by lazy { functionsFile.functions }
    val concepts: List<PotConcept> by lazy { functionsFile.concepts }

    private val functionsFile: FunctionsFile by lazy {
        val text = context.assets.open("functions.json").bufferedReader().use { it.readText() }
        json.decodeFromString<FunctionsFile>(text)
    }

    /** Distinct recipe categories in a sensible, stable display order. */
    val categories: List<String> by lazy {
        val order = listOf(
            "Soups & Stews", "Rice & Grains", "Beans & Lentils",
            "Chicken & Poultry", "Beef & Pork", "Pasta",
            "Steam & Veg", "Sauté", "Slow Cook",
            "Breakfast", "Yogurt & Dairy", "Desserts",
        )
        val present = recipes.map { it.category }.toSet()
        order.filter { it in present } + present.filter { it !in order }
    }

    fun byId(id: String): Recipe? = recipes.firstOrNull { it.id == id }

    fun inCategory(category: String): List<Recipe> = recipes.filter { it.category == category }
}
