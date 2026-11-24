# Legacy question bank — batches 17–19 review v0.1

**Review date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Scope:** 75 Aboudy Grade 2 records, `legacy-aboudy-0100`–`0175`.

## Answer-key review

The inspected sequence covers place value, skip counting, even/odd, addition and subtraction to 100, multiplication facts, time units, clock vocabulary, single-digit addition/subtraction, tens/ones, comparisons and story sums. Arithmetic and number-sequence keys were recalculated against their prompts; the inspected keys are consistent, including:
- 23 + 14 = 37; 38 + 25 = 63; 56 + 27 = 83; 49 + 38 = 87.
- 65 − 28 = 37; 90 − 45 = 45; 83 − 36 = 47.
- 2×3 through 4×8 and 10×7 multiplication facts.
- 7 + 5 = 12; 9 + 6 = 15; 14 − 6 = 8; 20 − 9 = 11.
- Two-digit place value, skip-counting sequences, and the three story-sum keys.

## Staged helper corrections

Patch: `content/staging/legacy-question-bank-maths-helper-alignment-batch-17-19-v0.1.json` (26 proposals).
- `0100`: align with comparing hundreds, then tens and ones.
- `0105`: explain the ones-digit rule for even numbers.
- `0126`–`0130`: align helpers with minutes/hour, hours/day, half past, quarter past and days/week.
- `0131`–`0136`: replace tens/regrouping language with counting forward for single-digit additions to ten.
- `0143`–`0148`: replace tens/borrowing language with counting backward for single-digit subtractions.
- `0169`–`0172`: explain comparing tens first, then ones.
- `0173`–`0175`: align story-problem helpers with the actual addition/take-away context.

All proposals remain pending and staging-only. Arabic helper audio is not exposed in the current child runtime; any future helper playback requires regenerated recordings and review. The source bank was not changed.
