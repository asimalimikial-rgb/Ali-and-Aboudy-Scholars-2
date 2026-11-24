# Legacy question bank — batches 14–16 review v0.1

**Review date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Scope:** 75 Aboudy Grade 2 records, `legacy-aboudy-0025`–`0099`.

## Confirmed findings

- The batch covers nouns, adjectives, opposites, punctuation, reading passages, capitalization, true/false noun/verb classification, sentence building and place value.
- The objective answer keys in the inspected examples align with the options and stored values; the key issue is not a general answer-key failure but ambiguous grammar prompts and mismatched Arabic helper text.
- The 12 noun/verb true/false items (`0066`–`0077`) ask whether isolated words are nouns or verbs. Several words can function as both depending on sentence context (notably run, jump, read, table and chair). The proposed revision adds short sentence contexts while preserving the intended true/false key.
- The 15 reading items `0045`–`0059` store their story passage separately in `legacyData.story`; the current prompt contains only the question. The renderer must display the passage and the question together. The same requirement applies to `0089`–`0092`.
- Punctuation items `0040`–`0044` have helpers that discuss word pronunciation instead of terminal punctuation.
- Noun/adjective items `0025`–`0030` and place-value items `0096`–`0097` also contain generic or incomplete helpers.

## Staged correction proposal

Patch: `content/staging/legacy-question-bank-context-and-helper-alignment-batch-14-16-v0.1.json` (44 records).

- 12 true/false noun/verb prompts now have sentence context in the proposal, with matching legacy title updates and skill-aligned helper text.
- 32 helper-only proposals align noun/adjective, punctuation, reading-comprehension and place-value support with the actual task.
- The 12 English prompt changes require regenerated English prompt audio. Arabic helper recordings remain outside the current child runtime; if exposed in a future version, their audio must be regenerated and reviewed.

## Review limits

- These are staging proposals only; all records remain pending technical QA and pedagogical review.
- The 19 reading items require a passage-aware renderer; they must not be presented as isolated questions without their story.
- True/false items require a dedicated binary-response renderer; they are not generic single-choice.
- The old source bank remains unchanged. No record is production-approved or runtime-integrated.
