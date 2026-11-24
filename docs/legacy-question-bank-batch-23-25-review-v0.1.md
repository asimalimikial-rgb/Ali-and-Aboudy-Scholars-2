# Legacy question bank — batches 23–25 review v0.1

**Review date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Scope:** Final 66 records of the 616-item review queue, Aboudy Grade 2 `legacy-aboudy-0250`–`0315`.

## Content and answer review

- The final queue segment covers main idea, sequencing, inference, arrays, missing parts, measurement, phonics/spelling, word meaning, science, geography, pattern changes, and creative drawing.
- The inspected arithmetic and array keys align with the visible quantities and stored visual models (including 3×4=12, 2×5=10, 4 groups of 3=12, and the missing-part problems).
- The 12 Arabic explanation mismatches for `0279`–`0290` are already covered by the separately staged Arabic-explanation patch v0.3; they were not duplicated in this helper patch.
- Thirty Arabic helpers are unrelated, overly generic, or use stale template tokens such as “for”, “first”, “tiny”, “length”, or “umbrella”. These are staged in `legacy-question-bank-helper-alignment-batch-23-25-v0.1.json`.
- Several renderer-dependent tasks remain: story comprehension with passages, arrays, bar models, rulers, spelling letter tiles, true/false, and free drawing/tracing. They cannot be represented accurately as generic single-choice.

## Staged helper corrections

The 30 proposals align helper text with:
- Sequence words and inference.
- Arrays, equal groups and bar models.
- Missing addends and inverse-operation checks.
- Ruler reading and length units.
- Word-building and phoneme-to-letter tasks.
- Synonyms, category exclusion and story-based inference.
- Robot-limb addition and circle/shape recognition.

All helper proposals remain pending technical QA and pedagogical review. Arabic helper audio is not exposed in the current child runtime; future helper playback requires new recordings and QA. No legacy source record was changed or production-approved.
