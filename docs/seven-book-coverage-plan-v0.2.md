# Seven-book coverage and integration plan v0.2

**Updated:** 24 November 2026  
**Scope:** the seven books confirmed for Ali KG2 and Aboudy Grade 2. The SMILE book is excluded from this seven-book scope.

## 1. Confirmed seven-book set

| # | Learner | Book | ISBN reference | Current authored activities | Runtime book/unit/lesson integration |
|---|---|---|---|---:|---|
| 1 | Ali — KG2 | Hop, Skip and Jump — Senior KG Complete Kit | 9789356660113 | 65 | Not integrated |
| 2 | Ali — KG2 | Maths Xpress — Senior KG | 9789356664975 | 35 | Not integrated |
| 3 | Aboudy — Grade 2 | Macmillan English World — Level 2 Pupil's Book Pack | 9781035117154 | 35 | Not integrated |
| 4 | Aboudy — Grade 2 | Macmillan Mathematics — Level 2A | 9781380000637 | 34 | Not integrated |
| 5 | Aboudy — Grade 2 | Macmillan Mathematics — Level 2B | 9781380000613 | 6 | Not integrated |
| 6 | Aboudy — Grade 2 | Macmillan Science — Level 2 | 9781035130740 | 62 | Integrated: 2 units / 9 lessons |
| 7 | Aboudy — Grade 2 | Maths Xpress — Book 2 | 9789356664999 | 0 | Book shell only; no activities yet |

ISBNs are catalogue references, not a claim that the physical copy in the home is the same edition.

## 2. Current content inventory

The two bundled packs currently contain 282 English activities:

- Ali KG2: 108 English activities.
- Aboudy Grade 2: 174 English activities.
- The 200-question authored expansion is included in those totals (100 per child).
- The 62 Science activities are additional original Grade 2 work across Science Units 1 and 2.
- 25 broad Science enrichment activities remain separate from the Macmillan Science unit sequence until their topic mapping is verified.
- 20 earlier pilot activities remain to be reviewed and assigned to the appropriate book/unit.

The seven book candidate distribution currently documented is 65 + 35 + 35 + 34 + 6 + 62 + 0. The remaining 25 enrichment and 20 pilot activities are not silently counted as textbook coverage.

## 3. Integration state in the Android packs

- Grade 2 is schema v4 and currently defines the Macmillan Science Level 2 book, Units 1–2 and nine lessons. Its 62 Science activities have book/unit/lesson metadata.
- KG2 remains schema v3 and currently has no book, unit or lesson hierarchy.
- The other four books with existing activities do not yet have runtime book/unit/lesson mappings.
- Maths Xpress Book 2 has a catalog identity but no authored activities. Its unit sequence must be established from the actual scope before lessons are named or questions are assigned.
- The current app therefore has content associated with these subjects and books in the planning records, but it does not yet provide complete seven-book navigation or coverage.

## 4. Next implementation sequence

1. Add a stable seven-book catalog shared by both learner paths.
2. Upgrade KG2 content to the supported book hierarchy while preserving schema compatibility.
3. Add book/unit/lesson structures for English World Level 2 and Mathematics 2A/2B, then map each eligible activity by its actual skill.
4. Preserve the existing Science Units 1–2; author and validate Units 3–5 from the publisher's Level 2 scope sequence.
5. Keep the 25 broad Science enrichment activities clearly labelled as enrichment until each is matched to a verified topic.
6. Inspect the Maths Xpress Book 2 scope and create its hierarchy before authoring its first activity.
7. Review the 20 pilot activities and map them only after confirming the skill and book destination.
8. Reconcile all 282 existing activities with the seven-book catalog; then continue authoring to fill the uncovered lessons and skills.

## 5. Completion rule

A book is not complete because its title appears in the catalog or because a few activities are attached to it. Completion requires a coherent unit/lesson sequence, mapped skills, enough varied original activities for the intended learning outcomes, validated answer keys and distractors, complete local media references, and successful technical/content QA.

No book is marked complete in this update. The catalog and hierarchy work is an integration step, not a claim of full textbook coverage.


## Correction — Mathematics 2A/2B item-level mapping — 24 November 2026

The earlier broad-cluster split of 34 candidates for 2A and six for 2B is superseded. After inspecting the Grade 2 pack's 40 individual Mathematics activities against the publisher digital contents listings, the candidate allocation is 31 for 2A and nine for 2B. The detailed map is `content/staging/macmillan-mathematics-level2-activity-map-review-v0.1.json`; corrected totals are in `docs/seven-book-coverage-plan-v0.3.md`. No activity is runtime-mapped or approved by this correction.
