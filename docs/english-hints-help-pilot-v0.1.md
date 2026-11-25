# English-only hints and guided-help pilot v0.1

**Status:** Implemented on `foundation/english-only-hints-help`; content QA pilot, not a full-bank completion.
**Product rule:** No Arabic in child learning content. The learner experience remains English-only regardless of device locale. Parent-facing localization is separate.

## Scope

- 16 activities received authored support content: 8 KG2 and 8 Grade 2.
- KG2 pack: `0.2.6`, schema 3.
- Grade 2 pack: `0.4.6`, schema 4.
- Existing activity IDs, answer keys, audio paths, image paths, Room schema and learner history are preserved.
- No Arabic support is generated from legacy Arabic activity records.

## Support contract

- `hintSequence`: 1–3 progressive prompts. A child reveals one at a time.
- `helpSteps`: 1–4 short teaching steps. A child reveals one at a time.
- Optional `hintImageAssetPath` and `helpImageAssetPath` allow a separate, relevant visual model.
- Every hint/help string is English-only, nonblank and length-limited. Hints are capped at 180 characters; help steps at 240.
- Arabic text or support on a historical Arabic activity is rejected by pack validation.
- Any support interaction sets the existing `LearningAttemptEntity.usedHint`; no Room migration is required.
- Supported attempts remain visible in history and are not counted as unaided evidence.

## Authored pilot activity IDs

### Ali — KG2
- `kg2-count-01-en`
- `kg2-shape-01-en`
- `kg2-plant-01-en`
- `kg2-sound-01-en`
- `kg2-count-02-en`
- `kg2-shape-02-en`
- `kg2-plant-02-en`
- `kg2-sound-02-en`

### Aboudy — Grade 2
- `g2-place-01-en`
- `g2-add-01-en`
- `g2-measure-01-en`
- `g2-habitat-01-en`
- `g2-vocab-01-en`
- `g2-sentence-01-en`
- `g2-place-02-en`
- `g2-add-02-en`

## QA rules before extending the bank

1. Hints should direct attention to the relevant concept, representation or strategy without stating the answer.
2. Help steps should model a short thinking process, not repeat the prompt or give the final option.
3. KG2: concrete language, one action per step, familiar objects, very short sentences.
4. Grade 2: subject vocabulary, explicit reasoning and transferable strategies.
5. Audio for hints/help is not yet generated. Existing six audio clips per activity remain unchanged; adding spoken support requires a separate text/audio manifest extension and integrity checks.
6. Images are not automatically reused as "hint images". Add a separate visual only when it clarifies a misconception or demonstrates a step.
7. A content reviewer must check answer leakage, age fit, scientific/mathematical correctness, grammar, and consistency with the question and image.

## Known remaining work

- The other 266 English activities still need authored and reviewed hints/help.
- The 23 English activities without option images still need item-level visual audit and, where useful, original option illustrations.
- All 282 prompt images and 259 existing option-image sets need semantic and visual QA, not just file-integrity checks.
- Existing speech manifest integrity and audio quality still require end-to-end device validation.
