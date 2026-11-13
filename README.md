# Ali & Aboudy Scholars 2

An independent, offline-first Android learning app for two separate learner pathways:

- **Ali** — KG2
- **Aboudy** — Grade 2

The app is designed around a shared learning engine while keeping each learner's subjects, activities, attempts, progress, and recommendations isolated.

## Project status

Active source-development phase. Product name, curriculum mappings, and package/application identifiers remain subject to final review where noted.

**Build policy:** APK and Debug APK production, APK artifact uploads, and releases are intentionally disabled during this phase. CI is limited to source validation, JVM unit tests, and Room schema checks. Emulator instrumentation will be reintroduced only when the project is ready for device-validation and APK production is explicitly authorized.

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

The active development branch is `foundation/slice-0-android`.

- Kotlin + Jetpack Compose
- Room database for learner profiles and learning-attempt records
- DataStore preferences
- Versioned Kotlin-serializable content-pack models and validation
- Unit tests for content-pack validation
- Room foreign-key isolation for learner attempts, with instrumentation tests currently retained in source but not executed in CI during the no-APK phase

The current UI is still a foundation-stage learner chooser. A complete learner home, activity navigation, parent controls, adaptive learning, and curriculum content remain to be implemented.

## Source validation

Requirements: JDK 17, Android SDK Platform 35, and Gradle 8.11.1.

```bash
gradle :app:testDebugUnitTest
```

The GitHub Actions workflow runs JVM unit tests and verifies the Room schema export. It does **not** run emulator instrumentation, assemble an APK, or upload APK artifacts during the current source-development phase.

## Provisional technical identifiers

The initial Android namespace and application ID are `com.asimalimikial.aas2`. Treat this as provisional until the product identity and release/application-ID decision is finalized. No APK or release should be produced from this development phase.
