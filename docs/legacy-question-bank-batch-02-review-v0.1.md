# Legacy question bank — batch 02 review v0.1

**Review date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Review queue:** `content/staging/legacy-question-bank-review-queue-v0.1.json`  
**Batch:** `LEGACY-02` — 25 items, Ali / KG2 / English

## Scope inspected

- IDs: `legacy-ali-0026` through `legacy-ali-0050`.
- 18 letter-recognition items (H–Z, with the source sequence omitting G and L in this batch).
- 7 short-word matching items: cat, dog, pig, hen, bat, fox and sun.
- All 25 are `mc_text`; none is a specialized interaction or a custom-response activity.

## Confirmed structural and media findings

| Check | Result |
|---|---:|
| Items inspected | 25 |
| Four-option items with distinct option text | 25/25 |
| Correct answer occurs exactly once | 25/25 |
| Audio references per item | 12 |
| Total audio references | 300 |
| Unique audio files referenced | 74 |
| Missing audio files in source snapshot | 0 |
| Prompt / option / helper text represented in speech manifest | 25/25 |

The source speech manifest contains 3,020 entries. All 74 unique audio IDs referenced by this batch resolve to an `.ogg` file in the same source snapshot. For every item, manifest text covers the spoken prompt, all four option labels, and the legacy Arabic helper. This is a metadata-to-file check; it does not establish acoustic pronunciation quality or playback on a device.

## Item-level pedagogical assessment

### Letter items — 18

The keyed letter matches one and only one of four distinct uppercase options. The selected letters and distractors are visually distinguishable and the task is suitable as a basic KG2 letter-identification exercise.

**Presentation issue to resolve before migration:** the visible prompt strings use the form `Listen and find: H`, with the target letter embedded in the text. If the child renderer displays that string, it reveals the answer before the child responds. Keep the target in the audio stimulus, but render a neutral on-screen instruction (for example, “Listen and choose the letter you hear”) and expose the spoken target through an explicit audio-stimulus control. Do not simply remove the target from both text and audio.

### Word items — 7

The keyed word is present exactly once among four distinct written options. The audio manifest contains the spoken prompt and each option. The words are short, familiar CVC/CVC-like forms suitable for early word recognition.

**Presentation issue:** the visible prompt embeds the target word (for example, `Listen and find: cat`). As with the letter items, the migration must separate neutral visible instructions from the audio target stimulus. Keep the option audio available independently.

## Mapping and calibration limits

- The legacy labels “KG2 English”, “Letters and sounds”, and “First words” describe the source bank's own structure; they do not prove alignment to one of the seven selected publisher books or a specific publisher unit.
- Difficulty remains `legacy-unrated`. No calibrated difficulty level is assigned.
- The source items contain no question-image references; this batch is text-option based. Do not invent visual assets merely to satisfy the image coverage of other question types.
- The existing new-app activity contract and renderer must be checked before integration. A generic multiple-choice screen is insufficient if it cannot present a separate audio stimulus without revealing the target in visible prompt text.

## Review disposition

**Partially reviewed — not production-approved.**

Confirmed: answer-key structure, option uniqueness, source audio-file presence, and speech-manifest text coverage.

Still open for all 25 items: neutral visible prompt plus separate audio stimulus design, actual audio listening QA, calibrated difficulty, final seven-book/unit mapping, and target-device playback validation. The 25 queue records remain pending and all seven review gates remain unchecked; this report does not alter the source bank or approve the items for a production pack.
