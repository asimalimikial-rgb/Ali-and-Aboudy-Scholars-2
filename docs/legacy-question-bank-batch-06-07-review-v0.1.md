# Legacy question bank — batches 06–07 review v0.1

**Review date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Scope:** `LEGACY-06` and `LEGACY-07` — 50 Ali / KG2 records (`legacy-ali-0126`–`legacy-ali-0175`).

## 1. Item inventory

| Item group | Count | Review result |
|---|---:|---|
| Sentence-to-picture matching | 1 | Answer/icon is consistent; Arabic helper was about word sounds rather than sentence meaning |
| Count/tap-count interactions | 16 | Numeric targets match the stored count fields; renderer must display the item repeated by the count and capture one tap per object |
| Listening number recognition | 10 | Keys occur once among four options; visible prompt exposes the target number |
| Addition facts within 10 | 10 | All 10 answer keys manually recalculated and correct |
| Subtraction facts within 10 | 5 | All 5 answer keys manually recalculated and correct |
| More/fewer/size/height/mass comparison | 8 | Answer keys match the presented symbols/objects; mass item is a qualitative comparison, not a measured-mass exercise |

## 2. Confirmed issues and staged proposals

- `legacy-ali-0126`: the prompt is “Read and match: The sun is hot.” but the Arabic helper asks the learner to analyse the sound/meaning/spelling of the word “hot”. A sentence-to-picture helper is proposed.
- `legacy-ali-0137`–`0146`: the ten listening-number prompts visibly include the target number. These are already included in the 69-item listening-target-leak register; the number must be spoken as a separate hidden audio stimulus and not printed in the visible prompt. The old Arabic helper is generic and does not explain the listening action.
- `legacy-ali-0147`–`0156`: addition items are single-digit facts within ten, but the repeated Arabic helper discusses tens, regrouping and carrying. This is not the assessed operation or KG2 demand. Proposed helper teaches counting forward.
- `legacy-ali-0157`–`0161`: subtraction items are single-digit facts within ten, but the repeated helper discusses tens and borrowing. Proposed helper teaches counting backward.
- `legacy-ali-0166`–`0168`: generic helper text refers to numeric/shape representations instead of comparing size or height. Item-specific helper proposals are staged.
- `legacy-ali-0169`: the task compares familiar objects by usual mass, but the old helper instructs unit conversion even though no measured values or units are shown. A qualitative mass-comparison helper is proposed.

Patch: `content/staging/legacy-question-bank-helper-alignment-batch-06-07-v0.1.json` (30 records). All proposals remain `REVISED_PENDING_TECHNICAL_QA`, with `productionApproved=false` and `runtimeIntegrated=false`.

Arabic helpers are excluded from the current child runtime. No Arabic helper audio is required for the current runtime; if a future release exposes these helper fields, the associated Arabic helper recordings must be regenerated and acoustically reviewed.

## 3. Technical and curriculum limits

- The 16 count activities require a dedicated tap-count renderer; a single emoji field is a repeated-object template, not a ready-made visual scene.
- The ten number-listening items require separate audio-stimulus presentation; a plain text prompt leaks the target.
- The current Android renderer supports only `SINGLE_CHOICE`; count interactions remain outside runtime support.
- The source labels describe the legacy bank's own sequence and do not prove alignment to one of the seven selected books.
- Difficulty remains `legacy-unrated`; no calibrated level is inferred in this review.

## 4. Disposition

**Partially reviewed — not production-approved.** The 15 arithmetic keys were manually checked; the 16 count targets and eight comparison keys were checked against their payloads. Helper proposals are staging-only. No source-bank record, production content pack, or runtime renderer was changed.
