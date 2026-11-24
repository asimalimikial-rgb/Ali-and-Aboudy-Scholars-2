# Legacy question-bank renderer and interaction audit v0.1

**Updated:** 24 November 2026  
**Source snapshot:** `Ali-andd-aboudy-scholars` commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**New app branch:** `foundation/slice-0-android`  
**Disposition:** Audit findings only. No legacy records are production-approved or runtime-integrated.

## 1. Source audio integrity across all 616 legacy records

- 616/616 records contain audio references.
- 6,810 audio references point to 2,152 unique OGG files.
- All 2,152 unique references resolve to manifest entries and actual `assets/speech/*.ogg` paths in the source snapshot.
- All referenced file byte sizes match the source speech manifest.
- Each of the 616 records has manifest text coverage for its prompt, options and Arabic helper.
- The source manifest contains 3,020 entries with 3,020 unique SHA-256 values.

This verifies manifest/reference/tree consistency, not acoustic quality, pronunciation accuracy, intelligibility, or physical-device playback. The legacy audio has not been migrated into the new app. Arabic helper audio must not be exposed in the current child runtime.

## 2. Specialized option-bearing interactions — 28

| Type | Count | Required interaction semantics |
|---|---:|---|
| phonics | 1 | Phoneme-level listening/blending; do not reduce to plain text choice |
| fill_gap | 3 | Inline blank completion with word/sentence context |
| number_line | 3 | Number-line position or forward/backward movement |
| comprehension | 10 | Passage/story plus a separate question and options |
| word_problem | 9 | Preserve the supplied model (ten-frame, addition model, array, bar model, number-line jump, ruler comparison) |
| place_value | 1 | Hundreds/tens/ones representation |
| graph | 1 | Render and read the supplied bar-chart data |

All 28 items have distinct options and exactly one keyed answer. Every referenced audio ID resolves; the manifest covers prompt, options and helper text. This is a structural/media-metadata pass only.

### Specific data-quality flags

- `legacy-ali-0245`: top-level prompt “What does the flower need?” differs from embedded comprehension question “What helped the seed grow?” The story is about a seed. Align the displayed question with the story before migration.
- `legacy-aboudy-0004`: top-level prompt is the generic “Read the story and answer”; the actual question “What color was the robot?” is stored separately. The comprehension renderer must display the story and the explicit question field, not the generic title alone.
- `legacy-aboudy-0288`: Arabic explanation refers to two rows of three dots, unrelated to the English shells subtraction problem. The current child-runtime contract defers Arabic helper/explanation exposure; do not surface this field.

## 3. Custom no-option interactions — 120

| Type | Count | Required renderer / behavior |
|---|---:|---|
| count | 22 | Tap/count objects and record the selected count |
| spelling | 11 | Letter tiles or letter ordering to build a target word |
| tracing | 4 | Stroke/trace interaction; distinguish letter/number tracing from free drawing |
| true_false | 60 | Explicit binary response, not generic multiple choice |
| build_sentence | 13 | Reorder word tiles into a sentence or sequence |
| match_pairs | 6 | Connect two sets of items; preserve the pair list |
| compare | 2 | Compare visible groups/lengths and choose the correct side |
| clock | 1 | Interactive clock face with hour/minute target |
| money | 1 | Coin-selection interaction with localized currency |

### Custom payload notes

- Four `match_pairs` records use top-level `answer: true` as a completion flag while the actual correct mapping is in `legacyData.pairs`. The new parser must read the pair payload, not interpret the boolean as the answer key.
- The single money activity `legacy-aboudy-0009` asks for “15 cents”. This is not localized to the Sudanese learner context; review the currency and replace with a suitable local-currency task or a non-currency coin-combination task before migration.
- Two tracing records are open drawing tasks (`legacy-ali-0299` and `legacy-aboudy-0313`). Their batch-01 patch supplies open-response rubric proposals; they must not be forced into a fixed answer.
- The other tracing records target a letter or number and need stroke/trace behavior rather than a drawing rubric.

## 4. Runtime architecture implications

The current AAS2 Android renderer supports single-choice only. The 28 specialized and 120 custom legacy items are not compatible with that renderer merely because some have options or an answer field.

Before integration, implement and test typed renderers for phonics/sound blending; inline gap completion; number line; comprehension passage and question; word-problem visual models; place value; graph reading; tap counting; spelling/letter construction; tracing and open drawing with rubric capture; true/false; sentence ordering; matching pairs; comparison; analog clock; and coin selection.

A parser must preserve each payload type and validate required fields. Missing optional audio/art must never hide the question or crash the session. Arabic helper fields remain excluded from child activities unless a separate future decision changes the content contract.

## 5. Review boundary

- The 28 specialized and 120 custom records remain in the 616-item review queue with all seven review gates unchecked.
- The first patch batch is staging-only and pending technical QA.
- This audit does not approve source text, difficulty, curriculum mapping, media pronunciation, or activity renderer.
- Do not convert specialized or custom items to generic SINGLE_CHOICE to increase counts.
