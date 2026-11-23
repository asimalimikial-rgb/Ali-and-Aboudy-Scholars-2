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
- The subsequent modernization to Android Activity Result API is a later code change and must be covered by the next CI run.
- CI does not build a distributable APK and does not run emulator or physical-device instrumentation.

## Preserved boundaries

- No edits to the old/legacy repository.
- No merge to `main`.
- No JSON pack rewrite or content expansion.
- No schema v4, Room migration, Manifest permission, or Gradle dependency change.
- No APK, Release, or artifact upload.
- No copyrighted textbook pages, questions, images, or scans copied into the app.

## Remaining work

1. Verify the modern Activity Result API implementation with CI.
2. Perform emulator and real-device tests for parent credential prompts, lifecycle closure, back navigation, screen sizes, and offline behavior.
3. Add safe option-image rendering and media integrity/fallback tests.
4. Add a proper parent settings surface only after its controls and security behavior are specified.
5. Expand content only after independent subject/pedagogy/language review and source/edition evidence closure.
6. Keep numeric mastery thresholds provisional; do not present them as calibrated educational conclusions.

## Current product limitation

This is a functional first implementation slice on top of the existing Android foundation, not a completed full curriculum or production release. The bundled content remains a small pilot: four English activities for KG2 and six for Grade 2. The seven textbook records remain reference-only; no full copyrighted book is bundled.
