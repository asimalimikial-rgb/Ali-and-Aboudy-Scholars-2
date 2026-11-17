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
- Room database for separate learner profiles and learner-scoped learning-attempt records (schema v2)
- DataStore persistence for the selected learner, with blank IDs rejected
- Active-profile selection policy; stale, blank, or inactive persisted selections are cleared after Room has loaded profiles
- Learner chooser and persisted learner-home flow, localized through Android `values` and `values-ar` resources according to the device language, including launcher label, learner names, and pathway labels
- Versioned serializable content packs (schema v3), with an offline asset repository that validates safe pack IDs, declared identity, JSON format, and pack integrity before returning a pack
- Skill definitions, structured single-choice activities, and source-reference metadata
- Validation for learner-path consistency, unique IDs, activity-to-skill and skill-to-source references, answer-option integrity, source HTTPS/license/usage metadata, safe image paths, feedback, language, and difficulty
- Reusable single-choice Activity Renderer with Arabic RTL and English LTR layout direction, content-language action labels, and localized save-error feedback
- Pure Kotlin Evaluation Engine with activity-contract checks, invalid-option rejection, and content-provided feedback
- Activity Submission Service that validates learner/profile and content-pack pathway pairing, evaluates the answer, and writes a learner-scoped attempt snapshot to Room (content-pack ID/version, selected option ID, correctness, hint use, feedback text, and timestamp)
- Compose Activity Session wrapper connecting the renderer to the submission service, preventing duplicate in-flight submissions and showing success feedback only after persistence succeeds
- Learner-scoped progress calculation with accuracy, hinted versus independent correct attempts, separate recent streaks, and provisional mastery bands
- Explicit learner-and-skill DAO query paths for progress reads
- JVM unit tests for learner-selection policy, content validation and provenance, offline content-pack loading and failure handling, Arabic activity acceptance, answer evaluation, submission persistence via fake DAOs, additive Room migration SQL, learner isolation, streaks, and mastery thresholds
- Room schema versions 1 and 2 checked into source; CI verifies both generated exports, with an additive 1→2 migration that preserves legacy attempts
- Room foreign-key isolation tests retained in source, pending later emulator validation

## Important implementation boundaries

- `AssetContentPackRepository` can load and validate bundled JSON packs from `assets/content-packs/{packId}.json`. It is not yet wired into `MainActivity` or a complete activity-navigation flow; the caller must still provide the selected learner, content pack, activity, and submission service to `ActivitySession`.
- Legacy attempt rows retain null values for the snapshot fields introduced in schema v2 because the earlier app version never recorded those values. New submissions persist the content-pack identity/version, selected option ID, and the exact feedback text shown at evaluation time.
- Option image assets are validated as safe relative paths but are not yet rendered by the Compose activity UI.
- Mastery bands are an initial deterministic rule set, not yet calibrated against educational research or child-specific learning data. Hint-assisted correct responses are reported separately and do not qualify for the independent mastery streak.
- Attempt rows are protected by a Room foreign key to the learner profile and all progress reads must remain learner-scoped.
- Source-reference metadata and validation are implemented, but no real content-pack manifest or verified source catalog has been supplied. No reviewed, complete curriculum/content pack is claimed at this stage.
- The migration JVM test verifies the four nullable `ALTER TABLE ... ADD COLUMN` statements and rejects destructive table-drop SQL; it does not execute the migration against a real SQLite database. Room foreign-key and actual migration instrumentation tests remain unexecuted under the no-APK restriction.

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
- Run [#40](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/43377367069) on the documented branch head passed both JVM unit tests and checked-in Room schema verification. It ran before the Activity Session integration in the next change set.
- These runs are source-only checks. They do not validate SQLite foreign-key behavior on a device.

### Recent persistence and Room schema verification — 17–18 November 2026

- Run [#46](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/44077621582) passed JVM unit tests and the checked-in Room schema v1 check after the source URL/date validation fix.
- Commit `09e1de15e526439873a7be921a5d21752119e415` added attempt snapshots and the additive Room 1→2 migration. Run [#47](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/44192429377) passed JVM tests and generated schema v2.
- Commit `c11e638d75eb0477a17215edc43d8f6662ed462f` checked in the generated schema v2 and updated CI to verify both schema versions. Run [#48](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/44193447233) passed both JVM tests and schema v1/v2 checks.
- Commit `4238f512c712503b42b6adcdde725e9690912caa` added a JVM test for the additive migration SQL contract. Run [#49](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/44193959359) passed all JVM tests and schema checks.
- Runs #47–#49 produced zero workflow artifacts. No APK, Test APK, instrumentation, emulator run, or release was created.

### Remaining integration gates

- The repository infrastructure exists, but no actual content-pack JSON assets or manifest have been supplied, and no complete activity-navigation flow exists yet.
- The app shell now follows the Android device language through `values` and `values-ar`; activity text and action labels follow the selected content pack language. An in-app language picker and paired localized-content workflow remain open.
- Option image assets still need a safe offline asset loader and renderer.
- Mastery bands are deterministic provisional rules, not educationally calibrated thresholds.
- Content-source metadata and validation exist, but no reviewed Content Pack manifest or verified source/license catalog is present. Curriculum mapping and per-asset rights review remain blocked until those inputs exist.
- Room foreign-key and DAO isolation instrumentation tests remain in source but are intentionally not executed during the no-Test-APK phase.

## Provisional technical identifiers

The initial Android namespace and application ID are `com.asimalimikial.aas2`. Treat this as provisional until the product identity and release/application-ID decision is finalized. No APK or release should be produced from this development phase.
