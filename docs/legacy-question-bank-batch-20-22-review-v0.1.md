# Legacy question bank — batches 20–22 review v0.1

**Review date:** 24 November 2026  
**Source snapshot:** `asimalimikial-rgb/Ali-andd-aboudy-scholars` at commit `bc9f397e29dba711f53aebe19ed5fa5a2ea5f4f8`  
**Scope:** 75 Aboudy Grade 2 records, `legacy-aboudy-0175`–`0249`.

## Review findings

- The batch spans equal-group multiplication, sharing/division, halves and quarters, money, clock reading, living things, plants, materials, animal groups, senses, body organs, weather, seasons, light/shadows and a short reading passage.
- The multiplication, equal-sharing, halves/quarters and basic science keys sampled in this pass align with their options.
- `legacy-aboudy-0220` (“Where do plants get food?” → “the soil”) is scientifically misleading: plants make their own food; soil supplies water and mineral nutrients. A separate staged patch already proposes “Where do plants get water and minerals?”.
- `legacy-aboudy-0221` omits oxygen from seed-germination conditions. The proposed complete answer is water, air and suitable warmth.
- `legacy-aboudy-0231` uses the grammatically unclear phrase “feeds milk”; the proposed wording states that mammals feed milk to their young.
- `legacy-aboudy-0242` is ambiguous because the moon and stars also shine; the proposed prompt specifies the Sun's daytime light.
- `legacy-aboudy-0244` assumes snowfall, which is not a suitable default example for Sudan; the proposed prompt asks which season is usually coldest.
- Money items `0206`–`0208` use British pence notation (“p”) and money-bag emoji. Proposed changes use coin imagery and Sudanese piastres without changing the numeric amounts.
- The queue assigns `legacy-aboudy-0175` to LEGACY-20. It has been removed from the previous batches 17–19 helper patch and is included here.

## Staged proposals

Patch: `content/staging/legacy-question-bank-localisation-and-science-batch-20-22-v0.1.json` (18 records).
- 11 Arabic helper corrections for equal sharing, halves/quarters and the window-material item.
- 4 science/language corrections for seed germination, mammals, daytime sunlight and seasonal wording.
- 3 currency localisations to piastres and coin imagery.
- 3 clock emoji items (`0209`–`0211`) remain open pending Android rendering verification; emoji clock glyphs can vary by font/vendor.

All proposals remain pending technical and pedagogical QA. Prompt changes require regenerated English audio. Arabic helper changes require new recordings only if that helper path is enabled in a future runtime. No source-bank or runtime content was changed.
