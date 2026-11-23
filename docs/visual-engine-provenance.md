# Visual engine extraction and AAS2 adaptation

## Source provenance
- Legacy repository: `asimalimikial-rgb/Ali-andd-aboudy-scholars`
- Source branch: `feature/v2-learning-visual-progress-phonics`
- Original file: `assets/visual-engine.js`
- Original Git blob SHA-1: `7f0a4a93665468429ba33560bf844fc6447f0a72`
- Engine version: `1.6.0`
- Extracted AAS2 copy: `tools/visuals/visual-engine.js`
- The extracted file header retains the original blob SHA for traceability.

## AAS2 build-time renderer
- Generator: `tools/visuals/generate-question-art.mjs`
- The renderer uses the extracted semantic SVG engine at build time; the engine is not packaged as an Android runtime dependency.
- The generator selects a visual profile from each activity's skill: phonics, grammar, reading, vocabulary, mathematics or science.
- Text-heavy grammar/reading options use legible word cards; math options use number cards and skill-specific scenes; counting questions render the actual counted objects; science/vocabulary/phonics use semantic visual kinds from the legacy engine.
- Each question image is generated locally as a PNG and stored under `app/src/main/assets/question-art/`.
- The PNG carries non-visible `tEXt` metadata for activity ID and visual profile. This replaces visible uniqueness dots; the artwork itself remains uncluttered.
- The manifest records activity ID, path, byte count, SHA-256, engine version, skill ID and visual profile.

## Offline and quality boundaries
- No external image API or network is required by the generator or the installed app.
- The Android app bundles the generated PNG assets, not the build-time JavaScript engine.
- Unique file hashes prove distinct file identities, not by themselves semantic or pedagogical quality. Representative visual review and real-device rendering checks remain required.
- The generator must fail if any activity lacks a prompt image, any output is corrupt, or two final PNG files have identical SHA-256 values.
