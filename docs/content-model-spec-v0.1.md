# AAS2 Content Model Specification v0.1

**Status:** Architecture specification draft; no Android schema or runtime code changed by this document.  
**Baseline:** Existing Kotlin content model and two bundled JSON packs at schema v3.  
**Product invariants:** One shared learning engine; isolated KG2 and Grade 2 learner paths; offline-first; Macmillan-first curriculum mapping; original AAS2-authored learning content; English remains the learning language for English/Science activities. No Arabic or explanatory paragraph is added inside child activities by default, under the confirmed Notion product decision.

## 1. Design decisions

1. **Separate learning language from interface/helper language.** The language of the learning objective and prompt is not selected from the device locale. A child using an Arabic device must not silently receive a fully translated Arabic science/English activity.
2. **Arabic helper is deferred, not an MVP feature.** The confirmed product decision is no Arabic language or explanation inside child activities by default. The logical model may reserve an extension point for a future, evidence-backed exception, but no helper object is populated, exposed in settings, or rendered unless a new specific need is documented and approved.
3. **Preserve v3 reading compatibility.** Existing v3 packs remain readable without rewriting their files. A v4-capable app reads v3 through an explicit legacy adapter; a v3 app must never be given a v4-only pack.
4. **Content is typed, not renderer-inferred.** Each activity declares its type and typed payload. Renderer support is an allow-list, not a best-effort guess.
5. **Provenance is first-class.** Skills, activities and media refer to stable source IDs and content review states. A source's curriculum alignment does not grant reproduction rights.
6. **Deterministic assessment.** Objective activities have explicit answer keys/rules. No generative model decides whether a child's answer is correct.
7. **Media failure is non-fatal.** Missing/corrupt optional media falls back to accessible text/shape; it never hides the question or crashes the learning session.

## 2. v3 baseline (observed)

Current `ContentPack` contains `packId`, `version`, `schemaVersion`, `learnerPath`, `skills`, `activities`, and `sourceReferences`.

- `SkillDefinition`: `skillId`, `learnerPath`, `domain`, `observableOutcome`, `sourceReferenceIds`.
- `ActivityDefinition`: `activityId`, `skillId`, `activityType`, one `language`, `prompt`, text/image options, `correctOptionId`, generic correct/incorrect feedback, and difficulty.
- Validator accepts schema v3, learner paths KG2/GRADE_2, activity type SINGLE_CHOICE, languages en/ar, and a finite rights enum.
- Language selection currently chooses the activity variant matching device locale, falling back to English. This is the behaviour to replace for new v4 content.
- `AnswerOption.imageAssetPath` is validated but the current renderer does not render option images.
- Current renderer supports single-choice only and displays activity-level feedback, not per-option misconception feedback.

## 3. Proposed v4 logical model

The following is a logical contract, not copy-ready Kotlin code. Exact serialization names and Kotlin sealed-class structure are implementation decisions to be resolved in the engineering design review.

### 3.1 Content pack

| Field | Type / rule | Purpose |
|---|---|---|
| `packId` | stable string | Pack identity |
| `version` | semantic content version | Content revision independent of app version |
| `schemaVersion` | integer = 4 | Explicit contract version |
| `learnerPath` | KG2 or GRADE_2 | Prevent cross-path content leakage |
| `localeDefaults` | optional locale policy | Parent-facing interface defaults only |
| `skills` | unique skill records | Outcomes and progression |
| `activities` | unique typed activity records | Learning and assessment |
| `sourceReferences` | unique canonical source records | Curriculum/provenance/rights |
| `mediaManifest` | optional local asset records | Integrity, attribution and fallback |
| `packMetadata` | authoring/review metadata | Version, review date, approval state |

### 3.2 Skill record

Required:
- `skillId`, `learnerPath`, `domain`, `observableOutcome`, `sourceReferenceIds`.

Optional but strongly recommended:
- `prerequisiteSkillIds` (IDs in the same learner path).
- `difficultyBand` (ordered qualitative band; do not confuse with per-item difficulty).
- `misconceptionCodes` (stable code + short internal description).
- `masteryEvidencePolicyId` (policy reference; no uncalibrated numeric threshold embedded).
- `reviewPolicyId`.
- `curriculumAlignment` with source ID, topic/unit, evidence status, and confidence.
- `status`: DRAFT / REVIEWED / APPROVED / RETIRED.

A skill must not refer to another learner path. Cyclic prerequisites are invalid. Retired IDs remain resolvable for historical attempts.

### 3.3 Activity record — shared envelope

Required:
- `activityId`, `skillId`, `activityType`, `learningLanguage`, `prompt`, `difficulty`, `assessmentPhase`, `reviewStatus`.
- `payload`: exactly one payload matching the declared `activityType`.
- `feedback`: structured feedback policy.
- `sourceReferenceIds`: one or more supporting references for curriculum alignment.
- `mediaIds`: zero or more IDs in the manifest.

Optional:
- No helper field is populated in the MVP content contract. A future approved exception may add a separate support object without changing `learningLanguage`.
- `accessibility`: concise alt text, spoken text equivalent, reduced-motion alternative.
- `tags`: diagnostic, practice, transfer, review, local-context etc.

### 3.4 Language model

- `learningLanguage` is an ISO language code such as `en`; it determines prompt, choices, and answer content.
- No Arabic helper, translation, or pre-question explanation is present in the current MVP. A future exception requires a specific observed learning need, a narrowly scoped proposal, and explicit approval. It must not be enabled merely because the device locale is Arabic.
- `uiLocale` is selected by the app/parent settings and controls navigation, parent dashboard and system messages; it must not rewrite learning content.
- No duplicate activity is required merely to provide a helper in another locale.
- The activity must be fully solvable in its learning language without translation or explanation.
- If a future approved support aid is introduced, its exposure must be recorded and its effect on independent mastery evidence defined before implementation.
- Any future support aid for phonics, spelling, reading or listening must not disclose the target phoneme, word form or correct response.

### 3.5 Activity types and typed payloads

| Type ID | Payload essentials | Primary use | Implementation status |
|---|---|---|---|
| `SINGLE_CHOICE` | options, exactly one correct option ID | Existing objective selection | Existing v3 renderer |
| `MULTI_SELECT` | options, correct option ID set, selection constraints | Categorisation / multiple evidence | Proposed v4 |
| `MATCH_PAIRS` | left/right item IDs and exact pair map | Vocabulary, classification | Proposed v4 |
| `ORDER_SEQUENCE` | item IDs and canonical order | Life cycles, story/time sequences | Proposed v4 |
| `SORT_CATEGORIES` | categories and item-to-category map | Science and numeracy classification | Proposed v4 |
| `NUMERIC_ENTRY` | accepted numeric value/range, unit and tolerance policy | Arithmetic/measurement | Proposed v4 |
| `SHORT_TEXT` | deterministic accepted forms or teacher review; no opaque AI marking | Spelling/writing | Proposed v4 |
| `AUDIO_IDENTIFY` | local audio ID, response options, transcript policy | Listening/sound | Proposed v4 |
| `TRACE_OR_DRAW` | local prompt/guide and evidence capture policy | Early writing/creativity | Proposed v4; requires privacy review |
| `PRACTICAL_OBSERVATION` | steps, safety rules, structured observation record | Science enquiry | Proposed v4; parent/adult-supervised |
| `INTERACTIVE_MODEL` | deterministic model config, bounded interaction | Number line/ten-frame/arrays | Proposed v4 |

Do not implement every type in one release. MVP type allow-list must be selected from the reviewed feature matrix and renderer capacity. A v4 pack containing an unsupported type must fail validation clearly or be filtered only under an explicit, tested capability policy; it must never silently render as the wrong interaction.

### 3.6 Options and per-option diagnostics

An option may contain:
- `optionId` (unique within activity).
- `text` and/or `mediaId`.
- `isCorrect` only if the chosen serialization makes exactly-one validation reliable; alternatively keep answer key in a separate answer-rule object.
- `misconceptionCode` for a known incorrect choice.
- `feedback` short, non-shaming explanation.
- `accessibilityLabel`.

Do not expose correctness or misconception metadata to the child UI before submission. Avoid storing duplicated truth fields that can disagree; choose one canonical answer key.

### 3.7 Feedback contract

Feedback has:
- `correct`: concise explanation of why the answer is correct.
- `incorrectDefault`: scaffold that points to the concept, not the answer.
- `byOption`: optional option-specific feedback keyed by option ID or misconception code.
- `hintSequence`: optional progressive hints; each has a level and must not reveal the answer.
- `tone`: neutral/supportive, no punitive language.

The evaluator returns a structured result: correctness, selected response, feedback key, misconception code (if any), and whether the attempt was independent or supported. The UI renders the returned feedback; it does not recalculate correctness.

### 3.8 Assessment phase and evidence

Allowed phases:
- DIAGNOSTIC
- GUIDED
- INDEPENDENT
- TRANSFER
- DELAYED_REVIEW

Attempt evidence should eventually record:
- learner ID/path, skill ID, activity ID + content version;
- response and correctness;
- attempt timestamp;
- helper/hint exposure;
- assessment phase;
- feedback key shown;
- evidence modality;
- completion state.

Mastery remains a separate derived record, not a property set by one activity. A single correct response cannot mark a skill secure. Supported attempts are retained but do not count as unaided evidence.

### 3.9 Media manifest

Each media item includes:
- `mediaId`, relative `assetPath`, `mediaType`, `sha256`, `byteLength`, `altText`, `sourceReferenceId`, `usageRights`, `attribution` where required, `fallbackText`, and `status`.
- Asset paths must be relative, remain inside the pack asset root, and reject traversal, absolute paths, remote URLs and unknown extensions.
- Every asset is individually rights-screened. A public-domain lesson PDF does not grant rights to a linked photo.
- Missing optional media must use fallback content and be recorded in QA; required media missing from a required interaction blocks that activity at pack review time.

## 4. Content review lifecycle

`DRAFT → SCIENCE/DOMAIN_REVIEW → PEDAGOGY_REVIEW → LANGUAGE_REVIEW → RIGHTS_AND_MEDIA_REVIEW → TECHNICAL_VALIDATION → APPROVED`.

A reviewer may return an item to an earlier state with a reason. Only APPROVED content may enter production packs. The content pack itself records the reviewer state and content version. Reviewers must not approve their own generated content without a second independent pass for science/math correctness.

## 5. Backward compatibility contract

- v3 files remain immutable fixtures until migration is separately approved.
- v4 app reader supports v3 via a dedicated adapter mapping: v3 `language` → `learningLanguage`; v3 options → SINGLE_CHOICE payload; generic feedback → v4 feedback.correct / incorrectDefault; no helper object is inferred.
- v3 source references map to v4 provenance records without changing IDs.
- Existing activity IDs, skill IDs, learner paths and answer keys are preserved.
- No v3 content is rewritten in place. A new content pack version is created only after all validation and regression checks pass.
- A legacy app must reject v4 packs with a clear unsupported schema message; do not attempt partial parsing.
- Database migrations for learner attempts are separate from content-pack migration. Existing attempts retain original content version and answer/feedback snapshot.

## 6. Non-functional requirements

- Offline operation: all required prompt, answer, helper and media assets are bundled or explicitly cached before the session.
- Performance: parse/validate packs off the main UI thread; do not decode full-resolution media during composition.
- Accessibility: scalable text, screen-reader labels, adequate touch targets, reduced motion, audio alternatives; RTL applies to UI/localized parent surfaces only if configured, not by translating child learning activities.
- Privacy: no child response/audio/image upload by default; any future sync or recording requires a separate consent and data-retention design.
- Determinism: answer evaluation must be reproducible from pack version and response.
- Localization: parent-facing text can be localized independently of child learning language.
- Observability: internal QA logs must not contain child names or free-text sensitive content.

## 7. Decisions intentionally not made in v0.1

- Final Kotlin sealed classes / JSON discriminator names.
- Whether v4 is introduced with one new pack or staged dual-reader release.
- Which activity types are in MVP beyond SINGLE_CHOICE.
- Whether a future Arabic helper is justified; current product rule is no Arabic or explanation inside child activities unless a new need is evidenced and approved.
- Numeric mastery thresholds and exact spaced-review intervals.
- Any cloud account, analytics, speech recognition, generative marking or data synchronization.
- Exact image/audio compression budgets.

## 8. Acceptance criteria for this specification

- Learning language, UI locale and optional helper are separate concepts.
- v3 can be read without changing existing pack files.
- Each activity type has a typed payload and explicit renderer capability.
- Source/rights metadata is traceable from skill/activity to canonical source IDs.
- Feedback and attempt evidence can distinguish independent from supported performance.
- Unsupported activities and missing media fail safely.
- No new child data leaves the device by default.
- The migration/test plan at `schema-v3-to-v4-migration-test-plan-v0.1.md` covers every compatibility promise above.

## 9. Source of truth

- Existing v3 Kotlin: `ContentPackModels.kt`, `ContentPackValidator.kt`, `ContentPackSelectionPolicy.kt`, `ActivityRenderer.kt`.
- Current bundled packs: `aas2-kg2-foundation.json`, `aas2-grade2-foundation.json`.
- Curriculum and rights: `docs/content-source-register.md`, `docs/curriculum-crosswalk-v0.1.md`, `docs/skill-registry-v0.1.md`.
- This specification does not authorize code changes by itself. Implementation follows the project approval gate and a separate reviewed change set.
