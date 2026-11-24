# Legacy question-bank item review — batches 02–25 v0.1

**Updated:** 24 November 2026  
**Source snapshot:** `Ali-andd-aboudy-scholars` at `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Disposition:** Initial item-level review and staging patch only; not production approval.

## Scope of this pass

- Batches 02–25 were inspected from the 616-item review queue; Batch 01 retains its earlier structural/duplicate/drawing review record.
- For the inspected batches, option uniqueness, answer-key occurrence, referenced audio IDs, and prompt/option/helper manifest coverage were checked. A manual pedagogical scan identified the findings below.
- The full source audio inventory remains: 616/616 questions have audio references; 6,810 references; 2,152 unique OGG files; all file paths and byte sizes match the source manifest.
- All queue gates remain unchecked. This report does not mark any batch fully approved.

## Confirmed corrections staged

Twelve patch records are in `content/staging/legacy-question-bank-correction-patch-v0.1.json` (nine findings from batches 03–25 plus three Batch 01 prompt/phoneme corrections):

| Item | Finding | Proposed correction |
|---|---|---|
| `legacy-ali-0073` | “bus” does not begin with /s/ | Change both answer fields from true to false |
| `legacy-ali-0125` | Prompt says “red ball”; options are colored circles | Change prompt/title to “red circle” |
| `legacy-aboudy-0009` | Uses US cents | Change to 15 piastres |
| `legacy-aboudy-0093` | “My name is” is incomplete | Add a neutral example name and punctuation |
| `legacy-aboudy-0094` | “I am years old” is incomplete | Replace with a complete age-neutral sentence |
| `legacy-aboudy-0220` | Says plants get food from soil | Ask where plants get water and minerals |
| `legacy-aboudy-0221` | Seed-germination answer omits oxygen | Add water, air and warmth |
| `legacy-aboudy-0242` | “What shines brightly?” has multiple plausible answers | Ask for the main source of daytime light |
| `legacy-aboudy-0315` | Follow-up question has no passage | Attach the Sami weather passage or group it with a shared stimulus |
| `legacy-ali-0001` | Generic `/a/` notation is accent-ambiguous for “apple” | Use British English short-vowel notation `/æ/`; verify against the selected course edition |
| `legacy-ali-0007` | “sun” is represented with the wrong short-vowel phoneme | Change the blend stimulus to `/s/ - /ʌ/ - /n/` |
| `legacy-ali-0008` | “Pick the CVC word: DOG” has more than one valid answer | Ask which word begins with `/d/` so DOG is uniquely correct |

All changes are staging proposals with `REVISED_PENDING_TECHNICAL_QA`. The source bank remains untouched. Prompt, option, story or target changes require regenerated audio and renewed media validation.

## Open item-level issues

- `legacy-ali-0112`–`0115`: phoneme blending uses grapheme letters as if they were phoneme audio. Requires an accent-checked phoneme rewrite and separate sound assets.
- `legacy-ali-0219`–`0221`: preposition items need a visual scene or unambiguous wording.
- `legacy-ali-0222`: right-hand assumption; convert to a non-graded self-report or replace.
- `legacy-ali-0225`: animal-length comparison needs measured values or a scale illustration.
- `legacy-aboudy-0283`–`0286` and `0288`: stale Arabic explanation payloads refer to unrelated questions; discard or regenerate before any future explanation-enabled migration.

## Renderer requirements

The 616-item source bank has:
- 468 multiple-choice response items;
- 28 explicitly typed specialized option-bearing interactions;
- 120 custom no-option interactions;
- within the multiple-choice group, 19 story-bearing and 15 structured-visual items.

The 34 structured-stimulus multiple-choice records need passage/visual rendering in addition to the ordinary option controls. See `content/staging/legacy-question-bank-renderer-requirements-v0.1.json`.

## Batch-level notes

- Batch 03: one confirmed phonics answer-key error (`legacy-ali-0073`).
- Batch 04: 20 phoneme true/false items checked; all 20 keys correct.
- Batch 05: four blend items need phoneme-level stimulus; one red-ball/circle mismatch.
- Batches 06–07: count/number tasks structurally sound; several generic Arabic helpers do not match the operation or use case.
- Batches 08 and 10–20: no additional keyed-answer error found in this pass; structured stories/visuals and helper mismatches still require renderer and item review.
- Batch 09: preposition and length-comparison items are under-specified; one right-handedness assumption.
- Batch 21: confirmed plant-food misconception and incomplete germination conditions.
- Batch 22: ambiguous “shines brightly” prompt clarified in the staging patch.
- Batch 24: stale Arabic explanation payloads in sound/spelling items.
- Batch 25: one follow-up comprehension item missing its passage.

## Approval boundary

No legacy record is production-approved. The staging correction patch must pass technical QA, content/media regeneration, Content Validation and Android CI before any migration. Typed renderers must be implemented before specialized/custom activities are integrated. Arabic helper/explanation fields remain excluded from the child runtime.


## Listening target-leak scan

A deterministic scan found 69 multiple-choice items whose visible prompt contains the exact target answer. These are letter, word and number listening tasks. The target must be stored as a separate audio stimulus and the visible prompt must be neutral. Their IDs are listed in `content/staging/legacy-question-bank-renderer-requirements-v0.1.json`; they are not approved for listening assessment until the presentation is corrected.


## Additional listening assessment repair — 24 November 2026

- Re-read the original 616 legacy records and cross-checked the renderer-requirements register.
- Confirmed 69 listening multiple-choice items expose their target in the visible prompt: 25 letters, 34 words and 10 numerals.
- Added `content/staging/legacy-question-bank-listening-target-leak-patch-v0.1.json` with neutral visible prompts and separate hidden audio stimuli.
- The target must be played as audio and must not be rendered as text. All 69 require regenerated audio and renderer support that is not yet implemented.
- All remain `REVISED_PENDING_AUDIO_AND_RENDERER_QA`; none is production-approved or runtime-integrated.
- Manual scan of legacy batches 01–05 confirmed the previously recorded phoneme/CVC findings and listening-prompt issue. Structural and answer-key checks do not replace a complete pedagogical review of all 616 records.


## Legacy batches 01–05: second-pass language and phoneme corrections — 24 November 2026

- Added `content/staging/legacy-question-bank-quality-patch-v0.2.json` with 12 additional proposals.
- Corrected the false phonics statement for BUS (/b/, not /s/).
- Standardised short-a notation to British English /æ/ for four true/false items (apple, ant, egg, at).
- Corrected seven Arabic helper strings: the H _ T gap, four CVC blending items (CAT/DOG/PIG/SUN), and two sentence-to-picture tasks.
- All proposals remain pending technical QA and are not applied to the source bank or runtime pack.


## Legacy Arabic explanation audit — 24 November 2026

- A full scan of the 68 Arabic `explanationAr` entries found 12 repeated explanation strings pairing Ali's content with different Aboudy tasks.
- The mismatched records are `legacy-aboudy-0279` through `legacy-aboudy-0290`; examples include a /sh/ item carrying an /m/ explanation, TRAIN carrying PIG feedback, and a shells subtraction problem carrying a dots-array explanation.
- Added `content/staging/legacy-question-bank-arabic-explanation-patch-v0.3.json` with 12 learner-specific replacements.
- All replacements remain `REVISED_PENDING_TECHNICAL_QA`; the source legacy bank has not been modified.


## Listening target-leak correction — two picture prompts found in batches 11–13

A second scan of all visible `Listen` prompts found two additional items missed by the original 69-item list:
- `legacy-ali-0295`: the visible prompt prints “fish” while the learner is asked to choose the fish picture.
- `legacy-ali-0296`: the visible prompt prints “tree” while the learner is asked to choose the tree picture.

Both are now added to the listening target-leak patch as `picture-word` items. The revised inventory is 71 items: 25 letters, 34 words, 10 numbers and 2 picture-listening targets. The target word must remain a separate audio stimulus; the visible prompt is neutral. Both still require regenerated audio and a renderer capable of playing hidden prompt audio.

## Batch 11–13 helper alignment review

A first-pass review covered 75 records: Ali KG2 `legacy-ali-0251`–`0301` and Aboudy Grade 2 `legacy-aboudy-0001`–`0024`. Eleven Arabic helper mismatches were staged in `content/staging/legacy-question-bank-helper-alignment-batch-11-13-v0.1.json`:
- Four Word Builder helpers incorrectly analyse the English token “for” instead of guiding the spelling task (`0275`–`0278`).
- The two-row dots item (`0280`) carries place-value guidance instead of array/counting guidance.
- Picture Detective and Daily Story items (`0291`, `0292`, `0300`, `0301`) carry generic word-sound/meaning helpers instead of category or story-comprehension guidance.
- Aboudy weekday-capitalisation item `0005` has a pronunciation helper unrelated to the assessed skill.
- Aboudy place-value item `0007` helper omits the hundreds place.

Other important limits retained: `legacy-aboudy-0004` stores its actual comprehension question separately from its generic title; the reading-passage renderer must show both story and question. Specialized types (ten-frame, number line, place value, ruler, compare, spelling, true/false, clock, money, graph and word problem) remain blocked until typed renderers exist. All helper changes remain staging-only and the old source repository is untouched.
