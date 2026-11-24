# Seven-book provisional unit map review v0.1

**Updated:** 24 November 2026  
**Purpose:** prepare consistent AAS2 unit/lesson groupings before adding runtime book navigation. These are AAS2 skill groupings, not claims about the publisher's printed chapter numbering unless an official scope sequence is explicitly recorded.

## Ali — KG2

### 1. Hop, Skip and Jump — Senior KG Complete Kit

| Provisional AAS2 unit | Skill group | Current candidate activities | Mapping state |
|---|---|---:|---|
| HSKJ-KG2-U01 | Initial sounds and letter–sound awareness | 20 | Candidate grouping |
| HSKJ-KG2-U02 | Rhyming words | 10 | Candidate grouping |
| HSKJ-KG2-U03 | Everyday vocabulary | 10 | Candidate grouping |
| HSKJ-KG2-U04 | My World Discovery | 25 | Candidate grouping |
| **Total** | | **65** | Not yet in Android runtime |

The eight earlier KG2 pilot activities are not included in this 65-item authored expansion and still need a book/unit decision.

### 2. Maths Xpress — Senior KG

| Provisional AAS2 unit | Skill group | Current candidate activities | Mapping state |
|---|---|---:|---|
| MX-SKG-U01 | Counting pictures and number sense | 10 | Candidate grouping |
| MX-SKG-U02 | Number patterns | 10 | Candidate grouping |
| MX-SKG-U03 | Adding and taking away | 10 | Candidate grouping |
| MX-SKG-U04 | Shapes | 5 | Candidate grouping |
| **Total** | | **35** | Not yet in Android runtime |

## Aboudy — Grade 2

### 3. English World — Level 2

The official 12-unit sequence is documented in docs/english-world-level2-scope-map-v0.1.md. The 35 current English activities are tracked in content/staging/english-world-level2-activity-map-review-v0.1.json:

- 15 candidate unit alignments after three targeted rewrites.
- 20 supplementary/unmapped activities.
- 0 runtime unit mappings until pedagogy review is complete.

### 4. Macmillan Mathematics — Level 2A

| Provisional AAS2 unit | Skill group | Current candidate activities | Mapping state |
|---|---|---:|---|
| MM2A-U01 | Place value to 1,000 | 10 | Candidate grouping |
| MM2A-U02 | Addition and subtraction | 10 | Candidate grouping |
| MM2A-U03 | Multiplication | 8 | Candidate grouping |
| MM2A-U04 | Fractions and measurement | 6 | Candidate grouping |
| **Total** | | **34** | Not yet in Android runtime |

### 5. Macmillan Mathematics — Level 2B

| Provisional AAS2 unit | Skill group | Current candidate activities | Mapping state |
|---|---|---:|---|
| MM2B-U01 | Measure and apply | 6 | Candidate grouping |
| **Total** | | **6** | Not yet in Android runtime |

The 2A/2B groupings are AAS2 skill clusters. Publisher unit numbers/titles are not inferred from these groupings.

### 6. Macmillan Science — Level 2

- Integrated: Unit 1 — Plant Life and Plant Uses (4 lessons, 32 activities).
- Integrated: Unit 2 — Animals and Survival (5 lessons, 30 activities).
- Runtime mapping: 62 activities across 9 lessons.
- Open: Units 3–5 — Senses and Our Bodies; Materials and Physical Properties; The Sky at Night.

### 7. Maths Xpress — Book 2

- Catalog identity recorded.
- No current activities.
- Exact scope/unit sequence not yet verified; do not duplicate the 2A/2B question groups or invent publisher unit titles.
- Next: inspect the actual Book 2 contents, then create its AAS2 unit map and author distinct activities.

## Reconciliation

| Content group | Activities |
|---|---:|
| Seven-book authored candidates | 237 |
| Broad Science enrichment awaiting unit mapping | 25 |
| Earlier pilot activities awaiting book mapping | 20 |
| **Current English activity total** | **282** |

Only the 62 Science activities currently have runtime book/unit/lesson mappings. All other groups above are planning candidates, not completed textbook coverage. The next implementation step is to review and map these groupings item-by-item, then add the approved hierarchy to the content packs and app.


## Correction — 24 November 2026

The former 34/6 Mathematics 2A/2B split was a broad-skill estimate. Publisher-origin contents listings expose 2A Units 1–12 and 2B Units 13–24. Item-level candidate review now assigns 31 of the 40 Mathematics activities to 2A and nine to 2B. See `docs/macmillan-mathematics-level2-unit-map-review-v0.1.md` and `content/staging/macmillan-mathematics-level2-activity-map-review-v0.1.json`. All 40 remain pending pedagogy review and none has a runtime unit ID.
