# Seven-book coverage plan v0.3

**Updated:** 24 November 2026  
**Branch:** `foundation/slice-0-android`  
**Status:** Coverage remains partial; this revision corrects the item-level Mathematics 2A/2B allocation.

## Selected books and current authored candidates

| # | Book | Learner | Authored candidates | Runtime-mapped | Current evidence/state |
|---|---|---|---:|---:|---|
| 1 | Hop, Skip and Jump — Senior KG Complete Kit | Ali / KG2 | 65 | 0 | Partial skill clusters only; kit components and publisher sequence remain open |
| 2 | Maths Xpress — Senior KG | Ali / KG2 | 35 | 0 | Initial numeracy clusters; unit sequence and broader coverage remain open |
| 3 | English World — Level 2 | Aboudy / Grade 2 | 35 | 0 | 15 candidate alignments, 20 supplementary/unmapped; all pending pedagogy review |
| 4 | Macmillan Mathematics — Level 2A | Aboudy / Grade 2 | 31 | 0 | 12-unit contents listing verified; 31 item-level candidate mappings across six units |
| 5 | Macmillan Mathematics — Level 2B | Aboudy / Grade 2 | 9 | 0 | 12-unit contents listing verified; nine item-level candidate mappings across four units |
| 6 | Macmillan Science — Level 2 | Aboudy / Grade 2 | 62 | 62 | Two units, nine lessons; Units 3–5 mapped at scope level but not yet authored |
| 7 | Maths Xpress — Book 2 | Aboudy / Grade 2 | 0 | 0 | Product identity confirmed; publisher contents sequence not verified |

**Seven-book authored-candidate subtotal: 237.** This is not a count of runtime-integrated activities or proof of complete textbook coverage.

Separate content retained outside the seven-book subtotal:
- 25 broad Science enrichment activities, still awaiting item-level mapping.
- 20 earlier pilot activities, still awaiting item-level reconciliation.

Reconciliation: 237 + 25 + 20 = 282 English activities in the current content inventory.

## Mathematics 2A/2B correction

The earlier v0.2 plan assigned 34 candidates to 2A and six to 2B using broad skill clusters. That split was too coarse: individual questions within the same skill cluster belong to different publisher units. The item-level review now assigns **31 to 2A and 9 to 2B**.

### 2A candidate mapping (31)

| Publisher unit | Title | Candidates |
|---|---|---:|
| 2 | Numbers to 999 | 10 |
| 3 | Addition to 99 | 4 |
| 5 | Subtraction within 99 | 3 |
| 6 | Multiplication | 8 |
| 10 | Fractions | 3 |
| 11 | Time | 3 |

Uncovered 2A units: 1 (Numbers to 100), 7 (Number facts), 9 (Equations and functions), and review units 4, 8 and 12.

### 2B candidate mapping (9)

| Publisher unit | Title | Candidates |
|---|---|---:|
| 14 | Addition and subtraction to 999 | 3 |
| 15 | Money | 2 |
| 21 | Measuring length | 2 |
| 22 | Measures problems | 2 |

Uncovered 2B units: 13 (Numbers and number patterns), 17 (Flat and solid shapes), 18 (Lines and shapes), 19 (Area of shapes), 23 (Handling data), and review units 16, 20 and 24.

The 40 activities are mapped in `content/staging/macmillan-mathematics-level2-activity-map-review-v0.1.json`. All remain `CANDIDATE_ALIGNMENT`, `PENDING_PEDAGOGY_REVIEW`, with `runtimeUnitId=null`. This is concept-level mapping, not page-level alignment. Exact family editions remain unconfirmed.

Initial deterministic QA: 40/40 keyed answers match independently calculated/factual expected answers; all have three distinct options and unique prompts. Answer-position distribution is A=12, B=15, C=13. All 40 currently have difficulty=2; this uniform value requires item-level calibration.

## Current runtime integration

- Only Macmillan Science Level 2 currently has publisher-aligned runtime hierarchy: two units, nine lessons, 62 activities.
- KG2 book/unit/lesson hierarchy remains unimplemented.
- English World Level 2 has a 12-unit official scope and 15 candidate alignments, but zero runtime unit mappings.
- Mathematics 2A and 2B have candidate maps, but zero runtime unit mappings.
- Maths Xpress Book 2 remains blocked on a verified publisher contents sequence.
- Hop Skip and Jump and Maths Xpress Senior KG remain partial.

## Next work

1. Review the 40 Mathematics items individually for age suitability, conceptual progression, distractor quality, language load, local context and difficulty calibration.
2. Add 2A/2B runtime hierarchy only after candidate mappings pass review.
3. Author Science Units 3–5 from the verified 18-topic scope with safety review.
4. Expand Hop Skip and Jump and Senior KG Maths Xpress coverage.
5. Continue searching for a verified Maths Xpress Book 2 contents sequence; do not infer units from another Macmillan series.
6. Complete English World item-level pedagogy review before runtime integration.

No changes to `main`, no APK or Release publication, and no signing changes were made.
