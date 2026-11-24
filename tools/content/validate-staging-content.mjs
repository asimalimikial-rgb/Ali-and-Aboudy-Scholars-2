import fs from "node:fs";
import path from "node:path";
import assert from "node:assert/strict";
import crypto from "node:crypto";

const catalog = JSON.parse(fs.readFileSync("content/catalog/seven-book-catalog.json", "utf8"));
const patch = JSON.parse(fs.readFileSync("content/staging/legacy-question-bank-review-batch-01.json", "utf8"));
const reviewQueue = JSON.parse(fs.readFileSync("content/staging/legacy-question-bank-review-queue-v0.1.json", "utf8"));
const englishWorldQueue = JSON.parse(fs.readFileSync("content/staging/english-world-level2-activity-map-review-v0.1.json", "utf8"));
const mathematicsQueue = JSON.parse(fs.readFileSync("content/staging/macmillan-mathematics-level2-activity-map-review-v0.1.json", "utf8"));
const legacyCorrectionPatch = JSON.parse(fs.readFileSync("content/staging/legacy-question-bank-correction-patch-v0.1.json", "utf8"));
const legacyRendererRequirements = JSON.parse(fs.readFileSync("content/staging/legacy-question-bank-renderer-requirements-v0.1.json", "utf8"));
const assetRoot = "app/src/main/assets";
const packs = [
  JSON.parse(fs.readFileSync(path.join(assetRoot, "content-packs/aas2-kg2-foundation.json"), "utf8")),
  JSON.parse(fs.readFileSync(path.join(assetRoot, "content-packs/aas2-grade2-foundation.json"), "utf8"))
];

assert.equal(catalog.books.length, 7, "The selected book catalog must contain exactly seven books.");
const bookIds = catalog.books.map(book => book.bookId);
assert.equal(new Set(bookIds).size, 7, "Book IDs must be unique.");
assert.ok(catalog.books.every(book => book.learnerPath === "KG2" || book.learnerPath === "GRADE_2"),
  "Every book must belong to one of the two learner paths.");
assert.ok(catalog.books.every(book => Number.isInteger(book.currentAuthoredActivities) && book.currentAuthoredActivities >= 0),
  "Book activity counts must be non-negative integers.");
assert.ok(!catalog.books.some(book => /SMILE/i.test(book.title)),
  "SMILE must remain outside the seven-book scope.");
assert.equal(catalog.books.reduce((sum, book) => sum + book.currentAuthoredActivities, 0), 237,
  "The seven-book authored activity subtotal must remain 237.");
assert.equal(catalog.unassignedCurrentActivities.broadScienceEnrichment, 25);
assert.equal(catalog.unassignedCurrentActivities.earlierPilotActivities, 20);
assert.equal(237 + 25 + 20, 282, "Mapped candidates plus unassigned content must reconcile to 282.");
const science = catalog.books.find(book => book.bookId === "macmillan-science-level-2");
assert.ok(science, "Macmillan Science Level 2 must be present.");
assert.equal(science.runtimeMappedActivities, 62);
assert.equal(science.integratedUnits.reduce((sum, unit) => sum + unit.activities, 0), 62);
assert.equal(science.integratedUnits.reduce((sum, unit) => sum + unit.lessons, 0), 9);
assert.equal(catalog.integrationState.completeSevenBookCoverage, "NOT_COMPLETE",
  "The catalog must not claim complete seven-book coverage.");
const englishWorld = catalog.books.find(book => book.bookId === "english-world-level-2");
assert.equal(englishWorld.officialScopeSequence.length, 12,
  "English World Level 2 must retain the verified 12-unit scope sequence.");

assert.equal(patch.source.reviewedLegacyCount, 616);
const patches = patch.patches;
const patchIds = patches.map(item => item.id);
assert.equal(new Set(patchIds).size, patches.length, "Review patch IDs must be unique.");
assert.equal(patches.length, 7, "Batch 01 must contain seven item patches.");
assert.ok(patches.every(item => item.reviewStatus === "REVISED_PENDING_TECHNICAL_QA"),
  "Batch 01 items must remain pending technical QA.");
const objective = patches.filter(item => item.changes.options);
assert.equal(objective.length, 5, "Five objective items should receive distinct revised prompts.");
for (const item of objective) {
  const { options, answer } = item.changes;
  assert.ok(options.length >= 3 && options.length <= 4, item.id + " must have 3–4 options.");
  assert.equal(new Set(options).size, options.length, item.id + " contains duplicate options.");
  assert.equal(options.filter(option => option === answer).length, 1,
    item.id + " must have exactly one keyed answer.");
}
const openResponse = patches.filter(item => item.changes.answerKind === "OPEN_RESPONSE_RUBRIC");
assert.equal(openResponse.length, 2, "Both drawing tasks must use an open-response rubric.");
for (const item of openResponse) {
  assert.ok(item.changes.responseRubric.criteria.length >= 2, item.id + " needs observable rubric criteria.");
  assert.equal(item.changes.answer, "", item.id + " must not invent a fixed answer.");
}
assert.match(patch.reviewPolicy.arabicHelper, /Do not expose/i,
  "Legacy Arabic helper fields must not be exposed in the child runtime.");

assert.equal(reviewQueue.summary.total, 616);
assert.equal(reviewQueue.records.length, 616);
assert.equal(reviewQueue.summary.ali, 301);
assert.equal(reviewQueue.summary.aboudy, 315);
assert.equal(reviewQueue.summary.optionBearingItems, 496);
assert.equal(reviewQueue.summary.strictSingleChoiceCandidates, 468);
assert.equal(reviewQueue.summary.specializedOptionInteractions, 28);
assert.equal(reviewQueue.summary.customInteractions, 120);
assert.equal(reviewQueue.summary.exactDuplicateGroups, 5);
assert.equal(reviewQueue.summary.currentPackOverlaps, 1);
assert.equal(reviewQueue.summary.openResponseRubrics, 2);
assert.equal(new Set(reviewQueue.records.map(item => item.questionId)).size, 616,
  "Every legacy question must have a unique review-queue ID.");
assert.equal(reviewQueue.records.filter(item => item.strictSingleChoiceCandidate).length, 468);
assert.equal(reviewQueue.records.filter(item => item.specializedOptionInteraction).length, 28);
assert.equal(reviewQueue.records.filter(item => item.customInteraction).length, 120);
assert.ok(reviewQueue.records.every(item => item.bookId && item.unitId && item.reviewStatus !== "APPROVED"),
  "Every legacy question must retain its book/unit context and remain unapproved until review is complete.");
assert.ok(reviewQueue.records.every(item => Object.values(item.reviewChecks).every(value => value === false)),
  "Review checks must start unchecked; do not pre-approve legacy content.");

assert.equal(englishWorldQueue.bookId, "english-world-level-2");
assert.equal(englishWorldQueue.packVersion, "0.4.4");
assert.equal(englishWorldQueue.summary.total, 35);
assert.equal(englishWorldQueue.summary.grammarAndWordStudy, 29);
assert.equal(englishWorldQueue.summary.readingComprehension, 6);
assert.equal(englishWorldQueue.summary.candidateAlignment, 15);
assert.equal(englishWorldQueue.summary.rewriteRequired, 0);
assert.equal(englishWorldQueue.summary.supplementaryReview, 20);
assert.equal(englishWorldQueue.summary.runtimeMapped, 0);
assert.equal(englishWorldQueue.records.length, 35);
assert.equal(new Set(englishWorldQueue.records.map(item => item.activityId)).size, 35);
assert.ok(englishWorldQueue.records.every(item =>
  ["PENDING_PEDAGOGY_REVIEW", "REVISED_PENDING_PEDAGOGY_QA"].includes(item.reviewStatus) && item.runtimeUnitId === null
), "English World candidates must remain pending until unit mapping is reviewed.");
const grade2Pack = packs[1];
assert.equal(grade2Pack.version, "0.4.4");
const mathActivities = grade2Pack.activities.filter(item => item.activityId.startsWith("new-aboudy-maths-"));
assert.equal(mathActivities.length, 40);
assert.equal(mathematicsQueue.packVersion, grade2Pack.version);
assert.equal(mathematicsQueue.summary.total, 40);
assert.equal(mathematicsQueue.summary.math2A, 31);
assert.equal(mathematicsQueue.summary.math2B, 9);
assert.equal(mathematicsQueue.summary.candidateAlignment, 40);
assert.equal(mathematicsQueue.summary.runtimeMapped, 0);
assert.equal(mathematicsQueue.records.length, 40);
assert.equal(new Set(mathematicsQueue.records.map(item => item.activityId)).size, 40);
assert.equal(mathematicsQueue.records.filter(item => item.bookPart === "2A").length, 31);
assert.equal(mathematicsQueue.records.filter(item => item.bookPart === "2B").length, 9);
assert.ok(mathematicsQueue.records.every(item => item.alignmentStatus === "CANDIDATE_ALIGNMENT" && item.reviewStatus === "PENDING_PEDAGOGY_REVIEW" && item.runtimeUnitId === null));
for (const mapping of mathematicsQueue.records) {
  const activity = mathActivities.find(item => item.activityId === mapping.activityId);
  assert.ok(activity, "Missing Mathematics activity: " + mapping.activityId);
  assert.equal(activity.prompt, mapping.prompt, "Stale Mathematics prompt: " + mapping.activityId);
  assert.equal(activity.skillId, mapping.skillId, "Stale Mathematics skill: " + mapping.activityId);
}
assert.equal(catalog.books.find(book => book.bookId === "macmillan-mathematics-level-2a").isbn, "9781035130382");
assert.equal(catalog.books.find(book => book.bookId === "macmillan-mathematics-level-2a").currentAuthoredActivities, 31);
assert.equal(catalog.books.find(book => book.bookId === "macmillan-mathematics-level-2b").currentAuthoredActivities, 9);
assert.equal(legacyCorrectionPatch.patchItems.length, 12);
assert.equal(new Set(legacyCorrectionPatch.patchItems.map(item => item.questionId)).size, 12);
assert.ok(legacyCorrectionPatch.patchItems.every(item => item.reviewStatus === "REVISED_PENDING_TECHNICAL_QA" && item.productionApproved === false));
const busFix = legacyCorrectionPatch.patchItems.find(item => item.questionId === "legacy-ali-0073");
assert.equal(busFix.changes.answer, false);
assert.equal(busFix.changes.legacyDataAnswer, false);
assert.equal(legacyRendererRequirements.counts.legacyRecords, 616);
assert.equal(legacyRendererRequirements.specialized.length, 28);
assert.equal(legacyRendererRequirements.custom.length, 120);
assert.equal(legacyRendererRequirements.storyBearingMultipleChoice.length, 19);
assert.equal(legacyRendererRequirements.visualBearingMultipleChoice.length, 15);
assert.equal(legacyRendererRequirements.counts.structuredStimulusMultipleChoice, 34);
assert.equal(legacyRendererRequirements.targetRevealMultipleChoice.length, 69);
assert.equal(legacyRendererRequirements.counts.targetRevealMultipleChoice, 69);
assert.equal(new Set([
  ...legacyRendererRequirements.specialized.map(item => item.questionId),
  ...legacyRendererRequirements.custom.map(item => item.questionId),
  ...legacyRendererRequirements.storyBearingMultipleChoice.map(item => item.questionId),
  ...legacyRendererRequirements.visualBearingMultipleChoice.map(item => item.questionId),
  ...legacyRendererRequirements.targetRevealMultipleChoice.map(item => item.questionId)
]).size, 251);
const capitalizationItem = grade2Pack.activities.find(item => item.activityId === "new-aboudy-english-001");
assert.equal(capitalizationItem.correctOptionId, "b");
assert.equal(capitalizationItem.options.find(option => option.optionId === "b").text, "I visit Grandma on Monday.");
const longAItem = grade2Pack.activities.find(item => item.activityId === "new-aboudy-english-020");
assert.equal(longAItem.correctOptionId, "b");
assert.equal(longAItem.options.find(option => option.optionId === "b").text, "cake");
const conjunctionItem = grade2Pack.activities.find(item => item.activityId === "new-aboudy-english-023");
assert.equal(conjunctionItem.correctOptionId, "b");
assert.equal(conjunctionItem.options.find(option => option.optionId === "b").text, "but");

// Verify all generated media against manifests and the actual checked-in files.
const audio = JSON.parse(fs.readFileSync(path.join(assetRoot, "speech/manifest.json"), "utf8"));
const questionArt = JSON.parse(fs.readFileSync(path.join(assetRoot, "question-art/manifest.json"), "utf8"));
const optionArt = JSON.parse(fs.readFileSync(path.join(assetRoot, "option-art/manifest.json"), "utf8"));
const unique = values => new Set(values).size === values.length;

assert.equal(audio.count, 1692);
assert.equal(audio.files.length, 1692);
assert.equal(Object.keys(audio.activities).length, 282);
assert.equal(audio.settings.language, "en");
assert.equal(audio.settings.speed, 0.70);
assert.ok(unique(audio.files.map(file => file.file)), "Audio filenames must be unique.");
assert.ok(unique(audio.files.map(file => file.sha256)), "Audio hashes must be unique.");
assert.ok(audio.files.every(file =>
  file.language === "en" && file.speed === 0.70 && file.bytes > 0 &&
  file.durationMs >= 100 && /^[a-f0-9]{64}$/.test(file.sha256)
), "Every audio manifest entry must have valid English/speed/hash/size/duration metadata.");
for (const file of audio.files) {
  assert.ok(fs.existsSync(path.join(assetRoot, "speech", file.file)), "Missing OGG file: " + file.file);
}
const audioRoles = audio.files.reduce((counts, file) => {
  counts[file.role] = (counts[file.role] || 0) + 1;
  return counts;
}, {});
assert.deepEqual(audioRoles, {
  "feedback-correct": 282,
  "feedback-incorrect": 282,
  option: 846,
  prompt: 282
});

assert.equal(questionArt.questionCount, 282);
assert.equal(questionArt.files.length, 282);
assert.equal(questionArt.formats.svg, 245);
assert.equal(questionArt.formats.png, 37);
assert.ok(unique(questionArt.files.map(file => file.activityId)), "Question-art activity IDs must be unique.");
assert.ok(unique(questionArt.files.map(file => file.path)), "Question-art paths must be unique.");
assert.ok(unique(questionArt.files.map(file => file.sha256)), "Question-art hashes must be unique.");
for (const file of questionArt.files) {
  assert.ok(fs.existsSync(path.join(assetRoot, file.path)), "Missing question illustration: " + file.path);
  assert.ok(file.bytes > 0 && /^[a-f0-9]{64}$/.test(file.sha256), "Invalid question-art metadata: " + file.path);
}

assert.equal(optionArt.count, 373);
assert.equal(optionArt.files.length, 373);
assert.ok(unique(optionArt.files.map(file => file.path)), "Option-art paths must be unique.");
assert.ok(unique(optionArt.files.map(file => file.sha256)), "Option-art hashes must be unique.");
for (const file of optionArt.files) {
  assert.ok(fs.existsSync(path.join(assetRoot, file.path)), "Missing option illustration: " + file.path);
  assert.ok(file.bytes > 0 && /^[a-f0-9]{64}$/.test(file.sha256), "Invalid option-art metadata: " + file.path);
}

const audioFileByName = new Map(audio.files.map(file => [file.file, file]));
const questionArtByActivity = new Map(questionArt.files.map(file => [file.activityId, file]));
const optionArtPaths = new Set(optionArt.files.map(file => file.path));
let englishActivityCount = 0;
const seenActivityIds = new Set();
for (const pack of packs) {
  for (const activity of pack.activities) {
    if (activity.language !== "en") continue;
    englishActivityCount += 1;
    assert.ok(!seenActivityIds.has(activity.activityId), "Duplicate English activity ID: " + activity.activityId);
    seenActivityIds.add(activity.activityId);
    const audioMap = audio.activities[activity.activityId];
    assert.ok(audioMap, "Missing audio manifest mapping: " + activity.activityId);
    const imageEntry = questionArtByActivity.get(activity.activityId);
    assert.ok(imageEntry, "Missing question-art manifest entry: " + activity.activityId);
    assert.equal(imageEntry.path, activity.promptImageAssetPath, "Prompt image manifest/path mismatch: " + activity.activityId);
    assert.ok(fs.existsSync(path.join(assetRoot, activity.promptImageAssetPath)), "Missing prompt image reference: " + activity.activityId);
    assert.ok(fs.existsSync(path.join(assetRoot, activity.promptAudioAssetPath)), "Missing prompt audio reference: " + activity.activityId);
    assert.equal(audioMap.prompt, path.basename(activity.promptAudioAssetPath), "Prompt audio manifest/path mismatch: " + activity.activityId);
    assert.equal(audioFileByName.get(audioMap.prompt)?.text, activity.prompt, "Prompt audio text is stale: " + activity.activityId);
    assert.ok(fs.existsSync(path.join(assetRoot, activity.feedbackCorrectAudioAssetPath)), "Missing correct-feedback audio: " + activity.activityId);
    assert.ok(fs.existsSync(path.join(assetRoot, activity.feedbackIncorrectAudioAssetPath)), "Missing incorrect-feedback audio: " + activity.activityId);
    assert.equal(audioMap.feedbackCorrect, path.basename(activity.feedbackCorrectAudioAssetPath), "Correct-feedback audio mismatch: " + activity.activityId);
    assert.equal(audioMap.feedbackIncorrect, path.basename(activity.feedbackIncorrectAudioAssetPath), "Incorrect-feedback audio mismatch: " + activity.activityId);
    assert.equal(audioFileByName.get(audioMap.feedbackCorrect)?.text, activity.feedbackCorrect, "Correct-feedback audio text is stale: " + activity.activityId);
    assert.equal(audioFileByName.get(audioMap.feedbackIncorrect)?.text, activity.feedbackIncorrect, "Incorrect-feedback audio text is stale: " + activity.activityId);
    const optionIds = activity.options.map(option => option.optionId);
    assert.deepEqual(Object.keys(activity.optionAudioAssetPaths).sort(), [...optionIds].sort(),
      "Every option must have exactly one audio reference: " + activity.activityId);
    for (const option of activity.options) {
      const assetPath = activity.optionAudioAssetPaths[option.optionId];
      const manifestFile = audioMap.options[option.optionId];
      assert.ok(fs.existsSync(path.join(assetRoot, assetPath)), "Missing option audio reference: " + activity.activityId);
      assert.equal(manifestFile, path.basename(assetPath), "Option audio manifest/path mismatch: " + activity.activityId + "/" + option.optionId);
      assert.equal(audioFileByName.get(manifestFile)?.text, option.text, "Option audio text is stale: " + activity.activityId + "/" + option.optionId);
      if (option.imageAssetPath) {
        assert.ok(optionArtPaths.has(option.imageAssetPath), "Option image is missing from the option-art manifest: " + option.imageAssetPath);
      }
    }
  }
}
assert.equal(englishActivityCount, 282);
assert.equal(seenActivityIds.size, 282);
const readingActivities = packs[1].activities.filter(activity => activity.skillId === "aas2-aboudy-reading-comprehension");
assert.ok(readingActivities.every(activity => activity.prompt.trim().length >= 65),
  "Reading-comprehension prompts must include a self-contained stimulus, not only a question.");
const mainIdea = readingActivities.find(activity => activity.activityId === "new-aboudy-english-019");
assert.ok(mainIdea.prompt.startsWith("Read the story:") && mainIdea.prompt.includes("What is the main idea of the story?"),
  "The main-idea activity must include its story stimulus in the prompt.");
assert.equal(questionArt.files.filter(file => seenActivityIds.has(file.activityId)).length, 282);

const scienceDraftPath = path.join(process.cwd(), "content/staging/science-g2-unit-03-senses-draft-v0.1.json");
const scienceDraft = JSON.parse(fs.readFileSync(scienceDraftPath, "utf8"));
assert.equal(scienceDraft.unit.unitId, "SCI-G2-U03");
assert.equal(scienceDraft.activities.length, 40);
assert.equal(scienceDraft.lessons.length, 5);
assert.equal(scienceDraft.status, "DRAFT_PENDING_REVIEW");
assert.equal(scienceDraft.releaseGate.runtimeIntegrated, false);
const scienceIds = new Set();
const sciencePrompts = new Set();
for (const activity of scienceDraft.activities) {
  assert.match(activity.activityId, /^aas2-g2-sci-u03-\d{3}$/);
  assert.equal(activity.activityType, "SINGLE_CHOICE");
  assert.equal(activity.reviewStatus, "DRAFT_PENDING_SCIENCE_PEDAGOGY_LANGUAGE_AND_ACCESSIBILITY_REVIEW");
  assert.equal(activity.runtimeStatus, "NOT_INTEGRATED");
  assert.equal(activity.assetStatus, "STAGED_ORIGINAL_SVG_PENDING_RENDER_QA");
  assert.equal(activity.options.length, 3);
  const optionIds = activity.options.map(option => option.optionId);
  const optionTexts = activity.options.map(option => option.text.trim().toLowerCase());
  assert.equal(new Set(optionIds).size, 3);
  assert.equal(new Set(optionTexts).size, 3);
  assert.ok(optionIds.includes(activity.correctOptionId));
  assert.deepEqual(Object.keys(activity.optionRationales).sort(), [...optionIds].sort());
  assert.ok(activity.prompt.trim().length > 10);
  assert.ok(!scienceIds.has(activity.activityId), "Duplicate Science Unit 3 ID: " + activity.activityId);
  scienceIds.add(activity.activityId);
  const normalizedPrompt = activity.prompt.trim().toLowerCase().replace(/\s+/g, " ");
  assert.ok(!sciencePrompts.has(normalizedPrompt), "Duplicate Science Unit 3 prompt: " + activity.activityId);
  sciencePrompts.add(normalizedPrompt);
}
for (const lesson of scienceDraft.lessons) {
  assert.equal(scienceDraft.activities.filter(activity => activity.lessonId === lesson.lessonId).length, 8);
}

const stagedScience = [
  ["content/staging/science-g2-unit-03-senses-draft-v0.1.json", "SCI-G2-U03", 40, 5, /^aas2-g2-sci-u03-\d{3}$/],
  ["content/staging/science-g2-unit-04-materials-draft-v0.1.json", "SCI-G2-U04", 20, 4, /^aas2-g2-sci-u04-\d{3}$/],
  ["content/staging/science-g2-unit-05-night-sky-draft-v0.1.json", "SCI-G2-U05", 12, 3, /^aas2-g2-sci-u05-\d{3}$/]
];
const stagedScienceIds = new Set();
const stagedSciencePrompts = new Set();
let stagedScienceCount = 0;
for (const [relativePath, unitId, expectedCount, expectedLessons, idPattern] of stagedScience) {
  const draft = JSON.parse(fs.readFileSync(path.join(process.cwd(), relativePath), "utf8"));
  assert.equal(draft.unit.unitId, unitId);
  assert.equal(draft.activities.length, expectedCount);
  assert.equal(draft.lessons.length, expectedLessons);
  assert.equal(draft.status, "DRAFT_PENDING_REVIEW");
  assert.equal(draft.releaseGate.runtimeIntegrated, false);
  for (const activity of draft.activities) {
    assert.match(activity.activityId, idPattern);
    assert.ok(!stagedScienceIds.has(activity.activityId));
    stagedScienceIds.add(activity.activityId);
    const prompt = activity.prompt.trim().toLowerCase().replace(/\s+/g, " ");
    assert.ok(!stagedSciencePrompts.has(prompt));
    stagedSciencePrompts.add(prompt);
    assert.equal(activity.reviewStatus, "DRAFT_PENDING_SCIENCE_PEDAGOGY_LANGUAGE_AND_ACCESSIBILITY_REVIEW");
    assert.equal(activity.runtimeStatus, "NOT_INTEGRATED");
    assert.equal(activity.options.length, 3);
    const optionIds = activity.options.map(option => option.optionId);
    assert.equal(new Set(optionIds).size, 3);
    assert.equal(new Set(activity.options.map(option => option.text.trim().toLowerCase())).size, 3);
    assert.ok(optionIds.includes(activity.correctOptionId));
    assert.deepEqual(Object.keys(activity.optionRationales).sort(), [...optionIds].sort());
    assert.ok(activity.feedbackCorrect && activity.feedbackIncorrect);
    assert.ok(["PENDING_STAGING_GENERATION", "STAGED_GENERATED_PENDING_TRANSCRIPT_AND_PLAYBACK_QA"].includes(activity.audioStatus));
  }
  stagedScienceCount += draft.activities.length;
}
assert.equal(stagedScienceCount, 72);
const scienceArtManifest = JSON.parse(fs.readFileSync(path.join(process.cwd(), "content/staging/media/science-g2/illustrations/manifest.json"), "utf8"));
assert.equal(scienceArtManifest.files.length, 3);
for (const file of scienceArtManifest.files) {
  const svg = fs.readFileSync(path.join(process.cwd(), file.path), "utf8");
  assert.match(svg, /<svg[\s>]/);
  assert.match(svg, /viewBox=/);
  assert.match(svg, /<\/svg>/);
}

const scienceAudioManifestPath = path.join(process.cwd(), "content/staging/media/science-g2/speech/manifest.json");
const scienceDraftFiles = stagedScience.map(([relativePath]) => relativePath);
const allStagedScienceActivities = scienceDraftFiles.flatMap(relativePath =>
  JSON.parse(fs.readFileSync(path.join(process.cwd(), relativePath), "utf8")).activities
);
const generatedScienceAudio = allStagedScienceActivities.filter(activity =>
  activity.audioStatus === "STAGED_GENERATED_PENDING_TRANSCRIPT_AND_PLAYBACK_QA"
);
if (generatedScienceAudio.length > 0) {
  assert.ok(fs.existsSync(scienceAudioManifestPath), "Generated science audio manifest is missing.");
  const audioManifest = JSON.parse(fs.readFileSync(scienceAudioManifestPath, "utf8"));
  assert.equal(audioManifest.scope, "STAGING_ONLY_NOT_RUNTIME_INTEGRATED");
  assert.equal(audioManifest.settings.language, "en");
  assert.equal(audioManifest.settings.speed, 0.70);
  assert.equal(audioManifest.sourceEnglishActivityCount, 72);
  assert.equal(audioManifest.count, 432);
  const audioFilesByPath = new Map(audioManifest.files.map(item => [item.path, item]));
  for (const activity of generatedScienceAudio) {
    const mapping = audioManifest.activityPaths[activity.activityId];
    assert.ok(mapping, "Science audio mapping is missing: " + activity.activityId);
    const roles = [
      ["prompt", activity.promptAudioAssetPath, activity.prompt],
      ["feedbackCorrect", activity.feedbackCorrectAudioAssetPath, activity.feedbackCorrect],
      ["feedbackIncorrect", activity.feedbackIncorrectAudioAssetPath, activity.feedbackIncorrect]
    ];
    for (const [role, assetPath, expectedText] of roles) {
      assert.equal(mapping[role], assetPath, "Science audio path mismatch: " + activity.activityId + "/" + role);
      const entry = audioFilesByPath.get(assetPath);
      assert.ok(entry, "Science audio manifest entry missing: " + assetPath);
      const payload = fs.readFileSync(path.join(process.cwd(), assetPath));
      assert.equal(payload.length, entry.bytes);
      assert.equal(crypto.createHash("sha256").update(payload).digest("hex"), entry.sha256);
      assert.equal(entry.text, expectedText);
    }
    assert.deepEqual(Object.keys(activity.optionAudioAssetPaths).sort(), activity.options.map(option => option.optionId).sort());
    for (const option of activity.options) {
      const assetPath = activity.optionAudioAssetPaths[option.optionId];
      assert.equal(mapping.options[option.optionId], assetPath);
      const entry = audioFilesByPath.get(assetPath);
      assert.ok(entry, "Science option audio missing: " + assetPath);
      const payload = fs.readFileSync(path.join(process.cwd(), assetPath));
      assert.equal(payload.length, entry.bytes);
      assert.equal(crypto.createHash("sha256").update(payload).digest("hex"), entry.sha256);
      assert.equal(entry.text, option.text);
    }
  }
}


const listeningLeakPatch = JSON.parse(fs.readFileSync(path.join(process.cwd(), "content/staging/legacy-question-bank-listening-target-leak-patch-v0.1.json"), "utf8"));
assert.equal(listeningLeakPatch.items.length, 69);
assert.deepEqual(listeningLeakPatch.summary, { total: 69, letters: 25, words: 34, numbers: 10, productionApproved: 0 });
const listeningIds = new Set();
for (const item of listeningLeakPatch.items) {
  assert.ok(!listeningIds.has(item.questionId));
  listeningIds.add(item.questionId);
  assert.ok(["letter", "word", "number"].includes(item.targetKind));
  assert.equal(item.answer, item.audioStimulus);
  assert.ok(item.options.includes(item.answer));
  const promptTokens = item.proposedPrompt.toLowerCase().split(/[^a-z0-9]+/);
  assert.ok(!promptTokens.includes(String(item.audioStimulus).toLowerCase()));
  assert.equal(item.rendererSupportRequired, "PLAY_SEPARATE_STIMULUS_AUDIO_WITHOUT_SHOWING_ITS_TEXT");
  assert.equal(item.reviewStatus, "REVISED_PENDING_AUDIO_AND_RENDERER_QA");
  assert.equal(item.productionApproved, false);
  assert.equal(item.runtimeIntegrated, false);
}


const legacyQualityPatch = JSON.parse(fs.readFileSync(path.join(process.cwd(), "content/staging/legacy-question-bank-quality-patch-v0.2.json"), "utf8"));
assert.equal(legacyQualityPatch.items.length, 12);
assert.equal(legacyQualityPatch.summary.promptCorrections, 5);
assert.equal(legacyQualityPatch.summary.arabicHelperCorrections, 7);
assert.equal(legacyQualityPatch.summary.productionApproved, 0);
assert.equal(new Set(legacyQualityPatch.items.map(item => item.questionId)).size, 12);
assert.ok(legacyQualityPatch.items.every(item => item.reviewStatus === "REVISED_PENDING_TECHNICAL_QA" && !item.runtimeIntegrated));

console.log("PASS: seven-book catalog, 12-unit English World scope, 40-item Mathematics map, 616-item legacy queue, 12 pending correction proposals, 28 specialized + 120 custom interactions, 34 structured-stimulus MC items, 72 staging-only Science Unit 3–5 items, 3 original unit illustrations, and all current media references.");

