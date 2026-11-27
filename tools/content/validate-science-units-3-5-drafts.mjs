import fs from "node:fs";
import assert from "node:assert/strict";
const file = JSON.parse(fs.readFileSync("content/staging/science-level2-units-3-5-original-activity-drafts-v0.1.json", "utf8"));
assert.equal(file.bookId, "macmillan-science-level2");
assert.equal(file.learnerPath, "GRADE_2");
assert.equal(file.summary.units, 3);
assert.equal(file.summary.lessons, 12);
assert.equal(file.summary.activities, 36);
assert.equal(file.summary.runtimeMapped, 0);
assert.equal(file.releaseGate, "STAGING_ONLY");
assert.equal(file.status, "SUPERSEDED_REFERENCE_ONLY");
assert.ok(file.records.every(item => item.reviewStatus === "SUPERSEDED_DUPLICATE_DO_NOT_INTEGRATE"));
assert.equal(file.records.length, 36);
assert.equal(new Set(file.records.map(x => x.activityId)).size, 36);
const lessons = new Set(file.records.map(x => x.lessonId));
assert.equal(lessons.size, 12);
for (const item of file.records) {
  assert.equal(item.runtimeEligible, false);
  assert.equal(item.reviewStatus, "SUPERSEDED_DUPLICATE_DO_NOT_INTEGRATE");
  assert.equal(item.mediaStatus, "NOT_GENERATED");
  assert.equal(item.options.length, 3);
  assert.equal(new Set(item.options.map(x => x.optionId)).size, 3);
  assert.equal(item.options.filter(x => x.optionId === item.correctOptionId).length, 1);
  assert.ok(item.options.some(x => x.optionId === item.correctOptionId));
  assert.ok(item.prompt && item.feedbackCorrect && item.feedbackIncorrect);
}
for (const lessonId of lessons) assert.equal(file.records.filter(x => x.lessonId === lessonId).length, 3);
console.log("Science Units 3–5 draft validation: PASS (36 unique original items; staging only).");
