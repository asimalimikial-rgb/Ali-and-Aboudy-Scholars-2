import fs from "node:fs";
import assert from "node:assert/strict";

const catalog = JSON.parse(fs.readFileSync("content/catalog/seven-book-catalog.json", "utf8"));
const patch = JSON.parse(fs.readFileSync("content/staging/legacy-question-bank-review-batch-01.json", "utf8"));

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

console.log("PASS staging content validation: seven-book catalog, 282-item reconciliation, 62 Science mappings, and seven pending legacy review patches.");
