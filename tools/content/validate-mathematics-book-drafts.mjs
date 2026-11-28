import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("content/staging/mathematics-books-original-activity-drafts-v0.1.json","utf8"));
assert.equal(p.summary.books,3);assert.equal(p.summary.units,29);assert.equal(p.summary.activities,63);assert.equal(p.records.length,63);assert.equal(p.summary.runtimeMapped,63);assert.equal(p.summary.stagingOnlyBookCount,0);assert.equal(p.releaseGate,"INTEGRATED_PENDING_PEDAGOGY_AND_MEDIA_QA");assert.ok(p.companionPathOnlyBookIds.includes("maths-xpress-book-2"));
assert.equal(new Set(p.records.map(x=>x.activityId)).size,63);
for(const [book,count] of Object.entries(p.summary.bookCounts))assert.equal(p.records.filter(x=>x.bookId===book).length,count);
for(const x of p.records){assert.equal(x.runtimeEligible,true);assert.ok(x.lessonId);assert.equal(x.options.length,3);assert.equal(new Set(x.options.map(o=>o.optionId)).size,3);assert.equal(x.options.filter(o=>o.optionId===x.correctOptionId).length,1);assert.equal(x.reviewStatus,"INTEGRATED_PENDING_PEDAGOGY_QA");assert.ok(x.feedbackCorrect.length<=32);assert.ok(x.feedbackIncorrect.length<=48);assert.equal(x.lessonId,x.unitId+"-L01");}
assert.equal(p.records.filter(x=>x.bookId==="maths-xpress-book-2").length,15);
assert.ok(p.records.filter(x=>x.bookId==="maths-xpress-book-2").every(x=>x.scopeMappingType==="AAS2_COMPANION_PATH_NOT_PUBLISHER_TOC"));
console.log("Mathematics: PASS (63 original items mapped across 2A, 2B and the explicitly labelled AAS2 Maths Xpress Book 2 companion path).");
