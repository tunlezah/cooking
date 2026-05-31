# PotPilot 🍲

A sleek, fully‑offline Android cooking app of **simple recipes for the Instant Pot Duo 5.7L (6‑Qt) multicooker** — the classic 7‑in‑1 model. Built with Jetpack Compose + Material 3, designed to look great on an 8" Android 13 tablet **and** on phones.

## What's inside

- **85 simple recipes** across 12 categories — every one chosen for a short ingredient list *or* easy prep. Spans soups & stews, rice & grains, beans & lentils, chicken, beef & pork, pasta, steamed dishes, sauté, slow cook, breakfast, yogurt and desserts, across many cuisines.
- **A full Instant Pot Duo function reference** — all 13 program buttons (Pressure Cook, Soup/Broth, Meat/Stew, Bean/Chili, Poultry, Rice, Multigrain, Porridge, Steam, Sauté, Slow Cook, Yogurt, Keep Warm) with default times, pressure level, adjustable settings and what each is for — plus "good to know" cards on natural vs quick release, minimum liquid, fill lines and come‑to‑pressure time.
- Each recipe shows servings, prep/cook time, the **pressure‑release method with a plain‑English explainer**, ingredients, numbered method, a tip and tags. Searchable and filterable by category, with favourites.

## Key features

- 🎨 **Light / Dark / System** theme switch (persisted) — a Material 3 segmented toggle.
- 🌈 **Material You dynamic colour** on Android 12+ with a warm terracotta brand fallback.
- ☀️ **Keep screen on while cooking** — the recipe screen holds the display awake (toggleable) so it doesn't sleep with messy hands.
- 📱➡️🖥️ **Responsive** — an adaptive navigation bar on phones becomes a rail/drawer on tablets, the recipe grid reflows to more columns, and on a wide tablet the list and recipe show **side‑by‑side** (two‑pane).
- 📦 **100% offline** — recipes and the function guide ship as bundled JSON assets; no network, no accounts.

## Tech stack (verified stable for Android 13 / API 33)

| | |
|---|---|
| Language / UI | Kotlin 2.1.0, Jetpack Compose (BOM 2025.05.01), Material 3 |
| Build | AGP 8.7.3, Gradle 8.9, JDK 17 |
| SDK | `minSdk 26`, `compileSdk 35`, `targetSdk 35` |
| Libraries | Navigation‑Suite + Material3 Adaptive, WindowSizeClass, DataStore (prefs/favourites), kotlinx.serialization |

Dynamic colour needs Android 12+, handled with a runtime version check; everything else runs on Android 8+.

## Project layout

```
app/src/main/
├── assets/
│   ├── recipes.json        # 85 recipes
│   └── functions.json      # Instant Pot Duo program reference + concepts
├── java/com/potpilot/cookbook/
│   ├── MainActivity.kt           # edge‑to‑edge, window size class, theme wiring
│   ├── data/                     # models, repositories (cookbook + settings), DI container
│   └── ui/
│       ├── theme/                # warm Material 3 colour/type/shape + dynamic colour
│       ├── components/           # RecipeCard, chips, lists
│       ├── screens/              # Recipes (+ two‑pane), RecipeDetail, Favourites, Functions, Settings
│       ├── util/KeepScreenOn.kt  # DisposableEffect FLAG_KEEP_SCREEN_ON
│       ├── CookbookViewModel.kt
│       └── CookbookApp.kt        # adaptive NavigationSuiteScaffold
└── res/                          # themes (day/night), adaptive launcher icon, colours
```

## Build

```bash
./gradlew assembleDebug
```

Requires the Android SDK (set `ANDROID_HOME` / `local.properties`). Open in Android Studio (Koala/Ladybug or newer) and run on an Android 13 tablet or phone.

> Recipe times are accurate Instant Pot guidance, but always follow your model's manual and food‑safety basics (e.g. fully cooking poultry and kidney beans).
