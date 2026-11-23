# AAS2 Implementation Slice 01 — Learner Experience and Parent Progress

**Date:** 23 November 2026  
**Repository:** `asimalimikial-rgb/Ali-and-Aboudy-Scholars-2`  
**Branch:** `foundation/slice-0-android`  
**Scope:** AAS2-NEW only.

## Authorization and boundary

The user explicitly instructed: “ابني التطبيق الآن” (“Build the app now”). Implementation began within the confirmed product boundaries and the existing Android foundation. This is authorization to start building the core product, not blanket approval of every proposed feature or unresolved architecture decision in MVP Proposal v0.2.

## Implemented

### 1. English-only learning selection

- The learning runtime selects only activities whose learning language is `en`.
- Device locale continues to localize app chrome and parent-facing labels; it cannot silently switch a child activity into Arabic.
- The existing schema-v3 JSON files remain unchanged. Their historical Arabic activity variants are not exposed through the current learning flow.
- If a pack has no English activity, the runtime shows an empty state rather than falling back to Arabic.

### 2. Question-first learning session

- Starting a learning session opens the first unpractised English activity immediately.
- When every activity has been attempted, the session starts again from the first activity for review.
- The learner can return to the activity list, see practice markers, and navigate by age-appropriate learning area.
- KG2 domains are grouped as English & Literacy, Mathematics, and Discovery. “Science and Discovery” is not presented as a separate core Science syllabus for KG2.
- Grade 2 domains remain English, Mathematics, and Science.

### 3. Home learning-area availability

- The learner home loads the selected learner's local pack and derives the available learning areas and activity counts from that pack.
- Static placeholder areas marked “In preparation” were removed from the live home display.
- The existing learner-scoped practice summary remains local to the selected learner.

### 4. Retry and feedback

- Incorrect responses show neutral, non-punitive feedback and a visible “Try again” action.
- Retry clears the in-memory evaluation and answer selection so the learner can respond again.
- Every submission is stored as its own attempt. No universal two-attempt limit was introduced.
- A JVM test verifies that an incorrect response followed by a correct retry creates two distinct attempt records.

### 5. Parent Dashboard and access gate

- A read-only Parent Dashboard displays overall attempts/accuracy and per-skill attempts/correctness/accuracy for the selected learner.
- It does not display uncalibrated mastery labels or compare the two children.
- Access requires Android's system device credential (PIN, password, or pattern). The app does not collect or store that credential.
- If the device has no secure screen lock, the dashboard remains closed and the app explains that a lock must be configured in Android settings. Child learning remains available.
- The dashboard closes automatically when the app leaves the foreground.
- No external navigation or network permission was added.

### 6. Optional offline answer illustrations

- The Compose answer renderer can load optional raster files from validated app asset paths.
- Decoding runs on the IO dispatcher and downsamples large images before display.
- Missing, corrupt, or unsupported images are omitted safely; the option text remains available.
- At the initial slice baseline, the v0.2.0 pilot packs did not reference image assets; the follow-up below adds the first six KG2 illustrations.

### 7. Expanded original English pilot content

- Both content packs are now at content version 0.2.0 while retaining schema v3.
- Ali KG2 has 8 original English activities across 4 skills; Aboudy Grade 2 has 12 original English activities across 6 skills.
- Legacy Arabic activity records remain in the files but are not surfaced by the child runtime.
- The new content QA report records the corrected KG2 counting stimulus, English-only initial-sounds outcome, and Grade 2 centimetre-comparison item: [Foundation Pilot Content QA v0.2](units/foundation-pilot-content-qa-v0.2.md).

## Acceptance coverage in this slice

| Requirement | Implementation evidence | Validation |
|---|---|---|
| Separate learner paths | Existing path-to-pack mapping retained; screens receive selected learner ID | Existing learner isolation JVM tests |
| Learning language independent of device locale | English-only selection policy | JVM tests: English selection and no Arabic fallback |
| Question-first start | First unpractised activity selected on session entry | JVM tests for next activity selection |
| Age-appropriate subject grouping | Domain-to-subject mapping per learner pathway | JVM tests for KG2 and Grade 2 grouping |
| Retry records distinct attempts | Activity session reset + submission service writes each attempt | JVM test for incorrect then correct retry |
| Parent progress privacy | System device credential gate; read-only local data | Source compile/CI; real-device gate UX still pending |
| Parent dashboard closure | Lifecycle observer closes dashboard on ON_STOP | Source compile/CI; real-device lifecycle test still pending |
| Room backward compatibility | No Room entity/schema changes in this slice | CI checked schema v1 and v2 exports |

## CI evidence

- **Run #131** — passed on `4420fbfcc0a20d86a91df77d9b01072026e95814`: Kotlin compilation, JVM unit tests, and Room schema v1/v2 verification.
- **Run #136** — passed on `7452b75297cefa1a4dd0884341675f39d6c1cb57`: includes retry persistence test, Kotlin compilation, JVM unit tests, and Room schema verification.
- **Run #142** — passed on `ac2cc6ce92617852f7f272a507cea609286e51b0`: includes Parent Dashboard, Android device credential gate, lifecycle close, Arabic labels, Kotlin compilation, JVM unit tests, and Room schema verification.
- **Run #147** — passed on `201fdbe02465a160dc7ea16c00c7f7e8aef9c487`: includes the modern Android Activity Result API for the credential gate, Kotlin compilation, JVM unit tests, and Room schema verification.
- **Run #167** — passed on `6acf5e89b48b2032929114fa3a71f5df6f15bfad`: includes content-pack v0.2.0 counts and item-alignment assertions, offline image-renderer compilation, Kotlin compilation, JVM unit tests, and Room schema v1/v2 verification. No Kotlin compiler warnings were reported in this run.
- **Run #169** — passed on `1633ed6f8f738b271d17861c5a6b83e6bfb76fa4`: final implementation/feature-matrix documentation revision; Kotlin compilation, JVM unit tests, and Room schema v1/v2 verification passed again. No Kotlin compiler warnings were reported. The workflow emitted a separate GitHub Actions Node 20 deprecation warning.
- **Run #174** — passed on `125baa77d580648d8decf8fe81c86b3e940bcfc7`: includes the six KG2 PNG assets, content references, and the JVM asset-integrity test; Kotlin compilation, JVM unit tests, and Room schema v1/v2 verification passed. The test validates paths, file presence, minimum size and PNG signature; visual rendering on a device remains unverified.
- CI does not build a distributable APK and does not run emulator or physical-device instrumentation.

## Preserved boundaries

- No edits to the old/legacy repository.
- No merge to `main`.
- Content packs were updated to version 0.2.0 with original English activities; schema v3 remains unchanged. No Room/Schema v4 migration was introduced.
- No schema v4, Room migration, Manifest permission, or Gradle dependency change.
- No APK, Release, or artifact upload.
- No copyrighted textbook pages, questions, images, or scans copied into the app.

## Remaining work

1. Verify the modern Activity Result API implementation with CI.
2. Perform emulator and real-device tests for parent credential prompts, lifecycle closure, back navigation, screen sizes, and offline behavior.
3. Eleven original option illustrations are now bundled (six KG2 and five Grade 2 habitat illustrations) and covered by a JVM asset-integrity test; runtime missing/corrupt-image fallback testing and device-scale visual QA remain open.
4. Parent Settings have now been implemented for parent-facing language (system / English / Arabic) and are gated behind the protected Parent Dashboard; verify language changes, back navigation and foreground exit on emulator/physical device.
5. Expand content only after independent subject/pedagogy/language review and source/edition evidence closure.
6. Keep numeric mastery thresholds provisional; do not present them as calibrated educational conclusions.

## Media asset follow-up — 23 November 2026

- Added six original 192×192 PNG illustrations under `app/src/main/assets/option-art/`: circle, square, triangle, sun, ball and cat.
- Linked the three shape illustrations to both KG2 shape activities, and sun/ball/cat to the KG2 initial `/s/` sound activity. Text labels remain visible and accessible; images are supplementary rather than the only answer cue.
- The illustrations are original project-created assets, not scans, crops, or derivatives of textbook artwork. The Grade 2 pack remains text-only at this stage.
- `BundledContentPackAssetsTest` now checks every referenced image path, file existence, minimum file size and PNG signature. This verifies packaged source assets, not visual rendering on a physical Android device.

## Current product limitation

This is a functional first implementation slice on top of the existing Android foundation, not a completed full curriculum or production release. The bundled content remains a small pilot: eight English activities for KG2 and twelve for Grade 2, with content version 0.2.0 and schema v3. The renderer supports optional local raster illustrations; KG2 now includes six original PNG assets for shape and initial-sound activities, while Grade 2 remains text-only and visual device QA is still pending. The seven textbook records remain reference-only; no full copyrighted book is bundled.

## Parent settings scope correction — 23 November 2026

- The saved language override applies only while the protected Parent Dashboard or Parent Settings screen is active. Learner-facing screens continue to follow the Android device locale; the learning activity content itself remains English-only.
- The settings screen has a separate foreground-exit callback: leaving the app from settings closes the entire parent area, while the Back action returns to the already-authorized dashboard.
- A JVM policy test covers the parent-only locale scope. Device-level visual verification of locale changes and background closure remains pending.

## Grade 2 habitat media follow-up — 23 November 2026

- Added five original raster illustrations for the Grade 2 habitat-choice activities: river, desert, oven, coral reef and deep ocean.
- Linked the options in `g2-habitat-01-en` and `g2-habitat-02-en`; the existing text labels remain visible and accessible.
- The bundled media set now contains eleven unique PNG files across Ali KG2 and Aboudy Grade 2. The JVM integrity test checks all eleven paths, file existence, minimum size and PNG signature.
- Other Grade 2 skills remain text-only pending content review. Phone-scale rendering and accessibility verification remain open.


## Parent Settings and Grade 2 media follow-up — 23 November 2026

### Parent Settings

- Added a protected Parent Settings screen reachable only from the Parent Dashboard.
- Persisted parent UI language choices (follow device, English, Arabic) through DataStore.
- Scoped locale override to the parent dashboard/settings surface only; learner-facing screens keep the device locale and child activity content remains English-only.
- Added lifecycle handling so leaving the app from settings closes the parent area; Back returns to the already-authorized dashboard.
- Added JVM policy tests for normalization, supported languages, locale direction and parent-only scope.

### Grade 2 illustrations

- Added five original offline PNGs for river, desert, oven, coral reef and deep ocean; linked them to the two Grade 2 habitat-choice activities.
- All eleven referenced illustrations across both packs are checked by the JVM asset-integrity test.
- PNG file structure, dimensions and chunk CRCs for the five new Grade 2 images were independently checked; each is 192 × 192.

### CI evidence

- **Run #179 passed** on `fa0afce974a823f17269d87fb184bf68536e872f`.
- Passed JVM unit tests and checked-in Room schema v1/v2 verification.
- The workflow is source-only: it does not assemble APKs or execute emulator instrumentation.

### Still open

1. Emulator and physical-device verification of Parent Gate, settings locale/RTL, foreground exit, back navigation and screen sizes.
2. Android instrumentation for Room foreign-key isolation and actual SQLite migration execution.
3. Airplane-mode learning session and process/device restart persistence test.
4. Runtime tests for missing/corrupt image fallback and visual/accessibility checks on target phone sizes.
5. Independent curriculum and pedagogy review before expanding the small 8+12 English activity pilot; Grade 2 mathematics and English remain narrow samples.
6. Production-ready versioned content catalog/manifest and full provenance validation across every item and asset.
7. Provisional mastery thresholds require calibration; no production claim of mastery is allowed.

No APK, release, artifact upload, main merge, Room schema change, Manifest change, Gradle dependency change or signing change was made.
