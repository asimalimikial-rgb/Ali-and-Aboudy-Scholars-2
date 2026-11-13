# Ali & Aboudy Scholars 2

An independent, offline-first Android learning app for two separate learner pathways:

- **Ali** — KG2
- **Aboudy** — Grade 2

The app uses a shared learning engine while keeping each learner's subjects, activities, attempts, progress, and recommendations isolated.

## Project status

Active source-development phase. Product name, curriculum mappings, and package/application identifiers remain subject to final review where noted.

**Build policy:** APK and Debug APK production, APK artifact uploads, and releases are intentionally disabled during this phase. CI is limited to source validation, JVM unit tests, and checked-in Room schema drift checks. Emulator instrumentation remains in source but is not executed during the no-APK phase because it builds and installs test APKs.

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
- Active-profile selection policy; stale, blank, or inactive persisted selections are cleared after Room has loaded profiles
- Learner chooser and persisted learner-home flow
- Versioned serializable content packs (schema v3)
- Skill definitions, structured single-choice activities, and source-reference metadata
- Validation for learner-path consistency, unique IDs, activity-to-skill and skill-to-source references, answer-option integrity, source HTTPS/license/usage metadata, safe image paths, feedback, language, and difficulty
- Reusable single-choice Activity Renderer with Arabic RTL and English LTR layout direction
- Pure Kotlin Evaluation Engine with activity-contract checks, invalid-option rejection, and content-provided feedback
- Activity Submission Service that validates learner/profile and content-pack pathway pairing, evaluates the answer, and writes a learner-scoped attempt to Room
- Learner-scoped progress calculation with accuracy, hinted versus independent correct attempts, separate recent streaks, and provisional mastery bands
- Explicit learner-and-skill DAO query paths for progress reads
- JVM unit tests for learner-selection policy, content validation and provenance, Arabic activity acceptance, answer evaluation, submission persistence via fake DAOs, learner isolation, streaks, and mastery thresholds
- Room schema v1 checked into source; CI verifies that the generated export remains identical
- Room foreign-key isolation tests retained in source, pending later emulator validation

## Important implementation boundaries

- Activity Renderer and Activity Submission Service are implemented as reusable components but are not yet connected to a content-pack repository or a complete activity navigation flow.
- The submission service currently stores learner ID, activity ID, skill ID, correctness, hint use, and timestamp. It does not yet persist the selected option or a feedback snapshot.
- Mastery bands are an initial deterministic rule set, not yet calibrated against educational research or child-specific learning data. Hint-assisted correct responses are reported separately and do not qualify for the independent mastery streak.
- Attempt rows are protected by a Room foreign key to the learner profile and all progress reads must remain learner-scoped.
- Source-reference metadata and validation are implemented, but no real content-pack manifest or verified source catalog has been supplied. No reviewed, complete curriculum/content pack is claimed at this stage.
- Room instrumentation tests remain unexecuted under the no-APK restriction; JVM tests do not prove SQLite foreign-key behavior on a device.

## Source validation

Requirements: JDK 17, Android SDK Platform 35, and Gradle 8.11.1.

```bash
gradle :app:testDebugUnitTest
```

The GitHub Actions workflow runs JVM unit tests and verifies that the generated Room schema matches the checked-in schema. It does **not** run emulator instrumentation, assemble an APK, or upload APK artifacts during the current source-development phase. Superseded runs on the same branch are cancelled automatically.


## Slice 0 verification and stage gates

### CI recovery record — 13 November 2026

- Run [#37](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/43375586084) failed during JVM test discovery: `ActivitySubmissionServiceTest > initializationError` reported JUnit `InvalidTestClassError`. The source review identified coroutine-backed test methods whose inferred return types were not JUnit 4-compatible.
- The test methods now explicitly use `runBlocking<Unit>` (commit `af7fb94fb95e7b97eab7b3a7418d07276748335c`).
- Run [#38](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/43376505419) passed the JVM unit-test task and confirmed that the generated Room schema matches the checked-in schema.
- A learner-scoped submission-isolation test was then added for Ali and Aboudy (commit `d6526609482538069ad4fd77d841a5c1e5c253ab`). Run [#39](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/43376959943) passed both the JVM unit-test task and Room schema check.
- These runs are source-only checks. They do not validate SQLite foreign-key behavior on a device.

### Remaining integration gates

- The Compose Activity Renderer supports single-choice activities and selects RTL for Arabic or LTR for English. It is not yet connected to a Content Pack repository or complete activity-navigation flow; option image assets are not yet rendered.
- The Activity Submission Service validates the active learner and pathway, evaluates the answer, and persists learner-scoped attempt metadata. It is not yet wired into the renderer/navigation flow and does not persist the selected option or a feedback snapshot.
- The Arabic/English implementation is per-activity language plus layout direction and button text; a complete language preference and paired localized-content workflow remain open.
- Mastery bands are deterministic provisional rules, not educationally calibrated thresholds.
- Content-source metadata and validation exist, but no reviewed Content Pack manifest or verified source/license catalog is present. Curriculum mapping and per-asset rights review remain blocked until those inputs exist.
- Room foreign-key and DAO isolation instrumentation tests remain in source but are intentionally not executed during the no-Test-APK phase.

## Provisional technical identifiers

The initial Android namespace and application ID are `com.asimalimikial.aas2`. Treat this as provisional until the product identity and release/application-ID decision is finalized. No APK or release should be produced from this development phase.
