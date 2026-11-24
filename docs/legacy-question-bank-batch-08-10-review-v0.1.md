# Legacy question bank — batches 08–10 review v0.1

**Review date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Scope:** 75 Ali KG2 records across LEGACY-08 to LEGACY-10.

## Confirmed findings

- Shape items `0190`–`0193` provide text-only shape names. If the intended skill is visual shape recognition, they need illustrated options; otherwise they assess reading shape vocabulary.
- Spatial items `0219` and `0221` depend on a bird/tree or sun/clouds scene that is not stored in the item. These remain blocked pending visual stimulus.
- `0220` is more precise when it refers to the water surface rather than saying a fish is “below the water”.
- `0222` assumes the learner writes with the right hand. The proposed wording tests right/left body orientation without assuming handedness.
- `0223` is scientifically more accurate as “mostly on the left side of the chest”.
- `0225` compares a snake, worm and ant without measured lengths or a scale; the item needs visual/measurement evidence.
- `0245` has a top-level flower prompt while the story's actual question asks what helped a seed grow. The prompt and title are aligned in the staged proposal.
- Helper text for `0228`–`0230` is aligned with qualitative mass and capacity comparison.
- Duplicate arithmetic prompts `0235`, `0236` and `0238` remain covered by the separate duplicate-correction patch; they are not duplicated here.

## Staged proposals

Patch: `content/staging/legacy-question-bank-context-and-helper-alignment-batch-08-10-v0.1.json` (14 records): 4 prompt/title corrections and 13 helper corrections. Seven visual/measurement issues remain open.

All proposals remain pending technical QA. Prompt changes require audio regeneration if these legacy items are later migrated into runtime. Helper audio is not exposed in the current child runtime. No source-bank record or runtime content was changed.
