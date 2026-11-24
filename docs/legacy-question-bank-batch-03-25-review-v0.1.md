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

Nine patch records are in `content/staging/legacy-question-bank-correction-patch-v0.1.json`:

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
