# AAS2 MVP Proposal v0.2 — Draft for Product Review

**Status:** Draft proposal; not approved for implementation.  
**Review date:** 23 November 2026  
**Project:** AAS2-NEW — Ali & Aboudy Scholars 2  
**Repository:** asimalimikial-rgb/Ali-and-Aboudy-Scholars-2  
**Active branch:** foundation/slice-0-android

## 1. Product definition

A single offline-first Android learning product with two independent learner pathways and a shared learning engine:

- **Ali:** KG2 / Senior KG, approximately 5–6 years.
- **Aboudy:** Grade 2, approximately 7–8 years.

The product must personalize the sequence and evidence for each learner without merging their curriculum, attempts, progress or recommendations.

**Curriculum hierarchy:** Macmillan is the primary nominated book backbone; Sudan/SMILE are secondary local-alignment references; QKLG/ELOF/Cambridge are developmental or international benchmarks only.

**Subject scope:**
- Ali: English/Early Literacy and Mathematics; General Awareness/Discovery remains cross-curricular and is not promoted to a separate core Science syllabus without a product decision.
- Aboudy: English, Mathematics and Science. Science is Core AAS2 scope by explicit product decision, not a claim of compulsory national Grade 2 status.

## 2. Confirmed product constraints

- Offline-first for core learning, content and progress.
- Separate learner profiles and path-specific progress.
- Question-first child experience; no long explanation before the question.
- No Arabic language inside child activities by default. Parent-facing localization is separate from the learning language. Any child-facing Arabic support requires a specific evidenced need and new approval.
- Audio does not gate ordinary tasks. Audio is used when listening itself is the target skill.
- No global autoplay, no universal two-attempt rule, no streak pressure, no sibling competition, no punitive error display.
- Original learning content only; protected textbook expression, questions, illustrations, audio scripts and layouts are not copied.
- Per-source and per-asset rights records are mandatory.
- No cloud account or cloud sync dependency in the initial MVP unless a separate need and approval are established.

## 3. Core MVP capability boundary

### 3.1 Learner and curriculum

- Two isolated learner profiles.
- Path-specific skill registry with stable Skill IDs, observable outcomes, prerequisites, evidence status and source references.
- Light diagnostic start, not a long exam.
- Skill sequence is evidence-graded; pending-edition skills cannot be presented as confirmed textbook coverage.
- Parent can review and adjust the starting point.

### 3.2 Child activity experience

- Question-first task screen.
- Question, stimulus and response controls kept together in one clear screen wherever possible.
- No routine vertical scrolling to reach the answer.
- Short, direct instructions only when required to operate the task.
- At least one fully working answer interaction in the initial implementation; additional activity archetypes are enabled only after the renderer and content contract support them.
- Immediate, concise, non-punitive feedback after response.
- Hints are limited and recorded; advanced adaptive scaffolding may remain Phase 2.
- Audio controls are available only for approved listening activities and follow instruction → stimulus → response order.
- Failure of a nonessential image/audio asset must not hide the question or crash the activity.

### 3.3 Learning evidence and review

- Store learner, skill, activity/content version, response, correctness, support/hint level, assessment phase and timestamp where supported by the approved data model.
- Distinguish diagnostic, guided, independent, transfer and review evidence.
- Do not infer Secure mastery from one correct answer or hint-assisted success.
- Initial mastery labels are provisional: Not assessed, Emerging, Developing, Secure (provisional), Review due.
- Start with a simple event-based review policy; exact intervals and numeric thresholds remain uncalibrated.

### 3.4 Parent experience and safety

- Minimal dashboard per learner: secure, developing and review-needed skills.
- Parent controls for sound and parent-facing UI settings.
- Parent Gate for sensitive settings and external navigation.
- No ads or external links in the child learning flow.
- No sibling leaderboard or streak-based pressure.

### 3.5 Offline and data

- Bundled, versioned and validated content packs.
- Local media manifest with integrity checks and safe fallback.
- Learner-scoped local persistence.
- Progress survives process restart and device restart.
- Cache cleanup must not erase durable learning progress.
- No analytics SDK or unnecessary network permissions in the initial MVP.

## 4. Proposed first content slice — conditional on evidence closure

This is a content planning proposal, not a production-ready pack list.

| Path | Candidate first slice | Evidence state | Gate before production |
|---|---|---|---|
| Ali KG2 English | Listening/vocabulary and early phonological awareness; preserve existing pilot Skill IDs | Catalogue/partial/pending family edition | Match Hop Skip and Jump Senior KG cover/ISBN and inspect contents/scope |
| Ali KG2 Mathematics | Counting-to-five and common shapes as current pilot anchors; extend only after number-range evidence | Current pilot exists; wider family scope catalogue-only | Match Maths Xpress Senior KG family copy and inspect contents/scope |
| Aboudy Grade 2 English | Selected English World Level 2 phonics, reading and listening outcomes | Official publisher S&S reviewed; family copy pending | Match family ISBN and select a bounded, source-mapped skill set |
| Aboudy Grade 2 Mathematics | Do not expand detailed 2A sequence yet | 2A ISBN known; matching S&S not verified; 2B family copy pending | Obtain ISBN-matched 2A S&S and inspect 2A/2B family books |
| Aboudy Grade 2 Science | A narrow original slice from a verified Level 2 topic, with safety review | Official 18-topic S&S reviewed; current U01/U02 are draft authoring documents | Independent science/pedagogy/language review and family-copy check |

The existing Science Unit 1 revised set (32 items) and Unit 2 draft (30 candidates) are not production-approved. They must pass independent review before being counted as MVP content.

## 5. Module responsibilities — logical, not final technical architecture

| Logical module | Responsibility |
|---|---|
| Learner Profiles | Learner identity and path isolation |
| Curriculum Engine | Skills, prerequisites, sequence and source evidence |
| Content Engine | Original activities and content metadata |
| Activity Renderer | Question, stimulus and response presentation |
| Learning Engine | Diagnostic-to-practice-to-review session flow |
| Assessment Engine | Response validation and evidence capture |
| Adaptive Engine | Provisional mastery and skill-priority review |
| Feedback Engine | Short feedback and controlled hints |
| Media Manager | Offline images/audio, integrity and fallback |
| Progress Database | Learner-scoped attempts and progress |
| Parent Dashboard | Progress summaries and parent controls |
| Safety / Accessibility | Parent Gate, privacy, touch targets, contrast and readable text |

This is a logical product architecture only. It does not choose Native Android versus another technology, nor does it approve schema v4 or Room changes.

## 6. Acceptance gates

The MVP cannot be considered ready until all applicable gates pass:

1. **Profile isolation:** no cross-learner leakage in attempts, progress, settings or recommendations.
2. **Curriculum traceability:** every production activity has a Skill ID, source evidence and review status.
3. **Question-first UX:** question appears first; no long preamble; ordinary tasks are one-screen where device size allows.
4. **Interaction correctness:** every visible control works; repeated taps cannot duplicate or skip submissions.
5. **Feedback integrity:** feedback is concise, specific and non-punitive; hints do not reveal answers.
6. **Assessment integrity:** assisted and independent evidence remain distinct; one item cannot establish mastery.
7. **Listening sequence:** instructions precede stimulus and response; audio can be replayed without changing answer state.
8. **Offline:** approved content and required assets work in airplane mode; progress persists after process death/restart.
9. **Data safety:** cache cleanup does not erase progress; nonessential media failure is non-fatal.
10. **Parent safety:** child cannot change parent settings or open external navigation.
11. **Accessibility/device fit:** touch, text, contrast and layout pass on the actual target device(s).
12. **Content rights:** every item and asset has a verified provenance and rights state.

## 7. Test strategy

- JVM unit tests for evaluation, learner isolation, progress policy and content validation.
- Schema/content contract tests for stable IDs, source references, rights fields, answer integrity and supported activity types.
- UI tests for question-first, one-screen interaction, duplicate-tap prevention and transitions.
- Offline tests in airplane mode, including media fallback.
- Recovery tests after process death and device restart.
- Accessibility checks for touch targets, text scaling, contrast and colour-independent cues.
- Supervised usability sessions with both children only after an approved prototype exists; collect minimal, non-identifying observations and do not use speed or sibling comparison as success measures.
- Distinguish JVM CI, emulator/instrumentation, and real-device QA. A green JVM CI run is not proof of emulator or device readiness.

## 8. Evidence gaps that prevent final approval

- Family edition/cover/ISBN and contents for Hop Skip and Jump Senior KG.
- Family edition/contents and detailed scope for Maths Xpress Senior KG.
- Family copy match for English World Level 2.
- Current ISBN-matched Macmillan Mathematics 2A Scope and Sequence and family copy; 2B family edition/scope also needs confirmation.
- Macmillan Science Level 2 family copy and full pupil-book inspection.
- Current official Sudan KG2/pre-primary framework.
- Current official Sudan Grade 2 framework; the public Learning Passport subject list is a platform snapshot, not the full national syllabus.
- Confirmation of the learner's English learning pathway for any Cambridge benchmark comparison.
- Actual target Android device, supported OS range, storage budget and backup expectations.
- Calibrated item difficulty, distractor quality, mastery thresholds and review intervals.

## 9. Explicitly outside this proposal

- Online neural voice, remote audio downloads or network fallback.
- Default Arabic translation/helper inside child activities.
- Long explanation before the question.
- Universal two-attempt policy or last-10-errors review rule.
- Mandatory SVG-only artwork.
- Advanced analytics, streaks, sibling rankings or cloud synchronization.
- Copying protected textbook questions, stories, illustrations, audio or page layouts.
- Any unreviewed schema migration, Room migration, Android runtime change, APK build, release or merge to main.

## 10. Approval gate

This document is a **Draft MVP Proposal v0.2**. It may be reviewed and corrected, but it is not a request to begin coding.

Before implementation:
1. Close or explicitly accept the outstanding book and local-curriculum evidence gaps.
2. Complete independent review of draft Science content.
3. Approve the final Core MVP / Phase 2 / Optional feature boundary.
4. Approve the UX prototype and activity capability list.
5. Approve the final content model, migration plan and technical architecture as separate explicit decisions.

Until those approvals are recorded, the project remains in planning/specification and documentation work. No implementation, APK, release or main-branch merge is authorized by this proposal.
