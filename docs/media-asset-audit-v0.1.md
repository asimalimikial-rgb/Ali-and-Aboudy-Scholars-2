# Offline media asset audit v0.1

**Audit date:** 24 November 2026  
**Branch:** `foundation/slice-0-android`

## Audio inventory

The checked-in speech manifest and asset tree confirm:

- 1,692 OGG files for 282 English activities.
- 282 prompt clips.
- 846 option clips (three per activity).
- 282 correct-feedback clips.
- 282 incorrect-feedback clips.
- Language: English only.
- Generation speed: 0.70.
- Total OGG payload size recorded by the manifest: 25,630,162 bytes.
- Every manifest entry has a unique filename and SHA-256, positive byte length, and duration of at least 100 ms.

The new content validation workflow checks every OGG file exists and verifies all activity-level prompt, option and feedback paths against the bundled packs.

## Question illustrations

The question-art manifest contains 282 unique activity records:

- 245 SVG.
- 37 PNG.
- 282 unique activity IDs, relative paths and SHA-256 values.
- No missing hash, byte-length or activity-ID metadata.

Visual profile distribution: Math 75, Science 116, Vocabulary 12, English Grammar 31, Reading 6, Phonics 32, English 10.

The manifest has 71 items without a specific `matchedPromptKinds` value. These are profile-based/text-oriented illustrations, not automatically classified as failures. Their visual clarity and usefulness still require a separate visual review.

## Option illustrations

The option-art manifest contains 374 SVG files. All paths and SHA-256 values are unique and all entries have size/hash metadata.

## Runtime and QA boundary

- The asset and manifest checks establish that files exist and their metadata is internally consistent.
- They do not prove that every image is semantically ideal, legible at the child's device size, or accessible through TalkBack.
- They do not prove that audio plays correctly on the target Android phone, in Airplane Mode, after pause/resume, screen lock, or activity changes.
- Physical-device playback, visual inspection, TalkBack review, and failure/fallback testing remain open.

## Automated regression gate

`tools/content/validate-staging-content.mjs` now checks the seven-book catalog, the first legacy review patch, all 1,692 OGG files, all 282 question illustrations, all 374 option illustrations, and every activity-to-media reference. The gate runs in the dedicated Content validation GitHub Actions workflow.
