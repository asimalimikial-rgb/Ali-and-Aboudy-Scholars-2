# Legacy question bank — batches 11–13 review v0.1

**Review date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Scope:** 75 records — Ali KG2 `legacy-ali-0251`–`0301`, Aboudy Grade 2 `legacy-aboudy-0001`–`0024`.

## Confirmed content checks

- Ten-frame and number-bond examples checked: the missing parts of ten, place-value examples, number-line step, and ruler-length arithmetic are consistent with their stored keys.
- Early phonics choices for /m/, /s/, /p/ and /f/ align with the keyed words.
- The four spelling targets MAP, PIG, SUN and NEST are present in their letter payloads.
- Aboudy grammar examples checked for proper nouns, plurals and past-tense forms are consistent with the intended answers.
- The listening picture tasks `legacy-ali-0295` and `0296` expose their target words in the visible prompt and are now included in the 71-item target-leak register.

## Staged helper corrections

Patch: `content/staging/legacy-question-bank-helper-alignment-batch-11-13-v0.1.json` (11 proposals).

- `0275`–`0278`: remove the incorrect helper reference to the word “for” and guide phoneme-to-letter word building.
- `0280`: replace place-value guidance with counting the two rows of a dot array.
- `0291` and `0292`: align helpers with category exclusion and identifying a reading object.
- `0300` and `0301`: use story-comprehension guidance rather than analysing the words “plant” and “grow”.
- Aboudy `0005`: align the helper with capital letters in weekday names.
- Aboudy `0007`: include hundreds, tens and ones in the place-value helper.

All proposals remain `REVISED_PENDING_TECHNICAL_QA`; Arabic helper fields remain excluded from the current child runtime. Any future playback of revised Arabic helper text requires new recordings and QA.

## Presentation and renderer blockers

- `legacy-ali-0295` and `0296` need neutral visible instructions plus separate hidden audio stimulus; a plain text prompt leaks the answer.
- `legacy-aboudy-0004` has a generic title while the actual comprehension question is stored separately; render the story and explicit question together.
- Ten-frame, number-line, place-value, ruler, compare, spelling, true/false, clock, money, graph and word-problem items require typed interaction renderers; they must not be coerced into generic single-choice.
- All 75 review-queue records remain pending; no source-bank record was changed or production-approved.
