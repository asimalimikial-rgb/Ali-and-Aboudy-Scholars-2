# Legacy question bank — pedagogy audit v0.1

**Audit date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Source file:** `content/question-bank/legacy-question-bank.json`  
**Scope audited:** the 616 records whose IDs begin with `legacy-`. The 200 `new-` records were excluded from this audit.

## 1. Confirmed inventory

| Measure | Result |
|---|---:|
| Legacy records | 616 |
| Ali — KG2 | 301 |
| Aboudy — Grade 2 | 315 |
| Objective items with answer options | 496 |
| Other interaction records | 120 |
| Records with blank prompt | 0 |
| Records missing a skill, book ID, or unit ID | 0 |
| Records marked with a calibrated difficulty | 0 |

The 496 objective candidates all passed this first structural gate: each has 3–4 distinct options and the keyed answer occurs exactly once. This is a structural result only; it does not establish that the prompt, distractors, answer key, difficulty or curriculum placement is pedagogically correct.

The 120 remaining records include counting, spelling, tracing/drawing, true/false, sentence building, matching, number line, comparison, comprehension, word problems, place value, clock, money and graph activities. They must retain their intended interaction type. They are not to be converted mechanically into single-choice questions.

## 2. Duplication review

A conservative comparison preserving numbers, mathematical operators, punctuation and visual symbols found 23 groups with repeated prompt wording. Most are legitimate parallel practice items: the stimulus, answer, options, child pathway or interaction differs.

Five groups are exact same-child content duplicates after comparing learner, type, prompt, answer and option set:

- Ali: `legacy-ali-0147` / `legacy-ali-0235`
- Ali: `legacy-ali-0148` / `legacy-ali-0236`
- Ali: `legacy-ali-0149` / `legacy-ali-0238`
- Aboudy: `legacy-aboudy-0019` / `legacy-aboudy-0082`
- Aboudy: `legacy-aboudy-0046` / `legacy-aboudy-0089`

One additional legacy item overlaps the current AAS2 pilot pack:

- `legacy-ali-0289` — “Which shape has three sides?” duplicates `kg2-shape-01-en`.

The first review patch revises the second item in each of the five exact duplicate pairs. The overlapping shape item is separately flagged for exclusion or replacement during migration; it is not part of patch batch 01. No source-bank record is deleted or altered.

Four option sets contain case-only contrasts (for example `i` / `I` and `emma` / `Emma`). These are meaningful where capitalization is the assessed skill and must not be collapsed by case-insensitive deduplication.

## 3. Open-response assessment

Two drawing activities have an empty fixed answer by design:

- `legacy-ali-0299` — draw a favourite thing.
- `legacy-aboudy-0313` — draw a scene from a known story.

They should remain open-response activities. The first review patch adds observable rubrics (recognisable subject/scene, relevant details, and a brief child explanation) instead of inventing one correct picture.

## 4. Difficulty and support-language findings

- All 616 legacy items use `legacy-unrated`. No difficulty level is inferred from the old labels. Each item needs an age-appropriate demand review against prerequisites, reading load, cognitive operation and independence.
- The source records contain an `arabicHelper` field. The current AAS2 content contract defers Arabic helper text and explanations in child activities by default. This field must not be exposed in the new child runtime; learning content remains English where English is the learning objective.
- Repeated helper strings in the old bank are not being treated as evidence of good pedagogy. They are historical fields, not a substitute for a question-specific teaching design.
- Legacy audio IDs are not assumed to remain valid after any prompt or option revision. The new offline English audio pipeline must generate and validate audio from the final approved text.

## 5. Review patch batch 01

File: `content/staging/legacy-question-bank-review-batch-01.json`

This batch contains seven reviewed changes covering 12 records:

- Five duplicate pairs: revise the second item in each pair with a distinct but same-skill prompt.
- Two drawing activities: add open-response rubrics and remove the expectation of a single keyed answer.

All patched records are marked `REVISED_PENDING_TECHNICAL_QA`. They are not yet approved for the production content packs.

## 6. Production acceptance gates

Every legacy item must pass the following before entering a production pack:

1. Correct learner path and age band: Ali KG2 or Aboudy Grade 2.
2. One primary observable skill and a defensible book/unit/lesson mapping; no invented chapter alignment.
3. Clear, self-contained English prompt with reading demand appropriate to the assessed skill.
4. Correct deterministic answer key for objective activities.
5. For single-choice items: 3–4 distinct options, exactly one correct answer, and plausible distractors tied to likely misconceptions.
6. Correct activity type and a renderer that genuinely supports that interaction.
7. Difficulty assigned only after reviewing prerequisite knowledge and cognitive demand.
8. No exact duplicate within the learner path or against the current 282 English activities.
9. No Arabic helper or explanation rendered in the child activity under the current MVP contract.
10. Final text, image and audio references validated together; audio must be regenerated after any spoken-text change.
11. Review state recorded separately for domain accuracy, pedagogy, language, media, and technical validation.

## 7. Current status

**Completed:** source bank inspected; 616 legacy records counted; structural MC gate run; duplicate groups and current-pack overlap identified; two open-response items identified; first revision patch committed.

**Still open:** item-level domain and pedagogy review of all 616; difficulty calibration; final seven-book mapping; review of distractor quality and reading load; support for the 120 custom interaction records; generation of new audio after approved text changes; integration into production packs and regression tests.

This report does not declare the 616-question bank complete or production-approved.
