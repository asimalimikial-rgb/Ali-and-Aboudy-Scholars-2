# AAS2 Original Offline Media Asset Register v0.1

**Date:** 23 November 2026  
**Repository:** `asimalimikial-rgb/Ali-and-Aboudy-Scholars-2`  
**Branch:** `foundation/slice-0-android`  
**Status:** Initial KG2 illustration set added; visual device QA remains open.

## Rights and production rule

The six images listed here are original project-created illustrations drawn for AAS2. They are not copied from, traced from, cropped from, or extracted from Macmillan, Sudanese, or other textbook pages. No third-party image source or external licence is claimed for these files. Keep this register current for every later asset; do not infer image rights from the software or curriculum-source licences.

## Bundled files

All files are 192 × 192 PNG raster images stored beneath `app/src/main/assets/`.

| Asset path | Visual subject | Current use |
|---|---|---|
| `option-art/shape-circle.png` | Circle with a contrasting outline | Ali KG2 shape recognition |
| `option-art/shape-square.png` | Square with a contrasting outline | Ali KG2 shape recognition |
| `option-art/shape-triangle.png` | Triangle with a contrasting outline | Ali KG2 shape recognition |
| `option-art/sun.png` | Sun illustration | Ali KG2 initial-sound choice |
| `option-art/ball.png` | Ball illustration | Ali KG2 initial-sound choice |
| `option-art/cat.png` | Cat-face illustration | Ali KG2 initial-sound choice |

## Content links

- `kg2-shape-01-en`: circle, square, triangle.
- `kg2-shape-02-en`: circle, triangle, square.
- `kg2-sound-01-en`: sun, ball, cat.

The images supplement visible text labels. They are not intended to be the sole carrier of an answer, and they do not replace the question text.

## Integrity and fallback

- The content pack uses relative paths under the Android assets root; no URL or external request is involved.
- `ContentPackValidator` rejects unsafe paths (absolute paths, traversal segments, backslashes and URL-like paths).
- The renderer decodes raster files on the IO dispatcher and downsamples large images.
- Missing, corrupt or unsupported image files are omitted; the text answer remains available.
- `BundledContentPackAssetsTest` verifies that every image referenced by either bundled pack exists, exceeds the minimum file size, and begins with the PNG signature.
- The JVM test does not prove that Android decodes and displays each image correctly on a physical device. That remains part of emulator/device QA.

## Remaining media work

- Add Grade 2 illustrations only when a reviewed item benefits from a visual stimulus and the image does not reveal the answer.
- Add runtime tests for missing and corrupt image files, including text fallback.
- Inspect the six images at actual phone scale and with accessibility settings enabled.
- Maintain per-file provenance and rights review for every later image or audio asset.

## Grade 2 habitat illustration set — 23 November 2026

The first Grade 2 visual set supports the two original habitat-choice activities. All five files are original AAS2 project-created PNGs (192 × 192); they are not textbook scans, traces or derivatives.

| Asset path | Visual subject | Linked activity options |
|---|---|---|
| `option-art/river.png` | River habitat | `g2-habitat-01-en` — A river |
| `option-art/desert.png` | Desert habitat | `g2-habitat-01-en` — A dry desert; `g2-habitat-02-en` — A desert |
| `option-art/oven.png` | Hot oven | `g2-habitat-01-en` — A hot oven |
| `option-art/coral-reef.png` | Coral reef | `g2-habitat-02-en` — A coral reef |
| `option-art/deep-ocean.png` | Deep ocean | `g2-habitat-02-en` — A deep ocean |

The shared JVM asset-integrity test now expects eleven unique referenced illustrations across both packs. Other Grade 2 skills remain text-only until a reviewed learning item benefits from an image.
