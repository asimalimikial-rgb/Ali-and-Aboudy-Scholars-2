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
- Learning flow wired into MainActivity: each learner loads only their own bundled pilot pack; child activities are English-only regardless of device locale, grouped into age-appropriate learning areas, and learner-scoped attempts are persisted in Room
- Question-first session entry: the first unpractised English activity opens immediately; returning to the activity catalogue shows remaining activities and practice markers
- Learner home derives its available learning areas and activity counts from the actual bundled pack instead of displaying static placeholder subjects
- Incorrect answers use neutral feedback and an explicit retry action; every retry is persisted as a separate attempt
- Versioned serializable content packs (schema v3), with an offline asset repository that validates safe pack IDs, declared identity, JSON format, and pack integrity before returning a pack
- Skill definitions, structured single-choice activities, and source-reference metadata
- Validation for learner-path consistency, unique IDs, activity-to-skill and skill-to-source references, answer-option integrity, source HTTPS/license/usage metadata, safe image paths, feedback, language, and difficulty
- Reusable single-choice Activity Renderer with Arabic RTL and English LTR layout direction, accessible optional offline raster illustrations, localized answer labels, and localized save-error feedback
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
- Room foreign-key isolation tests retained in source, pending later emulator validation
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

### Recent offline content and localization verification — 18 November 2026

- Commit `a00d14ed6a72077d108e24fae7bd194567ddc09f` added the offline asset-based `ContentPackRepository`, safe pack-ID validation, strict JSON decoding, and distinct read/format/validation errors. Run [#51](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/44194965564) passed all JVM tests and Room schema checks.
- Commit `d467fe13e01a4d22678f8c0eddb5ede36ddfc432` added Arabic resource strings for the learner shell and content-language-specific activity actions/errors. Run [#52](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/44195740216) passed.
- Commit `aba6b713a63cb63f47c5a73a08791d91ffc52ed3` localized the Android launcher label, learner display names, and KG2/Grade 2 pathway labels. Run [#53](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/44196357440) passed.
- Runs #51–#53 produced zero workflow artifacts. No APK, Test APK, instrumentation test, emulator test, or release was created. Run #50 was cancelled when the newer content-repository commit superseded it; Run #51 is the successful verification for that code.

### Initial original-content pilot packs

- Bundled assets: `aas2-kg2-foundation` (content v0.2.2, schema v3; 4 skills, 12 records, 8 English) and `aas2-grade2-foundation` (content v0.3.2, schema v4; 13 skills, 50 records, 44 English). The Grade 2 pack now includes a navigable AAS2 Science book structure with Unit 1, four lessons and 32 original activities aligned to Macmillan Science topics 1–4. Arabic legacy records remain stored but are not exposed by the child runtime.
- These are small foundation pilots, not complete KG2 or Grade 2 curricula. Their activity wording and options are original; copyrighted textbook passages, exercises, illustrations, and scripts are not included.
- `AssetContentPackRepository` loads and validates these assets through the MainActivity learning flow and read-only Parent Dashboard. A versioned pack catalog/manifest, expanded curriculum coverage, and richer skill-level progress remain open.

### Content provenance and licensing

- The source and rights register is maintained in [`docs/content-source-register.md`](docs/content-source-register.md).
- Production packs must distinguish reference-only curriculum sources from sources whose exact edition permits redistribution/adaptation. Every skill must carry source reference IDs; each source record must include its URL, rights statement, usage-rights classification, attribution where required, and verification date.
- No copyrighted textbook questions, passages, dialogues, scripts, illustrations, characters, or page layouts may be copied into AAS2. Sudan Ministry/NCCER textbooks are reference-only unless written permission is obtained.

### Remaining integration gates

- Two source-backed schema-v3 pilot packs are loaded through the activity-navigation flow. A versioned pack catalog/manifest, expanded curriculum coverage, and richer skill-level progress visualization remain open.
- App chrome and parent-facing labels follow Android device language through `values` and `values-ar`; child learning content is always selected in English and is never switched by device locale. Arabic child-activity support remains deferred.
- Fifty-two distinct offline prompt illustrations are now bundled: one per English activity across both learners. Eleven original option illustrations remain available as supplementary answer visuals. On-device visual/accessibility QA is still open.
- Mastery bands are deterministic provisional rules, not educationally calibrated thresholds.
- The source/license register and per-pack source references are in place. The 32 Grade 2 science questions are original AAS2 wording based on topic-level curriculum mapping; the Macmillan source remains `REFERENCE_ONLY`, and no protected textbook text or artwork is bundled. Remaining book units and other subjects still require authoring.
- Room foreign-key and DAO isolation instrumentation tests remain in source but are intentionally not executed during the no-Test-APK phase.

## Provisional technical identifiers

The initial Android namespace and application ID are `com.asimalimikial.aas2`. Treat this as provisional until the product identity and release/application-ID decision is finalized. No APK or release should be produced from this development phase.


## Curriculum priority update — 18 November 2026

- Macmillan Science Level 2 is a **core AAS2 subject** in Aboudy's Grade 2 pathway, by explicit product decision. It is not an optional enrichment module.
- The public Sudan Learning Passport Grade 2 listing is not used to determine the internal priority of AAS2 subjects; it only limits claims about the national catalogue.
- The official Macmillan Science Level 2 Scope and Sequence has been reviewed and maps 18 topics into five AAS2 units plus a cross-cutting scientific-enquiry strand.
- See [Macmillan Science Level 2 Curriculum Map v0.1](macmillan-science-level2-curriculum-map-v0.1.md).
- See [Macmillan-first Curriculum Crosswalk v0.1](curriculum-crosswalk-v0.1.md) for both learner paths and all selected subjects.
- See [Skill Registry v0.1](skill-registry-v0.1.md) for stable skills, prerequisites, misconceptions, assessment evidence, and mastery policy.
- See [Content Model Specification v0.1](content-model-spec-v0.1.md) for the proposed bilingual, typed activity and provenance contract.
- See [Schema v3 to v4 Migration & Test Plan v0.1](schema-v3-to-v4-migration-test-plan-v0.1.md) for backward-compatibility gates and regression coverage.
- See [Science Grade 2 Unit 1 QA Review](units/science-g2-unit-01-qa-review-v0.1.md) for the revised 32-item candidate set and assessment audit.
- See [Science Grade 2 Unit 2 — Animals and Survival](units/science-g2-unit-02-animals-survival-v0.1.md) for five lessons and 30 original draft items.

## Product planning and curriculum closure

- [Unified feature inventory and Feature Evaluation / Feature-to-Module / Feature-to-Skill matrices](docs/feature-matrix-v0.2.md)
- [Curriculum evidence closure register and outstanding book/local-curriculum gaps](docs/curriculum-evidence-closure-v0.1.md)
- [Vertical Skill Map from KG2 to Grade 2](docs/vertical-skill-map-v0.1.md)

These are evidence-graded planning documents. They do not authorize Android runtime, schema, Room, APK, release, or main-branch changes.
- [Textbook covers, ISBNs, official samples and lawful digital access register](docs/textbook-covers-and-digital-access-register-v0.1.md)
- [Textbook digital-access investigation v0.2 — BlinkLearning eBooks, licence requirements and legitimate purchase routes](docs/textbook-digital-access-investigation-v0.2.md)

- [MVP Proposal v0.2 — Draft for Product Review](docs/mvp-proposal-v0.2-draft.md)


## Implementation slice 01 — 23 November 2026

- The explicit user instruction to begin building was applied to the confirmed core boundaries; it does not automatically approve every optional feature or unresolved v4/Room decision.
- Implemented English-only question-first learning sessions, learner-specific subject grouping, dynamic home subject availability, per-activity practice markers, and retryable non-punitive feedback.
- Added a read-only Parent Dashboard with per-skill evidence and an Android device-credential gate. It requires a secure device lock and closes when the app leaves the foreground.
- See [Implementation Slice 01](docs/implementation-slice-01-v0.1.md) for scope, acceptance coverage, test evidence, and remaining work.
- See [Foundation Pilot Content QA v0.2](docs/units/foundation-pilot-content-qa-v0.2.md) for the expanded 8-item KG2 / 12-item Grade 2 English pilot review.
- See [Original Offline Media Asset Register v0.1](docs/media-asset-register-v0.1.md) for the six KG2 PNGs, rights notes, integrity checks, and remaining media QA.
- The current CI workflow compiles Kotlin and runs JVM tests, then checks Room schema v1/v2. It intentionally does not assemble or upload an APK and does not run emulator/device instrumentation.


### Latest source validation — 23 November 2026

- [GitHub Actions #179](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/45190733050) passed on commit `fa0afce974a823f17269d87fb184bf68536e872f`.
- The run passed JVM unit tests and checked-in Room schema v1/v2 verification. It did not build an APK or run Android instrumentation.
- The five Grade 2 habitat illustrations are included in the same passing source-validation run.
- The Parent Settings screen and parent-only language override are included in the passing source-validation run.


## Curriculum authoring and offline media update — 23 November 2026

- Grade 2 Science Unit 1 is now integrated as a schema-v4 book → unit → lesson hierarchy: four lessons covering plants as living things, plant parts/foods, useful plants, and germination/growth.
- The unit contains 32 original English single-choice activities (8 per lesson), with three unique options, one correct key, option-level rationales, misconception codes, difficulty, and assessment phases. Answer-key distribution is balanced: A=10, B=10, C=12. The set has no duplicate prompts or Arabic child text.
- The Grade 2 pack now contains 174 English activities; KG2 contains 108. Arabic legacy records remain stored but are excluded from the learner runtime.
- The legacy Visual Learning Engine v1.6.0 was extracted into `tools/visuals/` for build-time use only. GitHub Actions generated 282 distinct PNG prompt illustrations (SHA-256 uniqueness verified) and a manifest. The JavaScript engine itself is not packaged in the Android app.
- The Activity Renderer now displays local prompt illustrations and provides local audio buttons for question prompts, options, and post-answer feedback. One per-activity audio controller cancels previous playback before starting another clip.
- Post-answer English feedback was normalized to short encouragement only; it no longer repeats hints or explanations after submission.
- The English-only Supertonic 3 / sherpa-onnx generation workflow is configured to generate pre-recorded assets at speed 0.70 in GitHub Actions. The audio generation run is still in progress/pending; no generated audio count or runtime success is claimed until the manifest and files are verified.
- No APK, release, artifact publication, main merge, signing, manifest permission, or Room schema change was performed.


## Offline media expansion — 23 November 2026

- The extracted Scholars Visual Learning Engine v1.6.0 now generates one unique local PNG illustration for every English activity in the current packs: 108 KG2 and 174 Grade 2 activities (282 total). The generated manifest records per-activity paths and SHA-256 hashes; all 282 hashes are unique.
- The English offline speech pipeline is configured to use Supertonic 3 through sherpa-onnx 2.0.1 in GitHub Actions only. English speech generation is pinned to speed 0.70; OGG clips, SHA-256 metadata, and per-question prompt/option/feedback paths are generated into app assets. Neither the model weights nor the TTS runtime is packaged in Android.
- The speech-generation workflow is still running for the current 282-activity content revision. Do not treat the audio library as complete until the manifest and all referenced OGG files are committed and the asset-integrity test passes.
- The renderer now uses a single offline audio controller per activity, prevents clips from overlapping, and keeps the stop control available while a clip is preparing.
- Android device playback, visual scale/accessibility review, and full curriculum expansion remain open. No APK or release is claimed by this source-only work.


### Original question-bank migration

- The legacy bank contains 816 records: 616 legacy curriculum/game records and 200 explicitly tagged `new-authored` records.
- The 200 original-authored records have been migrated into the new packs: 100 for Ali KG2 and 100 for Aboudy Grade 2, with eight new unit-level skills added to each pathway. Exact duplicate prompt checks against the current 52-item pilot returned zero matches; every imported answer was verified against its options.
- Aboudy's original Science Unit 2 adds 30 further reviewed candidate activities. The current packs therefore contain 282 English activities (108 KG2 + 174 Grade 2), plus 10 retained Arabic historical records that are not shown in child sessions.
- The remaining 616 legacy records are not bulk-copied into the new runtime. Their item-level source, rights, activity-type, and curriculum mappings must be reviewed before migration; protected textbook expression is not redistributed.
- The 200 migrated records remain marked `REFERENCE_ONLY` pending the project-owner's item-level redistribution review. Do not treat this as public-release rights clearance.
