import fs from "node:fs";
import path from "node:path";
import assert from "node:assert/strict";

const catalog = JSON.parse(fs.readFileSync("content/catalog/seven-book-catalog.json", "utf8"));
const patch = JSON.parse(fs.readFileSync("content/staging/legacy-question-bank-review-batch-01.json", "utf8"));
const reviewQueue = JSON.parse(fs.readFileSync("content/staging/legacy-question-bank-review-queue-v0.1.json", "utf8"));
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

assert.equal(optionArt.count, 374);
assert.equal(optionArt.files.length, 374);
assert.ok(unique(optionArt.files.map(file => file.path)), "Option-art paths must be unique.");
assert.ok(unique(optionArt.files.map(file => file.sha256)), "Option-art hashes must be unique.");
for (const file of optionArt.files) {
  assert.ok(fs.existsSync(path.join(assetRoot, file.path)), "Missing option illustration: " + file.path);
  assert.ok(file.bytes > 0 && /^[a-f0-9]{64}$/.test(file.sha256), "Invalid option-art metadata: " + file.path);
}

let englishActivityCount = 0;
const seenActivityIds = new Set();
for (const pack of packs) {
  for (const activity of pack.activities) {
    if (activity.language !== "en") continue;
    englishActivityCount += 1;
    assert.ok(!seenActivityIds.has(activity.activityId), "Duplicate English activity ID: " + activity.activityId);
    seenActivityIds.add(activity.activityId);
    assert.ok(audio.activities[activity.activityId], "Missing audio manifest mapping: " + activity.activityId);
    assert.ok(fs.existsSync(path.join(assetRoot, activity.promptImageAssetPath)), "Missing prompt image reference: " + activity.activityId);
    assert.ok(fs.existsSync(path.join(assetRoot, activity.promptAudioAssetPath)), "Missing prompt audio reference: " + activity.activityId);
    assert.ok(fs.existsSync(path.join(assetRoot, activity.feedbackCorrectAudioAssetPath)), "Missing correct-feedback audio: " + activity.activityId);
    assert.ok(fs.existsSync(path.join(assetRoot, activity.feedbackIncorrectAudioAssetPath)), "Missing incorrect-feedback audio: " + activity.activityId);
    const optionIds = activity.options.map(option => option.optionId);
    assert.deepEqual(Object.keys(activity.optionAudioAssetPaths).sort(), [...optionIds].sort(),
      "Every option must have exactly one audio reference: " + activity.activityId);
    for (const assetPath of Object.values(activity.optionAudioAssetPaths)) {
      assert.ok(fs.existsSync(path.join(assetRoot, assetPath)), "Missing option audio reference: " + activity.activityId);
    }
  }
}
assert.equal(englishActivityCount, 282);
assert.equal(seenActivityIds.size, 282);
const readingActivities = packs[1].activities.filter(activity => activity.skillId === "aas2-aboudy-reading-comprehension");
assert.ok(readingActivities.every(activity => activity.prompt.trim().length >= 75),
  "Reading-comprehension prompts must include a self-contained stimulus, not only a question.");
const mainIdea = readingActivities.find(activity => activity.activityId === "new-aboudy-english-019");
assert.ok(mainIdea.prompt.startsWith("Read the story:") && mainIdea.prompt.includes("What is the main idea of the story?"),
  "The main-idea activity must include its story stimulus in the prompt.");
assert.equal(questionArt.files.filter(file => seenActivityIds.has(file.activityId)).length, 282);

console.log("PASS: seven-book catalog and English World 12-unit scope, 616-item review queue (468 single-choice, 28 specialized option interactions, 120 custom), reading-stimulus checks, 1,692 OGG files, 282 question illustrations, 374 option illustrations, and all 282 activity-to-media references.");
