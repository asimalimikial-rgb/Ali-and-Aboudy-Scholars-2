# AAS2 Skill Registry v0.1 — Draft

**Status:** Working registry; not a final learner-placement assessment or approved mastery-threshold specification.  
**Paths:** Ali KG2 and Aboudy Grade 2.  
**Curriculum hierarchy:** Macmillan primary; SMILE and Sudanese Ministry/NCCER references secondary for local alignment; Cambridge/QKLG/ELOF are developmental or international benchmarks where stated.  
**Implementation boundary:** This registry is a curriculum/assessment specification. The current Android model stores only basic skill fields; prerequisites, misconception codes, diagnostic evidence, mastery rules and review schedules are not yet persisted by the app.

## 1. Registry contract

Every skill has a permanent ID independent of question wording or activity type. A skill record must keep these concepts separate:

- **Curriculum alignment:** what the selected source indicates should be learned.
- **Observable outcome:** what the child can actually do.
- **Evidence status:** how directly the source supports the skill.
- **Learner evidence:** what this child has demonstrated in attempts.
- **Mastery judgement:** an evidence-based state, not a single correct answer.

### Evidence statuses

| Status | Meaning |
|---|---|
| `CONFIRMED_SCOPE` | The selected publisher Scope and Sequence directly lists the topic or outcome. |
| `PARTIAL_SCOPE` | Only some elements or a component-level description are supported. |
| `CATALOGUE_ONLY` | A catalogue/product description supports the broad domain or pedagogy, not detailed outcomes. |
| `PENDING_EDITION` | The skill is plausible, but the exact family edition or matching Scope and Sequence is unverified. |
| `BENCHMARK_ONLY` | Supported by an external developmental/framework benchmark, not established as a family-book outcome. |
| `AAS2_PRODUCT_SCOPE` | Included by an explicit AAS2 product decision, independent of claims about a national syllabus. |

### Activity and assessment rules

- Select the activity archetype that measures the target skill; do not force every skill into multiple choice.
- A diagnostic item is given before teaching or hinting; guided practice follows instruction; independent practice uses a fresh item; transfer changes the context while preserving the target skill.
- Record whether an answer was independent or hint-assisted. A supported answer must not be counted as independent mastery evidence.
- Do not infer mastery from one item, one repeated picture, or a single session.
- Provisional mastery evidence requires repeated independent demonstrations on different item forms across more than one session, followed by delayed retrieval. Numeric cut-offs remain uncalibrated and are intentionally not fixed here.
- Review timing is event-based for now: initial retrieval in a later session, then delayed retrieval after a longer gap. Exact intervals must be calibrated from actual item quality and learner evidence.
- Error labels below are instructional misconceptions, not diagnoses of the child.

## 2. Ali — KG2

### 2.1 English and early literacy

The family's nominated reference is Hop Skip and Jump Senior KG. The available Macmillan India catalogue confirms literacy sheets, a phonics book, story/rhyme books and handwriting components, but does not establish the exact phoneme sequence or full learning outcomes of the family's edition. Skill-level outcomes below are therefore provisional and must not be represented as page-by-page book coverage.

| Skill ID | Observable outcome | Prerequisite | Evidence | Common error to diagnose | Suitable assessment pattern |
|---|---|---|---|---|---|
| `ALI-ENG-LIST-01` | Follows a familiar one-step spoken instruction in English | None | `CATALOGUE_ONLY` / `BENCHMARK_ONLY` | Responds to a visual cue rather than the spoken instruction | Audio-first response; adult-observed task |
| `ALI-ENG-VOC-01` | Identifies a familiar spoken word by selecting its matching picture | LIST-01 | `PARTIAL_SCOPE` | Selects by colour/position rather than word meaning | Listen-and-select; picture matching |
| `initial-sounds` | Identifies the initial sound in a familiar English word | Listening to the word; familiar vocabulary | `PARTIAL_SCOPE`; existing pilot skill ID retained as the permanent ID | Chooses a letter name or a later sound instead of the first phoneme | Audio-first sound discrimination |
| `ALI-ENG-PHON-02` | Connects a taught letter to its sound when the letter is shown | `initial-sounds` | `PENDING_EDITION` | Names the letter but does not produce/recognise its sound | Letter-sound selection; oral response |
| `ALI-ENG-PHON-03` | Blends a short, taught sequence of phonemes into a simple word when ready | `initial-sounds`, ALI-ENG-PHON-02 | `PENDING_EDITION` | Says letter names, omits a phoneme, or guesses from the picture | Listen/blend; build-a-word interaction |
| `ALI-ENG-READ-01` | Matches a familiar decodable word to its meaning or picture | ALI-ENG-PHON-02; ALI-ENG-PHON-03 where decoding is required | `PENDING_EDITION` | Uses picture guessing without attending to print | Word-picture match; simple decoding check |
| `ALI-ENG-LIT-01` | Identifies a familiar character or key event in a short picture-supported story | LIST-01, VOC-01 | `PARTIAL_SCOPE` | Confuses a picture detail with the story event | Listen/read then select; event ordering |
| `ALI-ENG-WRITE-01` | Recognises a target letter shape and follows its visual formation path | Visual discrimination | `PARTIAL_SCOPE`; catalogue lists handwriting component | Reverses orientation or traces without attending to start/direction | Trace/point task plus offline pencil practice |

**Language-path note:** Cambridge Early Years EY3 separates First Language and Second Language pathways. The correct comparison route is not inferred from age alone. The AAS2 learner activity remains question-first; any Arabic helper is a separate product-language feature, not a replacement for English learning content.

### 2.2 Mathematics and early numeracy

The family's nominated reference is Maths Xpress Senior KG. The available catalogue describes mental maths, oral maths talk, puzzles/games and real-life learning, but no detailed Senior KG Scope and Sequence or number range has been verified. The current pilot contains `counting-to-five` and `common-shapes`; preserve these IDs when refining the same skills.

| Skill ID | Observable outcome | Prerequisite | Evidence | Common error to diagnose | Suitable assessment pattern |
|---|---|---|---|---|---|
| `counting-to-five` | Counts a set of up to five objects using one-to-one correspondence | None | `CATALOGUE_ONLY` plus current pilot implementation | Skips an object or counts one object twice | Count-and-select; touch/point counting |
| `ALI-MATH-NUM-02` | Recognises numerals in the verified taught range | Counting-to-five | `PENDING_EDITION` for range beyond current pilot | Confuses numeral shape or reverses order | Numeral-to-quantity match |
| `ALI-MATH-NUM-03` | Compares two small sets using more, fewer or the same | Counting-to-five | `PENDING_EDITION` | Chooses the visually spread-out set rather than counting | Compare picture sets |
| `ALI-MATH-OPS-01` | Models joining or separating small groups with objects or pictures | Counting-to-five; compare sets | `PENDING_EDITION` | Counts only the added group or loses track of the starting quantity | Manipulative/picture model; oral explanation |
| `common-shapes` | Recognises common two-dimensional shapes by visible attributes | Visual discrimination | `CATALOGUE_ONLY` plus current pilot implementation | Names by orientation/colour rather than sides or curved boundary | Shape identification; rotate-and-recognise |
| `ALI-MATH-PAT-01` | Sorts familiar objects by one stated attribute | Vocabulary for the attribute | `PENDING_EDITION` | Changes sorting rule midway or uses two attributes unintentionally | Sorting/classification |
| `ALI-MATH-PAT-02` | Continues a simple repeating pattern | PAT-01 | `PENDING_EDITION` | Copies the last item rather than identifying the repeating unit | Pattern completion/building |
| `ALI-MATH-MEAS-01` | Compares two familiar objects by a directly observable attribute such as longer/shorter or heavier/lighter | Comparison language | `PENDING_EDITION` | Judges by appearance or position rather than the measured attribute | Direct comparison; simple practical task |

### 2.3 General awareness / discovery

Hop Skip and Jump's catalogue lists General Awareness and SEL/STEM/Creative Coding components. These are broad component signals, not a verified standalone AAS2 Science syllabus for Ali. Keep discovery activities short and cross-curricular; do not create a separate core Science subject for Ali without a product decision and evidence map.

| Skill ID | Observable outcome | Evidence | Status |
|---|---|---|---|
| `plant-needs` | Recognises that plants need water and light to grow | Basic observation | Current KG2 pilot skill; QKLG developmental reference and NOAA public-domain topic reference |
| `ALI-DISC-OBS-01` | Notices and names one visible feature of a familiar object or living thing | Catalogue component only | `CATALOGUE_ONLY` |
| `ALI-DISC-SEQ-01` | Orders a short familiar routine or simple event sequence | STEM/creative-learning description only | `CATALOGUE_ONLY` |

These are exploratory candidates, not a claim that the exact family book teaches each outcome.

## 3. Aboudy — Grade 2

### 3.1 English — Macmillan English World Level 2

The official publisher Scope and Sequence supports a broad multi-skill map. The actual family edition/ISBN still requires confirmation. Sudan SMILE Starter 2 is secondary local alignment, not the sequence-setting curriculum.

| Skill ID | Observable outcome | Prerequisite | Evidence | Common error to diagnose | Suitable assessment pattern |
|---|---|---|---|---|---|
| `english-context-vocabulary` | Selects a familiar English word that completes a simple context sentence | Basic word recognition | `PARTIAL_SCOPE`; current pilot ID | Chooses a thematically related word that does not fit the sentence meaning | Context cloze; image-supported choice |
| `english-sentence-order` | Recognises a correctly ordered simple English sentence | Word recognition; basic sentence pattern | `PARTIAL_SCOPE`; current pilot ID | Places a verb or subject in an unsuitable position | Sentence ordering/building |
| `ABO-ENG-READ-01` | Retrieves an explicitly stated detail from a short age-appropriate text | Decoding/vocabulary appropriate to text | `CONFIRMED_SCOPE` | Selects a plausible but unstated detail | Read-and-find evidence |
| `ABO-ENG-READ-02` | Orders events from a short story or information sequence | READ-01 | `CONFIRMED_SCOPE` | Orders by personal plausibility rather than text sequence | Event sequencing |
| `ABO-ENG-READ-03` | Identifies a main idea and one supporting detail in a short text | READ-01 | `PARTIAL_SCOPE` | Treats an interesting detail as the main idea | Main-idea/detail selection |
| `ABO-ENG-READ-04` | Makes a simple prediction or inference using a textual or visual clue | READ-01; vocabulary | `PARTIAL_SCOPE` | Guesses without linking to a clue | Evidence-linked choice/explanation |
| `ABO-ENG-PHON-01` | Reads and spells taught vowel patterns, blends and digraphs in familiar words | Letter-sound knowledge | `CONFIRMED_SCOPE` for listed patterns | Confuses sound pattern with letter name or applies a pattern to an unfamiliar word | Decode, sort and spell |
| `ABO-ENG-LIST-01` | Identifies the gist or a stated detail from a short spoken text | Familiar vocabulary | `CONFIRMED_SCOPE` | Uses a picture guess without listening for the detail | Audio-first response |
| `ABO-ENG-LIST-02` | Sequences events or instructions heard in a short recording | LIST-01 | `CONFIRMED_SCOPE` | Remembers isolated words but loses order | Listen-and-sequence |
| `ABO-ENG-GRAM-01` | Uses a taught tense or question form appropriately in context | Basic sentence structure | `CONFIRMED_SCOPE` for listed syllabus items | Chooses by memorised keyword while ignoring subject/time context | Contextual sentence choice/build |
| `ABO-ENG-WRITE-01` | Writes a clear simple sentence with appropriate word order and basic punctuation | Sentence-order skill; spelling support | `PARTIAL_SCOPE` | Omits a subject/verb or punctuation | Sentence construction and edit |
| `ABO-ENG-WRITE-02` | Produces a short description or simple text for a stated purpose | WRITE-01; relevant vocabulary | `PARTIAL_SCOPE` | Lists words without connected meaning | Guided writing; short rubric |
| `ABO-ENG-SPEAK-01` | Describes a familiar picture, person or event in short connected language | Vocabulary; sentence patterns | `PARTIAL_SCOPE` | Gives labels without a meaningful description | Oral description; adult rubric |

### 3.2 Mathematics — Macmillan Mathematics 2A/2B

The current crosswalk has unit-title and catalogue evidence, but the current 2A Scope and Sequence has not been matched to ISBN 9781035130382. Do not silently treat older teacher-book content as the current 2A edition. Existing pilot IDs are retained.

| Skill ID | Observable outcome | Prerequisite | Evidence | Common error to diagnose | Suitable assessment pattern |
|---|---|---|---|---|---|
| `place-value-to-1000` | Identifies the value of a digit in a three-digit number | Reads three-digit numerals | `PARTIAL_SCOPE`; current pilot ID and local Grade 2 alignment | Confuses face value with place value | Base-ten/picture model; numeral choice |
| `addition-within-100` | Adds two two-digit numbers within the taught range | Place value; addition facts | `PARTIAL_SCOPE`; current pilot ID | Adds digits without respecting place value | Modelled/pictorial addition; calculation |
| `ABO-MATH-OPS-02` | Subtracts within the verified taught range and explains the strategy | Place value; addition/subtraction relationship | `PENDING_EDITION` | Subtracts smaller digit from larger regardless of position | Number line/base-ten model |
| `ABO-MATH-OPS-03` | Represents equal groups or arrays as an early multiplicative model | Counting; repeated addition | `PARTIAL_SCOPE` at unit-title level | Counts rows only or columns only | Array construction; equal-group model |
| `ABO-MATH-OPS-04` | Shares a set into equal groups when division is in the verified scope | Equal groups; counting | `PENDING_EDITION` | Unequal groups or confuses number of groups with group size | Concrete sharing task |
| `ABO-MATH-NUM-02` | Represents and compares numbers in the verified 2A/2B range | Place value | `PARTIAL_SCOPE` at unit-title level | Compares digit strings rather than place values | Number line; base-ten comparison |
| `ABO-MATH-FRAC-01` | Recognises a simple fraction as equal parts of a whole where supported by the family edition | Equal partitioning | `PENDING_EDITION` | Counts shaded parts without checking equal partition | Visual partition and explain |
| `measure-length-centimeters` | Compares lengths measured in centimetres | Unit awareness; comparison | `PARTIAL_SCOPE`; current pilot ID and available unit-title evidence | Starts ruler measurement at 1 rather than 0 | Measure/compare with ruler |
| `ABO-MATH-MEAS-02` | Solves a simple measurement problem using the unit and instrument taught | MEAS-01 | `PENDING_EDITION` | Selects an operation without interpreting the quantity | Practical measure and word problem |
| `ABO-MATH-TIME-01` | Reads and uses time language/clock representations in the verified scope | Number sequence | `PARTIAL_SCOPE` at unit-title level | Confuses hour/minute or hands | Clock reading and routine sequencing |
| `ABO-MATH-MONEY-01` | Recognises and compares money values in the verified scope | Number comparison | `PARTIAL_SCOPE` at unit-title level | Compares coin count rather than total value | Price comparison and simple shop task |
| `ABO-MATH-GEO-01` | Identifies and describes flat/solid shapes using attributes in the verified scope | Shape vocabulary | `PARTIAL_SCOPE` at unit-title level | Names by orientation or colour | Sort/describe/build shapes |
| `ABO-MATH-DATA-01` | Reads or organises simple data in the representation used by the family edition | Counting and comparison | `PARTIAL_SCOPE` at unit-title level | Reads a category label as a quantity | Tally/table/graph interpretation |
| `ABO-MATH-REASON-01` | Chooses a strategy and checks whether an answer is reasonable | Relevant domain prerequisite | `PENDING_EDITION` | Gives an answer without checking units or context | Explain/check/compare strategies |

### 3.3 Science — Macmillan Science Level 2 (Core AAS2)

The publisher's official Level 2 Scope and Sequence identifies all 18 topics. The following topic-skill IDs are retained from `docs/macmillan-science-level2-curriculum-map-v0.1.md`. The 10 enquiry skills are cross-cutting and should be assessed in context as well as through explicit practice.

| Skill ID | Observable outcome | Prerequisite | Evidence | Common error to diagnose |
|---|---|---|---|---|
| `SCI-G2-PL-01` | Identifies a living plant using observable features | ENQ-01 | `CONFIRMED_SCOPE` | Confuses a wooden object or artificial plant with a living plant |
| `SCI-G2-PL-02` | Identifies roots as the part that usually takes in water from soil | PL-01 | `CONFIRMED_SCOPE` | Confuses roots with leaves or flowers |
| `SCI-G2-PL-03` | Identifies familiar food/material products that come from plants | PL-01 | `CONFIRMED_SCOPE` | Assumes all food/materials come from animals or factories |
| `SCI-G2-PL-04` | Sequences early seed germination and describes basic growth conditions accurately | ENQ-03, ENQ-06 | `CONFIRMED_SCOPE` | Believes a flower appears first or that soil is an absolute requirement for germination |
| `SCI-G2-AN-01` | Identifies an insect by its defining body features, including six legs | ENQ-01 | `CONFIRMED_SCOPE` | Counts wings as legs or calls every small animal an insect |
| `SCI-G2-AN-02` | Groups examples as vertebrates or invertebrates using the backbone criterion | ENQ-05 | `CONFIRMED_SCOPE` | Groups by habitat, size or movement instead of the stated feature |
| `SCI-G2-AN-03` | Orders stages in a familiar animal life cycle | ENQ-06 | `CONFIRMED_SCOPE` | Assumes young animals always look like smaller adults |
| `SCI-G2-AN-04` | Explains how a visible feature such as camouflage can help survival in a habitat | ENQ-01, ENQ-08 | `CONFIRMED_SCOPE` | Thinks camouflage makes an animal invisible in every setting |
| `SCI-G2-SEN-01` | Matches a sense to the relevant sensory organ and stimulus | ENQ-01 | `CONFIRMED_SCOPE` | Selects the visible object rather than the sense used |
| `SCI-G2-SEN-02` | Distinguishes loudness from pitch in simple sound comparisons | ENQ-04 | `CONFIRMED_SCOPE` | Treats high pitch as automatically loud |
| `SCI-G2-SEN-03` | Identifies eyes as the organs used for seeing colours and objects | SEN-01 | `CONFIRMED_SCOPE` | Confuses eyes with other sense organs |
| `SCI-G2-SEN-04` | Describes a surface using appropriate touch/texture vocabulary | ENQ-01 | `CONFIRMED_SCOPE` | Confuses temperature with texture |
| `SCI-G2-SEN-05` | Applies safe rules when investigating taste and smell; never tastes unknown plants/materials | ENQ-09 | `CONFIRMED_SCOPE` | Assumes a familiar-looking unknown plant is safe |
| `SCI-G2-MAT-01` | Predicts and tests whether an object floats or sinks, recording the result | ENQ-03, ENQ-07 | `CONFIRMED_SCOPE` | Assumes size alone determines floating |
| `SCI-G2-MAT-02` | Describes how a liquid takes the shape of its container | ENQ-01, ENQ-04 | `CONFIRMED_SCOPE` | Thinks a liquid has a fixed shape |
| `SCI-G2-MAT-03` | Describes a simple dissolving/mixing observation without claiming the material disappears | ENQ-03, ENQ-08 | `CONFIRMED_SCOPE` | Thinks dissolved material vanishes or becomes a new liquid |
| `SCI-G2-MAT-04` | Identifies heat hazards and chooses a safe action requiring adult help | ENQ-09 | `CONFIRMED_SCOPE` | Treats a hot object as safe to touch |
| `SCI-G2-SKY-01` | Identifies the Moon as Earth's natural satellite and distinguishes it from stars | ENQ-01 | `CONFIRMED_SCOPE` | Calls the Moon a star or confuses apparent brightness with object type |

### 3.4 Scientific enquiry — cross-cutting skills

| Skill ID | Observable outcome | Prerequisite | Evidence | Assessment evidence |
|---|---|---|---|---|
| `SCI-G2-ENQ-01` | Identifies relevant visible, audible or tactile features | None | `AAS2_PRODUCT_SCOPE` aligned to enquiry across S&S topics | Selects/records the relevant feature |
| `SCI-G2-ENQ-02` | Chooses a question that can be investigated | ENQ-01 | `AAS2_PRODUCT_SCOPE` | Selects a testable question rather than an opinion |
| `SCI-G2-ENQ-03` | Makes a simple prediction before a test | ENQ-01/02 | `AAS2_PRODUCT_SCOPE` | Prediction is recorded before observing the result |
| `SCI-G2-ENQ-04` | Compares two or more observations using a stated feature | ENQ-01 | `AAS2_PRODUCT_SCOPE` | Comparison refers to evidence |
| `SCI-G2-ENQ-05` | Groups items using a stated observable rule | ENQ-01/04 | `AAS2_PRODUCT_SCOPE` | Correct grouping and consistent rule |
| `SCI-G2-ENQ-06` | Orders stages or events logically | ENQ-01 | `AAS2_PRODUCT_SCOPE` | Correct sequence; no unsupported leap |
| `SCI-G2-ENQ-07` | Records observations in a simple picture record, tally or table | ENQ-01 | `AAS2_PRODUCT_SCOPE` | Record matches observed results |
| `SCI-G2-ENQ-08` | Gives a simple explanation linked to an observation | ENQ-04/07 | `AAS2_PRODUCT_SCOPE` | Explanation is supported by recorded evidence |
| `SCI-G2-ENQ-09` | Identifies safe actions and situations requiring adult help | None | `AAS2_PRODUCT_SCOPE` and topic-specific safety objectives | Selects safe action and identifies hazard |
| `SCI-G2-ENQ-10` | Communicates a result using age-appropriate science vocabulary | ENQ-08 | `AAS2_PRODUCT_SCOPE` | Clear oral, selected or short written explanation |

## 4. Shared assessment and mastery record

The registry defines what evidence should eventually be collected. It does not claim that schema v3 currently stores all these fields.

| Evidence field | Purpose |
|---|---|
| `skillId` | Stable skill identity |
| `activityId` + content version | Trace the exact item and version used |
| `response` + correctness | Record the child's observable response |
| `hintLevel` / `hintUsed` | Distinguish independent and supported performance |
| `misconceptionCode` | Identify the error pattern to guide the next explanation |
| `assessmentPhase` | DIAGNOSTIC / GUIDED / INDEPENDENT / TRANSFER / REVIEW |
| `evidenceType` | Selected response, oral response, practical observation, short writing, etc. |
| `feedbackSnapshot` | Preserve the explanation actually shown at the time |
| `attemptTimestamp` | Support review and retention checks |
| `learnerPath` | Keep Ali and Aboudy evidence strictly isolated |

### Mastery decision policy — provisional

1. **Not assessed:** no usable evidence exists yet.
2. **Emerging:** the child can respond with modelling, prompts or substantial hints.
3. **Developing:** the child succeeds on some independent items but evidence is inconsistent.
4. **Secure (provisional):** the child demonstrates the outcome independently on varied items across multiple sessions and succeeds on a later retrieval check.
5. **Review due:** prior evidence exists but delayed retrieval is absent, unsuccessful or stale.

No percentage or fixed number of attempts is hard-coded in v0.1. The policy must be validated against the age, skill type, item difficulty and false-positive risk before production.

## 5. Runtime capability boundary

The current app implementation has these constraints:

- Content schema v3 stores one `language` value per activity.
- Activity type is currently limited to `SINGLE_CHOICE`.
- Correct and incorrect feedback are activity-level strings; there is no per-option misconception feedback.
- The current model does not persist prerequisites, assessment phase, misconception code, mastery state or review schedule as first-class skill metadata.
- The current renderer does not yet support the full activity archetypes listed in this registry.

Therefore, this document is a target curriculum/assessment model, not a statement that sorting, sequencing, audio, handwriting, drag-and-drop, practical observation or adaptive review are already implemented. The next engineering deliverable is a backward-compatible content-model design and migration/test plan; do not add unsupported fields to existing JSON packs.

## 6. Open evidence and calibration work

1. Confirm the exact family editions/ISBNs for Hop Skip and Jump Senior KG, Maths Xpress Senior KG, English World Level 2, Mathematics 2A/2B, and Science Level 2.
2. Obtain full/official scope evidence for Hop Skip Senior KG and Maths Xpress Senior KG; current evidence is catalogue-level.
3. Obtain current ISBN-matched Macmillan Mathematics 2A Scope and Sequence; do not assign older teacher-book unit detail to the 2027 edition without proof.
4. Compare selected English skills against the complete relevant Cambridge framework only after deciding which FLE/ESL comparison is appropriate; do not infer the learner's pathway.
5. Calibrate item difficulty, distractor quality, mastery confidence and review timing using reviewed original items and actual learner evidence.
6. Review SCI-G2-U01's 32 draft items against these Skill IDs, misconceptions and assessment phases before production approval.

## 7. Source references

- Macmillan Science Level 2 Scope and Sequence: https://api.macmillanenglish.com/fileadmin/user_upload/Catalogue/Samples/Macmillan_Science/Macmillan_Science_Level_2_Scope_and_Sequence.pdf
- Macmillan English World Level 2: https://www.macmillanenglish.com/us/catalogue/courses/young-learners/english-world/levels-samples
- Macmillan Mathematics: https://www.macmillanenglish.com/us/catalogue/courses/young-learners/macmillan-mathematics/levels-samples
- Macmillan Education India School Catalogue 2025: https://macmillaneducation.in/wp-content/uploads/2025/04/MacEd-2025-1.pdf
- Sudan SMILE Starter 2 Grade 2: https://www.mdl.edu.sd/img/bookpdf/english2_1664176727.pdf
- Sudan Grade 2 Mathematics revised edition 2021: https://www.mdl.edu.sd/img/bookpdf/math2_1659464927.pdf
- Cambridge Early Years: https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-early-years/curriculum/
- Cambridge Primary English 0057/0058: https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-primary/curriculum/english/
- Cambridge Primary Mathematics 0096: https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-primary/curriculum/mathematics/
- Cambridge Primary Science 0097: https://www.cambridgeinternational.org/programmes-and-qualifications/cambridge-primary/curriculum/science/

**Review note:** this is an AAS2-authored internal skills specification. Publisher sources inform alignment only; all AAS2 learner-facing content and assessment items remain original.
