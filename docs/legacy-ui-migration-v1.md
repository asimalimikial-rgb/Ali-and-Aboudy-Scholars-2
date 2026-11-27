# Legacy-inspired learning experience migration

## Reference source
- Repository: https://github.com/asimalimikial-rgb/Ali-andd-aboudy-scholars
- Reference branch: `backup/main-before-rebuild-v2-2026-11-08`
- Reference file: `index.html`
- The legacy repository and its branches are read-only references for this task. No legacy files, assets, child profile photos, question-bank data, or audio files are copied into AAS2.

## AAS2 implementation target
- Repository: `asimalimikial-rgb/Ali-and-Aboudy-Scholars-2`
- Working branch: `feature/legacy-inspired-learning-world-ux`
- Base: `foundation/slice-0-android`

## UX patterns being carried forward
- Separate, recognizable learner journeys for Ali (KG2) and Aboudy (Grade 2).
- A visual learning-world entry point, clear progress signals, large touch targets, and low-friction navigation.
- Question-first learning with prompt audio automatically played when a new activity opens.
- Feedback audio automatically played after evaluation when the content pack supplies a correct/incorrect feedback clip.
- A calm parent control area for progress review and preferences.
- Strong visual hierarchy, rounded cards, meaningful color coding, and responsive vertical scrolling.

## Product-preservation rules
- Keep the current AAS2 content packs, question bank, books, units, illustrations, and bundled audio as the source of truth.
- Do not replace current content with legacy content.
- Do not copy legacy profile photos or other private family images.
- Preserve Room database schema, attempt history, learner separation, content validation, offline-only Android audio playback, manifest, Gradle configuration, package ID, permissions, and signing configuration.
- No changes to `main`, no merge, and no release until regression checks and user review are complete.

## Files changed in this UI slice
- `MainActivity.kt`: redesigned learner selection and learner home.
- `LearningExperienceScreen.kt`: visual learning-world header and more expressive domain/book/unit/lesson/activity cards.
- `ActivityRenderer.kt`: question prompt audio and supplied feedback audio now play automatically at the relevant point in the activity lifecycle.
- `ParentDashboardScreen.kt`: calmer parent progress-console presentation.
- `ParentSettingsScreen.kt`: clearer parent control-centre presentation.

## Verification still required
- Android CI compile and unit tests.
- Android instrumentation tests and visual review on emulator/device.
- Verify prompt/feedback clips start once per relevant event, stop on navigation/retry, and never overlap.
- Verify the question remains fully usable when any optional image/audio asset is missing.
- Verify Ali and Aboudy retain isolated progress and the existing book/content mappings.
