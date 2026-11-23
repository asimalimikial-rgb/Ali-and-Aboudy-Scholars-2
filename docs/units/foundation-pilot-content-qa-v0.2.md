# Foundation Pilot Content QA Review v0.2

**Date:** 23 November 2026  
**Repository:** AAS2-NEW  
**Branch:** `foundation/slice-0-android`  
**Content schema:** v3  
**Content pack version:** 0.2.0 for both learner paths

## Scope

This review covers the new original English activities added to the existing KG2 and Grade 2 pilot packs. It does not certify a complete curriculum, and it does not replace independent teacher/subject review.

## Content inventory

| Learner | Skills | English activities | Legacy Arabic variants | English items per skill |
|---|---:|---:|---:|---:|
| Ali — KG2 | 4 | 8 | 4 | 2 |
| Aboudy — Grade 2 | 6 | 12 | 6 | 2 |

The runtime continues to select English activities only. Legacy Arabic variants remain in the schema-v3 files for historical compatibility and are not surfaced in the child learning flow.

## QA findings and corrections

1. **KG2 counting item — missing stimulus:** the initial wording asked the learner to count stars but displayed no stars. The prompt now visibly contains four star symbols, matching the correct answer.
2. **KG2 phonological-awareness outcome:** the skill metadata previously mentioned both English and Arabic words. It now describes identification of initial sounds in familiar English words, consistent with the current English-only child pathway.
3. **Grade 2 measurement item — skill alignment:** the initial item asked which tool measures a desk, which did not directly assess comparing centimetre measurements. It now compares a 120 cm desk with a 95 cm table, and the answer key/feedback are aligned.
4. **Pack versioning:** both packs were incremented from content version 0.1.0 to 0.2.0. The content schema remains v3; no schema migration is involved.

## Automated acceptance checks

- Exact pack IDs and learner paths load through AssetContentPackRepository.
- Both packs contain the expected skill and activity totals.
- KG2 contains 8 English + 4 legacy Arabic records; Grade 2 contains 12 English + 6 legacy Arabic records.
- Every skill has exactly two English activities in this pilot version.
- The corrected KG2 counting stimulus and Grade 2 centimetre comparison are asserted in JVM tests.
- Existing ContentPackValidator checks all activity IDs, skill references, options, answer keys, feedback, source references and provenance metadata.

## Pedagogical and content limitations

- These remain small foundation pilots, not complete KG2 or Grade 2 curricula.
- Activities use original wording and answer options; no textbook question, passage, image, or page layout was copied.
- Each item is still a single-choice activity. No drag-and-drop, audio, handwriting, open response, or adaptive sequencing is claimed.
- The Grade 2 habitat item is an original habitat-recognition activity; future content should expand beyond repeated habitat examples to cover the full Macmillan Science Level 2 scope.
- KG2 Discovery is cross-curricular and is not represented as a separate core Science syllabus.
- A teacher/subject-matter review and child usability test remain required before production use.

## Rights and provenance

Existing skill-level source-reference IDs remain attached to each skill. The new question wording is original and is not a reproduction of a textbook. The referenced textbooks remain `REFERENCE_ONLY`; no copyrighted textbook material has been bundled.

## Release status

Content pack v0.2.0 is a development pilot. It is not a public release, and it does not authorize copying full books or source scans into the repository.
