# Ali & Aboudy Scholars 2

An independent, offline-first Android learning app for two separate learner pathways:

- **Ali** — KG2
- **Aboudy** — Grade 2

The app uses a shared learning engine while keeping each learner's subjects, activities, attempts, progress, and recommendations isolated.

## Project status

Active source-development phase. Product name, curriculum mappings, and package/application identifiers remain subject to final review where noted.

**Build policy:** APK and Debug APK production, APK artifact uploads, and releases are intentionally disabled during this phase. CI is limited to source validation, JVM unit tests, and Room schema checks. Emulator instrumentation remains in source but is not executed during the no-APK phase because it builds and installs test APKs.

## Principles

- Offline-first; no account or cloud dependency in the MVP.
- Native Android: Kotlin and Jetpack Compose.
- Local persistence with Room and DataStore.
- Versioned, validated JSON content packs.
- Original learning prompts and assets; no reproduction of protected textbook pages, text, illustrations, or exercises.
- No analytics SDK or unnecessary network permissions in the MVP.

## Repository boundary

This repository is **AAS2-NEW** only. It is independent of the legacy repository `Ali-andd-aboudy-scholars`. Do not copy, merge, move, or modify legacy project files as part of this work.

## Current foundation

The active development branch is `foundation/slice-0-android`.

- Kotlin + Jetpack Compose
- Room database for separate learner profiles and learning-attempt records
- DataStore persistence for the selected learner, with blank IDs rejected
- Learner chooser and persisted learner-home flow
- Versioned serializable content packs (schema v2)
- Skill definitions and structured single-choice activities
- Validation for learner-path consistency, unique IDs, activity-to-skill references, answer-option integrity, source-reference ID uniqueness, safe image paths, feedback, language, and difficulty
- Reusable single-choice Activity Renderer (caller supplies validated content and persists results)
- Pure Kotlin Evaluation Engine with invalid-option rejection and content-provided feedback
- Learner-scoped skill-progress calculation (accuracy, correct streak, and provisional mastery bands)
- Explicit learner-and-skill DAO query paths for progress reads
- JVM unit tests for content validation, answer evaluation, learner isolation in progress calculations, streaks, and mastery thresholds
- Room foreign-key isolation tests retained in source, pending later emulator validation

## Important implementation boundaries

- Activity Renderer is reusable but is not yet connected to a content-pack repository or a full activity navigation flow.
- Evaluation returns a result; the caller must persist a `LearningAttemptEntity` after a valid submission.
- Mastery bands are an initial deterministic rule set, not yet calibrated against educational research or child-specific learning data.
- Attempt rows are protected by a Room foreign key to the learner profile and all progress reads must remain learner-scoped.
- Source IDs are checked for blanks and duplicates, but a source manifest with verified publisher, attribution, URL, and license metadata remains future work.
- No reviewed, complete curriculum/content pack is claimed at this stage.

## Source validation

Requirements: JDK 17, Android SDK Platform 35, and Gradle 8.11.1.

```bash
gradle :app:testDebugUnitTest
```

The GitHub Actions workflow runs JVM unit tests and verifies the Room schema export. It does **not** run emulator instrumentation, assemble an APK, or upload APK artifacts during the current source-development phase. Superseded runs on the same branch are cancelled automatically.

## Provisional technical identifiers

The initial Android namespace and application ID are `com.asimalimikial.aas2`. Treat this as provisional until the product identity and release/application-ID decision is finalized. No APK or release should be produced from this development phase.
