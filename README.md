# Ali & Aboudy Scholars 2

An independent, offline-first Android learning app for two separate learner pathways:

- **Ali** — KG2
- **Aboudy** — Grade 2

The app uses a shared learning engine while keeping each learner's subjects, activities, attempts, progress, and recommendations isolated.

## Project status

Early foundation stage. Product name, curriculum mappings, and package/application identifiers remain subject to final review where noted in the project decision log.

## Principles

- Offline-first; no account or cloud dependency in the MVP.
- Native Android: Kotlin and Jetpack Compose.
- Local persistence with Room and DataStore.
- Versioned, validated JSON content packs.
- Original learning prompts and assets; no reproduction of protected textbook pages, text, illustrations, or exercises.
- No analytics SDK or unnecessary network permissions in the MVP.

## Repository boundary

This repository is **AAS2-NEW** only. It is independent of the legacy repository `Ali-andd-aboudy-scholars`. Do not copy, merge, move, or modify legacy project files as part of this work.

## Android foundation

The initial foundation branch is `foundation/slice-0-android`.

- Kotlin + Jetpack Compose
- Room database for learner profiles and learning-attempt records
- DataStore preferences
- Versioned Kotlin-serializable content-pack models and a first validation layer
- Unit tests for valid packs, duplicate skill IDs, and mixed learner pathways
- GitHub Actions workflow for unit tests and debug APK assembly

The current UI is a foundation-only learner chooser. Activity navigation, parent controls, the full learning engine, and curriculum content are not implemented yet.

## Build locally

Requirements: JDK 17, Android SDK Platform 35, and Gradle 8.11.1.

```bash
gradle :app:testDebugUnitTest
gradle :app:assembleDebug
```

The CI workflow installs the Android SDK packages and runs these commands on pushes to `main` and `foundation/**`, and on pull requests targeting `main`.

## Provisional technical identifiers

The initial Android namespace and application ID are `com.asimalimikial.aas2`. Treat this as provisional until the product identity and release/application-ID decision is finalized. Do not publish a release APK from this foundation branch.
