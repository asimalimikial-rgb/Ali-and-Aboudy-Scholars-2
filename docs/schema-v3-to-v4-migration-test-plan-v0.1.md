# AAS2 Schema v3 → v4 Migration and Verification Plan v0.1

**Status:** Proposed migration plan; no production code, JSON pack, Room schema, or Android configuration changed.  
**Baseline commit reviewed:** `ec79ec8154a6f3e5260288f7b53f670fb21927ff`  
**Target:** Implement the approved Content Model Specification without breaking existing KG2/Grade 2 packs, historical learner progress, or offline startup.

## 1. Migration objective

Introduce a new content contract that separates:
- learning language from app/UI locale;
- any future approved support aid from the English learning prompt (not part of MVP);
- typed activity payloads from renderer implementation;
- content provenance and rights from the activity presentation;
- attempt evidence from mastery decisions.

The migration must be reversible at the release/pack level, deterministic, offline-safe, and additive. It must not require a cloud migration or change Android permissions.

## 2. Observed v3 baseline

### Current models

- `ContentPack`: packId, version, schemaVersion, learnerPath, skills, activities, sourceReferences.
- `SkillDefinition`: skillId, learnerPath, domain, observableOutcome, sourceReferenceIds.
- `ActivityDefinition`: activityId, skillId, activityType, language, prompt, options, correctOptionId, feedbackCorrect, feedbackIncorrect, difficulty.
- `AnswerOption`: optionId, text, optional imageAssetPath.
- Source records already carry canonical sourceReferenceId, title, publisher, URL, rights/licence, attribution and verifiedAt.

### Current validator and renderer

- Validator hard-requires schemaVersion 3 and supports only KG2/GRADE_2, SINGLE_CHOICE, en/ar and known usage-right values.
- It checks source IDs, skill IDs, activity IDs, option IDs, answer key uniqueness, HTTPS provenance URLs, rights attribution, difficulty 1–5, and unsafe image paths.
- Renderer presents one single-choice card with text prompt and text options. It does not render option images or per-option feedback.
- Language selector chooses Arabic or English activity variants according to device language, then falls back to English.
- Existing tests include ContentPackValidatorTest, ContentPackSelectionPolicyTest, AssetContentPackRepositoryTest and BundledContentPackAssetsTest.
- Two bundled pack files are KG2 and Grade 2 foundation JSON. Both must remain valid fixtures throughout migration.

## 3. Release strategy

### Stage A — Freeze and characterize v3

1. Capture immutable copies/hashes of both v3 JSON packs and current Kotlin model/validator/selection/renderer files.
2. Run the existing JVM unit tests and Room schema checks on the baseline.
3. Record the exact activity/skill/source counts, all stable IDs, answer keys, and sourceReferenceIds.
4. Add regression fixtures that deserialize and validate the original v3 files.
5. Do not modify v3 fixture contents as part of v4 implementation.

**Exit gate:** baseline tests pass and a reproducible inventory exists.

### Stage B — Introduce a dual-version reader

1. Define a v4 envelope and typed activity representation in a separate model namespace/package where practical.
2. Add a version dispatcher:
   - schema 3 → LegacyV3Adapter → normalized domain activity;
   - schema 4 → V4 decoder → normalized domain activity;
   - any other version → explicit UnsupportedSchema result.
3. Keep the existing v3 deserializer and validation path intact.
4. Map v3 `language` to `learningLanguage`; do not infer an Arabic helper from an Arabic activity variant.
5. Map existing option image paths into media references only after path validation and manifest verification.
6. Preserve activity IDs, skill IDs, source IDs, learnerPath, answer keys and difficulty.
7. Keep historical attempts attached to the exact original activity/content version.

**Exit gate:** both existing packs load through the new repository without content mutation; unsupported schema fails safely.

### Stage C — Add v4 validation independently

Validate:
- schema version and required pack metadata;
- supported learner path;
- unique/nonblank skill, activity, source, media and option IDs;
- skill path isolation and valid prerequisite graph (no unknown IDs/cycles);
- every activity references an existing skill and source;
- declared activity type exactly matches its payload;
- only enabled renderer capabilities are accepted for the current app build;
- exactly one valid answer for SINGLE_CHOICE; set/ordering rules for later activity types;
- valid language codes and helper policy;
- helper cannot replace or mutate the learning prompt/answer;
- feedback keys reference valid option/misconception IDs;
- assessment phase is from the allow-list;
- media paths are relative, inside asset root, no traversal, no remote URL, expected extension and MIME match;
- SHA-256 and byte length match manifest;
- rights status and required attribution are complete;
- no duplicate content IDs across a pack where global uniqueness is required.

**Exit gate:** positive and negative validator fixtures cover every rule; errors are actionable and deterministic.

### Stage D — Renderer capability rollout

Do not add every interaction in one change. Recommended order after the feature matrix selects MVP scope:

1. SINGLE_CHOICE (existing) with learning-language policy and optional helper slot.
2. MATCH_PAIRS and ORDER_SEQUENCE after interaction accessibility review.
3. SORT_CATEGORIES.
4. NUMERIC_ENTRY.
5. AUDIO_IDENTIFY / PRACTICAL_OBSERVATION only after media, safety and lifecycle review.
6. SHORT_TEXT, TRACE_OR_DRAW and INTERACTIVE_MODEL only after deterministic scoring, privacy and device-performance review.

For each type:
- implement a dedicated renderer;
- implement a deterministic evaluator;
- add semantics/screen-reader labels and keyboard/switch-access alternatives where relevant;
- add UI tests for loading, selection, submit, feedback, disabled state and process recreation;
- ensure missing optional media falls back without crashing.

**Exit gate:** renderer and evaluator capability registry agree with validator allow-list. No unsupported type is silently rendered as another type.

### Stage E — Pack migration and content QA

1. Convert one small reviewed activity set into v4 as a pilot, not all content at once.
2. Preserve all v3 stable IDs and content version metadata.
3. Add learningLanguage and optional helper separately.
4. Run science/domain, pedagogy, language, rights/media and technical QA.
5. Compare v3 and v4 activity meaning, answer keys and learning objective equivalence.
6. Keep v3 packs available as rollback fixtures until the v4 pilot passes all gates.
7. Expand only after a full regression pass.

**Exit gate:** pilot pack validated, rendered, evaluated and resumed offline without data loss.

## 4. Learner-progress and database compatibility

- Do not change Room entities or migrations merely because content JSON changes.
- If attempt evidence needs new fields (helper exposure, assessment phase, feedback snapshot, content version), design a separate additive Room migration.
- Existing rows must receive safe defaults such as `legacy_unknown`, not fabricated evidence.
- Never retroactively label a historical Arabic activity as helper exposure; it was a separate language variant in v3. No helper is introduced in the MVP. A future approved support aid requires its own explicit evidence and tests.
- Keep learner-path separation at repository query, progress, recommendations and export boundaries.
- Test upgrade from an installed v3 database with attempts for both learners; test downgrade/rollback policy explicitly.
- Preserve attempt history and avoid recomputing past correctness using a changed answer key.

## 5. Required test matrix

| Test layer | Scenario | Expected result |
|---|---|---|
| Serialization | Deserialize original KG2 v3 pack | Pass unchanged |
| Serialization | Deserialize original Grade 2 v3 pack | Pass unchanged |
| Version dispatch | v3 input | Legacy adapter selected |
| Version dispatch | v4 input | V4 decoder selected |
| Version dispatch | Unknown schema | Explicit unsupported-version error; no crash |
| Identity | Duplicate/blank IDs | Validation failure |
| Path isolation | KG2 activity references Grade 2 skill | Validation failure |
| Source provenance | Unknown sourceReferenceId | Validation failure |
| Rights | Adaptation allowed without required attribution | Validation failure |
| Rights | REFERENCE_ONLY source used as inspiration with original AAS2 text | Allowed; no copied source expression |
| Language | Arabic device locale with English learning activity | English learning content remains selected |
| Helper | Helper disabled | No helper displayed; task remains solvable |
| Child-language policy | Device locale is Arabic | English learning content remains unchanged; no Arabic helper or pre-question explanation is shown in MVP |
| Helper evidence | Helper used | Attempt marked supported, not unaided |
| Answer key | Zero or multiple correct SINGLE_CHOICE keys | Validation failure |
| Feedback | Invalid option feedback key | Validation failure |
| Activity type | Declared type/payload mismatch | Validation failure |
| Capability | Pack requires renderer not supported by app | Safe unsupported capability result |
| Media | Missing optional image | Text/fallback renders |
| Media | Hash mismatch/corrupt required asset | Activity/pack blocked with actionable error; app remains usable |
| Media security | Absolute/traversal/remote asset path | Validation failure |
| Offline | Fresh install, airplane mode, bundled packs | Learning starts and completes |
| Recovery | Process killed during session | Learner resumes without duplicate/lost attempt |
| Progress | Existing v3 attempts after app upgrade | History preserved and IDs resolvable |
| Progress | Two learner profiles | No cross-profile leakage |
| Accessibility | Large font, TalkBack, RTL UI, touch targets | No clipping or inaccessible controls |
| Regression | Existing repository/selection/evaluator tests | Pass |
| Build | JVM tests + Room schema checks | Pass |
| Device | ARM32 target if supported by product decision | Install/start/session test on actual device |

## 6. Test data and assertions

Create fixtures:
- `legacy-kg2-v3.json` and `legacy-grade2-v3.json` as exact baseline copies;
- `valid-v4-single-choice-en.json`;
- `valid-v4-single-choice-en-with-ar-helper.json`;
- `invalid-v4-unknown-skill.json`;
- `invalid-v4-helper-replaces-learning-language.json`;
- `invalid-v4-unsafe-media-path.json`;
- `invalid-v4-multiple-correct-options.json`;
- `invalid-v4-unsupported-type.json`;
- `invalid-v4-bad-attribution.json`.

Assertions:
- stable IDs/counts/answer keys preserved;
- normalized v3 and equivalent v4 domain representations are equal for legacy-supported fields;
- no helper is synthesized from old language variants;
- app locale changes do not change the learning objective or answer key;
- evaluator output is deterministic;
- no network call is needed to load or solve a bundled activity.

## 7. Rollback and failure policy

- Ship v4 reading support before shipping v4-only content.
- Keep last-known-good v3 packs and reader path until v4 usage has passed the agreed pilot period.
- If v4 decoding or renderer fails, return to v3 pack selection without deleting attempts.
- Never downgrade the Room database destructively. Any rollback must preserve new nullable/additive fields.
- A pack with unsupported schema/type is rejected as a pack, not partially imported.
- Record failure details without child-identifying data.

## 8. CI and release gates

Required before implementation PR can be considered complete:
1. Existing tests pass on baseline and after change.
2. New serializer, adapter, validator and evaluator tests pass.
3. Room schema checks pass; no unexplained schema drift.
4. Bundled pack integrity checks pass.
5. Static analysis/lint passes.
6. No new Android permissions or network dependencies introduced by content migration.
7. APK/artifact generation remains disabled until separately authorized by the project release gate.
8. No merge to main or public release is implied by this plan.

## 9. Implementation dependencies and ownership

- Product/curriculum: approve activity types and content-language policy.
- Content architecture: finalize v4 logical schema and adapter mapping.
- Android domain layer: versioned parser, normalized domain model, validator.
- UI: renderer capability registry and per-type components.
- Assessment: deterministic evaluator and supported/independent evidence.
- Data: only additive Room migration if attempt evidence requires it.
- QA: fixtures, regression, offline, accessibility and device tests.
- Rights review: source IDs, per-asset provenance and attribution.

## 10. Current decision

**Proceed with specification and test design only.** The user authorized the five parallel planning/content workstreams, not an unreviewed Android code migration. Actual Kotlin/schema/Room edits remain gated by review of the specification and explicit implementation authorization. The existing v3 packs and app files remain untouched.
