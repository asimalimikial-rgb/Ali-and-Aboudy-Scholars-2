# Ali & Aboudy Scholar 2

An independent, offline-first learning product with a native Android app and companion web experience for two separate learner pathways:

- **Ali** — KG2
- **Aboudy** — Grade 2

The app uses a shared learning engine while keeping each learner's subjects, activities, attempts, progress, and recommendations isolated.

## Project status

Active product-development phase. `main` is the canonical branch and contains the Android app, restored learning website, launcher/PWA icon, and shared content assets. `foundation/slice-0-android` is retained as a working/history branch and is not the primary branch.

**Build policy:** Debug pre-releases for personal device testing are allowed only after Android CI validation. Production signing and production releases remain disabled.

### Current Android source and release policy

- **Canonical source:** `main`. The latest reviewed changes are merged only after Android CI and content/PWA validation pass.
- **Production learning website:** [https://aas2-web.vercel.app](https://aas2-web.vercel.app).
- **Android launcher icon:** `app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp`; the matching PWA icon is `docs/assets/aas2-icon-192.webp`.
- **Application ID:** `com.asimalimikial.aas2`.
- **App version for the next preview:** `0.1.1` (versionCode `2`).
- **Release policy:** an installable debug preview is published only after the Android CI workflow passes. Production signing remains disabled; this is not a production-signed release.

### Previous Android pre-release (historical) — 27 November 2026

- **GitHub Release:** [v0.1.0-preview.457.1](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/releases/tag/v0.1.0-preview.457.1)
- **APK asset:** `Ali-Aboudy-Scholars-2-v0.1.0-preview.457.1.apk` (40,767,819 bytes; about 38.9 MiB).
- **App version:** 0.1.0 (versionCode 1); **package ID:** `com.asimalimikial.aas2`.
- **SHA-256:** `e76731a8ac5f2edb8e862cdba845f587a4d029a8e7655b89dab02108aa83867a`.
- **Release source:** `foundation/slice-0-android`, commit `a4a5dc216eb7afee1b4b379e3ce7281e0abe92ca`.
- Android CI [#457](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/46013091450) passed JVM tests, Debug/AndroidTest compilation, Room schema v1/v2 verification and temporary APK artifact handoff. API 35 emulator instrumentation passed in PR validation [#450](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/46008491095).
- This is a **debug pre-release for personal device testing**, not a production-signed release. Physical-device acceptance and visual/audio QA on the target handset remain open. The repository and this pre-release are publicly accessible through the linked GitHub Release.

### Historical execution update — 24 November 2026

- The seven-book catalog is in `content/catalog/seven-book-catalog.json`; SMILE remains outside scope. The current coverage plan is `docs/seven-book-coverage-plan-v0.3.md`. Seven cover references and their ISBN/source notes are indexed in `docs/reference-book-covers-v0.1.md`; these are remote, reference-only images and are not bundled in the APK.
- Current content packs contain 282 English activities: 108 KG2 and 174 Grade 2. Only Macmillan Science Level 2 currently has runtime book/unit/lesson mapping (62 activities across two units and nine lessons); other book hierarchies and mappings remain open.
- Verified offline media: 1,692 English OGG clips for 282 activities at speed 0.70; 282 question illustrations (245 SVG and 37 PNG); 378 option illustrations. All generated media are checked against file sizes and SHA-256 values by content validation. Manifest paths and hashes are unique. Physical-device audio playback and visual accessibility checks remain open.
- The 616-item legacy bank has 468 strict single-choice candidates, 28 specialized option-bearing interactions and 120 custom no-option interactions. The renderer audit records 182 items requiring specialized interactions or structured stimuli, plus 69 listening prompts that reveal their target visually and must remain blocked from listening-assessment use until corrected.
- Twelve proposed legacy corrections are staged in `content/staging/legacy-question-bank-correction-patch-v0.1.json`; all remain `REVISED_PENDING_TECHNICAL_QA` and none is production-approved. The legacy source repository remains untouched.
- The official English World Level 2 12-unit scope is recorded in `docs/english-world-level2-scope-map-v0.1.md`. Its 35 current activities include 15 candidate alignments and 20 supplementary/unmapped items; none is yet runtime-mapped. Mathematics 2A/2B has 40 candidate mappings (31/9), also pending pedagogy review.
- Android CI #329 passed on commit `f99e648`. A stale content-validator count (9 versus 12 staged correction records) was corrected in commit `eedc485`; Content Validation #17 and Android CI #330 were started for that commit. Later documentation commits add the cover index and this README update; their workflow results must be checked separately before declaring the branch green.
- This historical record predates the current `main` build above. Signing configuration and production-release policy remain unchanged; the current debug APK is published separately for device testing.

## Principles

- Offline-first; no account or cloud dependency in the MVP.
- Native Android: Kotlin and Jetpack Compose.
- Local persistence with Room and DataStore.
- Versioned, validated JSON content packs.
- Original learning prompts and assets; no reproduction of protected textbook pages, text, illustrations, or exercises.
- No analytics SDK or unnecessary network permissions in the MVP.

## Repository boundary

This repository is **AAS2-NEW** only. It is independent of the legacy repository `Ali-andd-aboudy-scholars`. Do not copy, merge, move, or modify legacy project files as part of this work.

## Android implementation

The canonical source branch is `main`. The `foundation/slice-0-android` branch remains preserved as a working/history branch.

- Kotlin + Jetpack Compose
- Room database for separate learner profiles and learner-scoped learning-attempt records (schema v2)
- DataStore persistence for the selected learner, with blank IDs rejected
- Active-profile selection policy; stale, blank, or inactive persisted selections are cleared after Room has loaded profiles
- Learner chooser and persisted learner-home flow, localized through Android `values` and `values-ar` resources according to the device language, including launcher label, learner names, and pathway labels
- Learning flow wired into MainActivity: each learner loads only their own bundled pilot pack; child activities are English-only regardless of device locale, grouped into age-appropriate learning areas, and learner-scoped attempts are persisted in Room
- Question-first session entry: the first unpractised English activity opens immediately; returning to the activity catalogue shows remaining activities and practice markers
- Learner home derives its available learning areas and activity counts from the actual bundled pack instead of displaying static placeholder subjects
- Incorrect answers use neutral feedback and an explicit retry action; every retry is persisted as a separate attempt
- Versioned serializable content packs (schema v3), with an offline asset repository that validates safe pack IDs, declared identity, JSON format, and pack integrity before returning a pack
- Skill definitions, structured single-choice activities, and source-reference metadata
- Validation for learner-path consistency, unique IDs, activity-to-skill and skill-to-source references, answer-option integrity, source HTTPS/license/usage metadata, safe image paths, feedback, language, and difficulty
- Reusable single-choice Activity Renderer with Arabic RTL and English LTR layout direction, accessible optional offline raster illustrations, localized answer labels, and localized save-error feedback; supplied question audio starts automatically, supplied correct/incorrect feedback audio plays after evaluation, and the question clip is stopped before feedback.
- Pure Kotlin Evaluation Engine with activity-contract checks, invalid-option rejection, and content-provided feedback
- Activity Submission Service that validates learner/profile and content-pack pathway pairing, evaluates the answer, and writes a learner-scoped attempt snapshot to Room (content-pack ID/version, selected option ID, correctness, hint use, feedback text, and timestamp)
- Compose Activity Session wrapper connecting the renderer to the submission service, preventing duplicate in-flight submissions and showing success feedback only after persistence succeeds
- Learner-scoped progress calculation with accuracy, hinted versus independent correct attempts, separate recent streaks, and provisional mastery bands
- Learner-home practice summary sourced from the selected learner's Room attempts, showing scored attempts, correct answers, accuracy, and hinted versus independent correct answers
- Read-only Parent Dashboard shows the selected learner's per-skill attempt evidence and accuracy; access requires the Android device credential, and the dashboard closes when the app leaves the foreground
- Parent Settings are accessible only from the protected Parent Dashboard; parent UI language (follow device / English / Arabic) is persisted in DataStore and scoped to parent screens, while learner-facing screens keep the device locale and child activities remain English-only
- Explicit learner-and-skill DAO query paths for progress reads
- JVM unit tests for learner-selection policy, content validation and provenance, offline content-pack loading and failure handling, Arabic activity acceptance, answer evaluation, submission persistence via fake DAOs, additive Room migration SQL, learner isolation, streaks, and mastery thresholds
- Room schema versions 1 and 2 checked into source; CI verifies both generated exports, with an additive 1→2 migration that preserves legacy attempts
- Room foreign-key isolation tests retained in source; Android instrumentation tests passed on the API 35 emulator in CI #450.
- Parent language policy JVM tests cover supported settings, fallback to system locale, RTL selection and isolation from learner-facing screens

## Important implementation boundaries

- `AssetContentPackRepository` loads and validates bundled JSON packs from `assets/content-packs/{packId}.json` and is wired into `MainActivity`, `LearningExperienceScreen`, and the read-only `ParentDashboardScreen`. The learning screen opens the next unpractised English activity, then supports subject-grouped catalogue navigation and learner-scoped progress.
- Legacy attempt rows retain null values for the snapshot fields introduced in schema v2 because the earlier app version never recorded those values. New submissions persist the content-pack identity/version, selected option ID, and the exact feedback text shown at evaluation time.
- Optional raster option images are loaded from validated local asset paths, downsampled off the main thread, and omitted safely when missing or corrupt. The KG2 v0.2.0 pack now references six original offline PNG illustrations for shape recognition and initial-sound choices; Grade 2 now has original images for its two habitat-choice activities; visual coverage of the other Grade 2 skills remains open. A JVM asset-integrity test verifies that every referenced image exists and has a PNG signature.
- Mastery bands are an initial deterministic rule set, not yet calibrated against educational research or child-specific learning data. Hint-assisted correct responses are reported separately and do not qualify for the independent mastery streak.
- Attempt rows are protected by a Room foreign key to the learner profile and all progress reads must remain learner-scoped.
- A verified source and rights register exists, and two small schema-v3 pilot packs are bundled and validated. Legacy Arabic variants remain in those files but are not selected by the child learning runtime; they are not a complete curriculum. Additional reviewed content, a versioned content-pack catalog, and expanded curriculum coverage remain open.
- The migration JVM test verifies the four nullable `ALTER TABLE ... ADD COLUMN` statements and rejects destructive table-drop SQL; it does not execute the migration against a real SQLite database. Room foreign-key and actual migration instrumentation tests remain unexecuted under the no-APK restriction.

## Source validation

Requirements: JDK 17, Android SDK Platform 35, and Gradle 8.11.1.

```bash
gradle :app:testDebugUnitTest
```

The GitHub Actions PR workflow runs JVM unit tests, compiles the debug app and Android test APKs, verifies Room schema v1/v2, and runs instrumentation tests on an API 35 emulator. Content validation also checks media checksums and JavaScript syntax for the offline website and Service Worker. The CI-gated preview-release workflow builds an installable Debug APK from the exact commit that passed Android CI, calculates SHA-256, and publishes the APK plus checksum as a GitHub pre-release. Production signing configuration is not modified. Instrumentation status must be checked separately from APK assembly; passing APK assembly does not imply that every emulator test passed.

## Slice 0 verification and stage gates