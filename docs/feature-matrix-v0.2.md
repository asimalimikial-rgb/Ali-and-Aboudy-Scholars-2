# AAS2 Feature Inventory and Unified Feature Matrices v0.2

**Status:** Evidence-graded planning artifact; not final product approval or implementation authorization.  
**Review date:** 23 November 2026  
**Scope:** AAS2-NEW only — Ali KG2 and Aboudy Grade 2.  
**Canonical inventory:** EDU-001–EDU-043. No new feature IDs were created in this review.  
**Authority:** Notion project record sections 17–22 and 44–45; current product decisions in section 20 and corrected next-step instruction under section 90.

## 1. Audit result and rules

- The canonical Notion inventory contains exactly 43 IDs: EDU-001 through EDU-043.
- No later EDU ID was found in the current project record or retrieved project context. This is a bounded audit of available records, not a claim that every historical chat has been exhaustively transcribed.
- Historical IDs remain immutable. Semantic duplicates are mapped to a canonical feature but never deleted.
- Original inventory status remains Proposed unless the project record explicitly identifies a confirmed product direction or a not-approved/deferred decision.
- Inventory status, evaluation recommendation, and implementation state are separate dimensions.
- No Android runtime, JSON pack, Room schema, or application code is changed by this document.

## 2. Current product decisions that constrain every row

1. One independent app, two learner paths: Ali KG2 and Aboudy Grade 2, sharing learning infrastructure without forcing the same sequence or difficulty.
2. Macmillan is the primary curriculum backbone for the nominated books. Sudan/SMILE are secondary local-alignment evidence; Cambridge/QKLG/ELOF are benchmarks only.
3. Aboudy Grade 2 Science is a Core AAS2 subject by product decision. This is not a claim that Science is compulsory in every Sudanese Grade 2 school.
4. Offline-first is a core product requirement.
5. Child activities are question-first: no explanatory paragraph before the question by default, and no Arabic language inside child activities by default. Any exception requires a specific evidenced need and a new approval.
6. Parent-facing UI localization is distinct from learning language. Device locale must not silently translate an English learning activity.
7. Audio is optional except where listening itself is the assessed skill; the question must not become unsolvable merely because audio is unavailable.
8. All prompts, options, explanations, visuals and audio must be original or individually rights-cleared. Macmillan, SMILE and Sudanese textbook records remain REFERENCE_ONLY unless a separate licence is verified.
9. No feature classification in this document authorizes Android implementation, schema migration, release, or merge to main.

## 3. Unified Feature Evaluation + Feature-to-Module Matrix

Priority labels: **Core MVP**, **Phase 2**, **Optional**, **Deferred**, **Rejected as standalone / merged**. These are recommendations, not a new user approval.

| ID | Canonical feature / preserved intent | Learner path | Owning module(s) | Recommended scope | Status / disposition | Acceptance evidence |
|---|---|---|---|---|---|---|
| EDU-001 | Independent learner profile and isolated progress | Both | Learner Profiles; Progress Database | Core MVP | Proposed; canonical profile feature | Switching Ali/Aboudy never exposes the other learner's attempts, recommendations or mastery |
| EDU-002 | Light diagnostic starting point | Both | Assessment Engine; Curriculum Engine | Core MVP, minimal | Proposed | Short, non-exam diagnostic identifies a provisional starting point and can be overridden by parent |
| EDU-003 | Skill map and prerequisite graph | Both | Curriculum Engine; Content Engine | Core MVP; prerequisite | Proposed | Every released activity maps to a stable Skill ID; prerequisites and evidence state are inspectable |
| EDU-004 | Separate KG2 and Grade 2 learning sequences | Both | Curriculum Engine; Adaptive Engine | Core MVP | Proposed | Each path follows its own verified skill sequence and does not inherit the other path's level |
| EDU-005 | Learning cycle: diagnose, micro-teach when needed, guided, independent, transfer, review | Both | Learning Engine; Activity Renderer; Assessment Engine | Core MVP, compact | Proposed; question-first constraint | The first child-facing step is a diagnostic question; extra teaching appears only when evidence indicates need |
| EDU-006 | Multi-evidence mastery model | Both | Adaptive Engine; Assessment Engine; Progress Database | Core MVP, rules-based v1 | Proposed | One correct answer never sets Secure; independent varied evidence across sessions and delayed retrieval are required |
| EDU-007 | Error-pattern / misconception analysis | Both | Assessment Engine; Adaptive Engine | Core MVP, limited taxonomy | Proposed | Stored misconception code is tied to a specific item/skill and is not presented as a diagnosis of the child |
| EDU-008 | Progressive hints / scaffolding | Both | Feedback Engine; Adaptive Engine | Phase 2 or narrowly enabled per skill | Proposed | Hint does not reveal the key; hint exposure is recorded and assisted success is excluded from independent mastery evidence |
| EDU-009 | Immediate constructive feedback | Both | Feedback Engine; Activity Renderer | Core MVP | Proposed | Short, specific feedback appears after the response in the same task context; no long pre-question explanation |
| EDU-010 | Spaced skill-priority review | Both | Adaptive Engine; Progress Database | Core MVP, simple event-based scheduler | Proposed | Review selection is based on skill evidence, recency and prerequisite importance, not a fixed last-N-errors rule or streak |
| EDU-011 | More than one activity form for a skill | Both | Activity Renderer; Content Engine | Core MVP, small set | Proposed | A skill can be checked with at least two suitable forms when needed; form change preserves the same learning target |
| EDU-012 | Concrete, pictorial and symbolic math representations | Both | Activity Renderer; Content Engine | Core MVP | Proposed | Representation is appropriate to the target skill and does not reveal the answer through layout or cues |
| EDU-013 | Structured literacy / phonics pathway | Ali KG2; Aboudy Grade 2 | Curriculum Engine; Content Engine; Activity Renderer | Core MVP | Proposed; book evidence varies by path | KG2 and Grade 2 sequences remain distinct; each published phonics skill has source evidence and a fresh assessment item |
| EDU-014 | Scientific observation and inquiry activities | Aboudy Grade 2 | Content Engine; Activity Renderer; Assessment Engine | Core MVP, narrow first slice; expand in Phase 2 | Proposed; Science is Core AAS2 scope | First science slice maps to a verified Macmillan topic and safe observable outcome; practical safety rules are tested |
| EDU-015 | Separate parent progress dashboard | Parent | Parent Dashboard; Progress Database | Core MVP, minimal | Proposed | Parent sees each learner's secure/emerging/review-needed skills without comparison or streak pressure |
| EDU-016 | Diagnostic evidence and review record | Both; Parent view | Progress Database; Assessment Engine; Parent Dashboard | Core MVP, parent-facing summary | Proposed; canonical evidence store | Attempts retain learner, skill, item version, response, support level and timestamp; child is not labelled by errors |
| EDU-017 | Core learning works offline | Both | Offline/Data; Content & Media Manager; Progress Database | Core MVP, release blocker | Proposed inventory status; offline-first is confirmed product direction | Airplane-mode session loads approved content/assets, records progress and restores it after process restart |
| EDU-018 | Provenance and rights record for every content/media item | Both | Content Engine; Media Manager; Governance | Core MVP, release blocker | Proposed inventory status; rights governance is required | Validator rejects an item with missing source, rights state or required attribution; no protected book asset is copied |
| EDU-019 | Parent control of sound and parent-facing language/settings | Parent | Parent Dashboard; Settings | Core MVP, minimal | Proposed | Parent settings persist; child activity language remains independent of device UI locale |
| EDU-020 | Parent Gate for sensitive settings and external navigation | Parent; both paths protected | Safety; Parent Dashboard | Core MVP, release blocker | Proposed | Child cannot change parent settings or open an external link without passing the parent gate |
| EDU-021 | Accessibility: readable text, contrast, scalable text and child-sized touch targets | Both | UX; Accessibility; Activity Renderer | Core MVP, release blocker | Proposed | Core tasks remain operable without colour-only cues and without precision taps; target-device checks pass |
| EDU-022 | Optional real-world home activity linked to a skill | Both; Parent | Parent Support; Content Engine | Optional / later | Proposed | Parent activity is short, safe, optional and linked to a specific skill; no completion pressure |
| EDU-023 | Automatic question audio playback | Listening skills only | Media Manager; Activity Renderer | Optional / conditional | Proposed; no global autoplay | Autoplay occurs only for a listening task when parent setting and task design allow it; never blocks visual response |
| EDU-024 | Listening instructions before audio stimulus and response | Listening skills only | Activity Renderer; Media Manager | Core acceptance rule when listening is in scope | Proposed | Test order is instruction → stimulus → response; instructions never appear after the response |
| EDU-025 | Play / pause / replay controls | Listening skills only | Media Manager; Activity Renderer | Core where audio exists; otherwise Phase 2 | Proposed | Controls operate independently from answer state; replay does not create duplicate submissions |
| EDU-026 | Real answer interaction, not decorative controls | Both | Activity Renderer; Assessment Engine | Core MVP | Proposed | Every visible answer control accepts input, validates it and records exactly one response |
| EDU-027 | Immediate correction / feedback | Both | Feedback Engine | Rejected as standalone; merge into EDU-009 | Proposed historical ID retained | EDU-009 acceptance tests cover this ID; no duplicate feedback pipeline is created |
| EDU-028 | Fixed two attempts before support | Both | Assessment Engine | Deferred; do not adopt as a global rule | Proposed historical request; no fixed rule | Per-skill policy is justified by guessing risk and learning objective; no universal two-attempt assumption |
| EDU-029 | Scaffold between attempts | Both | Feedback Engine; Adaptive Engine | Merge into EDU-008 | Proposed historical ID retained | Same hint-level logging and non-revealing hint tests as EDU-008 |
| EDU-030 | Spoken explanation and encouragement | Both | Feedback Engine; Media Manager | Defer explanation; minimal text feedback in MVP | Proposed; no default spoken explanation | Encouragement is brief and non-coercive; no spoken explanation is required to solve an ordinary task |
| EDU-031 | Error Notebook | Both; Parent | Progress Database; Parent Dashboard | Merge into EDU-016 | Proposed historical ID retained | Evidence is stored privately and summarized by skill; no punitive child-facing error notebook |
| EDU-032 | Review the last 10 errors | Both | Adaptive Engine; Progress Database | Reject fixed last-10 rule; merge into EDU-010/016 | Proposed historical ID retained | Review queue is skill-priority based; no hard-coded last-10 chronological list |
| EDU-033 | 14-day progress trend | Parent | Parent Dashboard; Analytics | Optional / Phase 2 | Proposed | If enabled later, it reports trends without streak pressure or daily compliance targets |
| EDU-034 | SVG educational drawings | Both | Media Manager; Content Engine | Quality criterion, not a format mandate | Proposed historical ID retained | Any image format passes only if clear, lightweight, scalable as needed, rights-cleared and not answer-revealing |
| EDU-035 | Interactive number line | Both, where skill requires | Activity Renderer; Math Activity Toolkit | Phase 2 template; promote only with skill evidence | Proposed | Number-line task is mapped to a number/operations Skill ID and tests position/order rather than decoration |
| EDU-036 | Ten Frame | Ali KG2 primarily; Aboudy only if justified | Activity Renderer; Math Activity Toolkit | Candidate for Ali; verify exact taught range | Proposed | Quantity representation is mapped to counting-to-five or a later verified number skill; no range assumed |
| EDU-037 | Place Value activity | Aboudy Grade 2 | Activity Renderer; Math Activity Toolkit | Conditional / pending 2A scope | Proposed | Activate only after family book/scope confirms number range and prerequisite sequence |
| EDU-038 | Separate progress files per child | Both | Learner Profiles; Progress Database | Merge into EDU-001 | Proposed historical ID retained | EDU-001 isolation tests cover the requirement; no parallel profile store is created |
| EDU-039 | Personal greeting (e.g. learner name) | Both | Motivation Engine; Learner Profiles | Optional / defer | Proposed | If used later, it is brief, configurable and never delays the first question |
| EDU-040 | Bundled offline audio | Both; listening skills | Media Manager; Offline/Data | Core only for approved listening content; otherwise later | Proposed | Required audio assets are local, rights-cleared, integrity-checked and non-fatal when a nonessential asset fails |
| EDU-041 | Online neural voice, download and fallback | Both | Remote Media / Audio | Deferred; not approved for MVP | Explicitly not approved | No network voice call or remote audio download is present in MVP; any later proposal requires privacy, cost, rights and consent review |
| EDU-042 | Separate Arabic/English voice selection | Parent | Parent Settings; Media Manager | Deferred | Proposed; no current need established | No child-facing Arabic voice control is added by default; any later need is evidenced and separately approved |
| EDU-043 | Do not rely on audio alone to understand/solve a task | Both | Accessibility; Activity Renderer; Assessment Engine | Core non-functional acceptance rule | Proposed inventory status; product constraint retained | Non-listening tasks remain solvable with visual/text interaction when audio is disabled or unavailable |

## 4. Feature-to-Skill Matrix

The table below links feature IDs to the current canonical Skill Registry. A broad link means the feature operates across the listed skill family; it does not authorize a skill whose evidence status is pending.

| Feature IDs | Canonical skill links | Mapping rule / evidence limit |
|---|---|---|
| EDU-001, EDU-015, EDU-016, EDU-033, EDU-038 | All skills in the selected learner's path | Learner-scoped storage and reporting only; no cross-learner aggregation |
| EDU-002, EDU-006, EDU-007, EDU-010, EDU-028, EDU-031, EDU-032 | All assessed skills; especially skills with explicit prerequisites and misconception codes | Assessment policy must distinguish diagnostic, guided, independent, transfer and delayed review evidence |
| EDU-003, EDU-004, EDU-005 | Every stable Skill ID in docs/skill-registry-v0.1.md | Curriculum graph controls path and prerequisites; a pending source remains pending |
| EDU-008, EDU-029 | Skills whose registry defines a safe, non-revealing scaffold | Do not create a generic hint that gives away the answer |
| EDU-009, EDU-027, EDU-030 | Every activity with authored feedback; spoken feedback remains deferred | Feedback is linked to the item/skill and remains short, age-appropriate and non-punitive |
| EDU-011, EDU-026 | All skills with more than one valid evidence mode | Use a different item form to test transfer, not a repeated picture/position |
| EDU-012, EDU-035, EDU-036, EDU-037 | Ali: counting-to-five, ALI-MATH-NUM-*, ALI-MATH-OPS-*, common-shapes; Aboudy: place-value-to-1000, addition-within-100, ABO-MATH-*, measure-length-centimeters | Specific tool is activated only when the skill's source and number range support it |
| EDU-013 | Ali: ALI-ENG-LIST-01, ALI-ENG-VOC-01, initial-sounds, ALI-ENG-PHON-*, ALI-ENG-READ-01, ALI-ENG-LIT-01, ALI-ENG-WRITE-01; Aboudy: ABO-ENG-* | Keep Ali KG2 and Aboudy Grade 2 sequences separate; Ali's exact family-book scope remains catalogue-level |
| EDU-014 | SCI-G2-PL-*, SCI-G2-AN-*, SCI-G2-SEN-*, SCI-G2-MAT-*, SCI-G2-SKY-01, SCI-G2-ENQ-* | Macmillan Science Level 2 topic scope is verified; original AAS2 content still requires authoring/review |
| EDU-017, EDU-018, EDU-020, EDU-021, EDU-034, EDU-040, EDU-043 | All skills/activities that use offline content, media or child interaction | Non-functional quality and rights rules; they do not create new learning outcomes |
| EDU-019, EDU-023, EDU-024, EDU-025, EDU-030, EDU-041, EDU-042 | Listening skills: ALI-ENG-LIST-01, ABO-ENG-LIST-01, ABO-ENG-LIST-02; parent controls apply across both paths | Audio features are activated only for listening objectives; device locale is not the learning language |
| EDU-022 | Any skill with a safe, observable real-world transfer task; science inquiry SCI-G2-ENQ-* where appropriate | Optional parent support; no new core skill is implied |
| EDU-039 | No direct learning Skill ID | Motivation-only enhancement; cannot be counted as learning evidence |

## 5. Module ownership summary

| Module | Primary responsibility | Feature IDs |
|---|---|---|
| Learner Profiles | Independent child identity and path selection | EDU-001, EDU-038 |
| Curriculum Engine | Stable skills, sources, prerequisites and path order | EDU-003, EDU-004, EDU-013 |
| Content Engine | Original content, skill/item metadata and rights references | EDU-003, EDU-011, EDU-013, EDU-014, EDU-018, EDU-022 |
| Learning Engine | Question-first learning sequence | EDU-005 |
| Activity Renderer | Question, stimulus, answer interaction and supported activity forms | EDU-011, EDU-012, EDU-024, EDU-026, EDU-034, EDU-035, EDU-036, EDU-037, EDU-043 |
| Adaptive Engine | Mastery evidence, scaffolding and skill-priority review | EDU-006, EDU-007, EDU-008, EDU-010, EDU-028, EDU-029, EDU-032 |
| Assessment Engine | Diagnostics, response validity and misconception evidence | EDU-002, EDU-006, EDU-007, EDU-016, EDU-026 |
| Feedback Engine | Immediate feedback and controlled hints | EDU-008, EDU-009, EDU-027, EDU-029, EDU-030 |
| Media Manager | Offline assets, audio controls, integrity and fallback | EDU-023, EDU-025, EDU-034, EDU-040, EDU-041, EDU-042 |
| Progress Database | Learner-scoped attempt and mastery evidence | EDU-001, EDU-006, EDU-010, EDU-016, EDU-031, EDU-032, EDU-033, EDU-038 |
| Parent Dashboard / Settings | Parent-facing progress, controls and gate | EDU-015, EDU-019, EDU-020, EDU-022, EDU-033, EDU-039, EDU-042 |
| Offline/Data | Local content and progress operation | EDU-017, EDU-040 |
| Safety / Accessibility | Child protection, usable controls and non-audio dependency | EDU-020, EDU-021, EDU-043 |

## 6. Consolidated MVP boundary recommendation

### Core MVP candidates

- EDU-001–007, EDU-009–021, EDU-024, EDU-026, EDU-043.
- EDU-025 and EDU-040 only when an approved listening skill requires audio.
- EDU-014 starts with a narrow, safe Science slice because Science is Core AAS2 scope; it does not require all 18 topics to ship at once.
- EDU-034 is an asset-quality criterion, not a requirement to use SVG.
- EDU-024 is an acceptance rule for listening tasks, not a general pre-question explanation.

### Phase 2 / conditional

- EDU-008/029 advanced scaffolding; EDU-011 broader activity variety; EDU-014 broader inquiry activities; EDU-025/040 expanded audio library; EDU-035 number line; EDU-036 Ten Frame beyond currently evidenced KG2 range; EDU-037 place value after 2A evidence; EDU-033 progress trends; writing/handwriting interactions beyond basic visual support.

### Optional / later

- EDU-022 home activities; EDU-039 greeting; EDU-033 extended reports; richer motivation/animation; optional parent tools.

### Deferred / not approved

- EDU-023 global autoplay; EDU-028 fixed two-attempt rule; EDU-030 spoken explanation; EDU-041 online neural voice; EDU-042 bilingual voice selector; Arabic helper/translation inside child activities unless new evidence and approval.

### Merged historical IDs

- EDU-027 → EDU-009.
- EDU-029 → EDU-008.
- EDU-031 → EDU-016.
- EDU-032 → EDU-010 + EDU-016.
- EDU-038 → EDU-001.
- EDU-035/036/037 remain subordinate activity templates under EDU-012.
- EDU-034 remains a visual quality criterion, not a mandated file format.

## 7. Remaining decisions and blockers

1. Confirm exact covers/ISBNs and contents/indexes of the five nominated family book paths. Publisher catalogue identity alone is not proof of the family's physical edition.
2. Obtain the current ISBN-matched Macmillan Mathematics 2A Scope and Sequence; the catalogue ISBN is known but detailed sequence is not.
3. Obtain official current Sudan KG2 and Grade 2 curriculum documents if available; current public portal snapshots do not establish a complete national syllabus.
4. Confirm which Cambridge Early Years English pathway (FLE or ESL) is relevant before detailed benchmark mapping.
5. Approve the final MVP feature set and measurable mastery policy only after source closure and child usability testing.
6. Keep the Android technical choice, schema v4 contract, Room migration and implementation plan behind a separate explicit approval gate.

## 8. Verification checklist for this artifact

- [x] All existing EDU-001–EDU-043 IDs retained.
- [x] No new feature IDs invented.
- [x] Historical duplicates mapped without deleting their intent.
- [x] Each feature has module ownership, path, priority/disposition and acceptance evidence.
- [x] Feature-to-Skill links use existing registry IDs and explicitly retain evidence limits.
- [x] Arabic/default explanation decision in section 20 is applied.
- [x] No runtime, JSON pack, Room schema, APK, release or main-branch change is included.


## Implementation state — Slice 01 (23 November 2026)

The following implementation evidence supplements, but does not rewrite, the proposed priority/disposition rows above:

| Feature IDs | Current implementation state | Evidence / remaining gate |
|---|---|---|
| EDU-001, EDU-004 | Existing learner isolation and separate path-to-pack selection retained | JVM learner-isolation tests; real-device validation remains |
| EDU-005 | First unpractised question opens immediately; return-to-catalogue navigation remains available | JVM selection-policy tests; broader diagnostic/adaptive cycle remains partial |
| EDU-009, EDU-026 | Objective single-choice submission, persisted feedback, neutral retry action | JVM service test confirms separate attempt records; more activity types remain out of scope |
| EDU-015, EDU-016 | Read-only parent progress dashboard with overall and per-skill evidence | Requires Android device credential; UI and lifecycle still need device testing |
| EDU-017 | Local bundled-pack loading and learner-scoped Room persistence remain offline-first | Source path and JVM tests; airplane-mode and process-restart device tests remain |
| EDU-020 | Parent dashboard is protected by Android system device credential and closes on app background | CI source compile; real-device credential and lifecycle tests remain |
| EDU-019 | Parent/app chrome is localized independently of English learning content | Runtime policy tests; full parent settings are not yet implemented |

No feature row is marked fully production-verified solely because source CI passed. CI does not prove real-device usability, full curriculum coverage, or production readiness.
