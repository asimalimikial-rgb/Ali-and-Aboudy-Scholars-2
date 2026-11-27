import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("content/staging/mathematics-books-original-activity-drafts-v0.1.json","utf8"));
assert.equal(p.summary.books,3);assert.equal(p.summary.units,29);assert.equal(p.summary.activities,63);assert.equal(p.records.length,63);assert.equal(p.summary.runtimeMapped,0);assert.equal(p.releaseGate,"STAGING_ONLY");
assert.equal(new Set(p.records.map(x=>x.activityId)).size,63);
for(const [book,count] of Object.entries(p.summary.bookCounts)){assert.equal(p.records.filter(x=>x.bookId===book).length,count);}
for(const x of p.records){assert.equal(x.runtimeEligible,false);assert.equal(x.reviewStatus,"PENDING_MATHEMATICS_PEDAGOGY_MEDIA_QA");assert.equal(x.options.length,3);assert.equal(new Set(x.options.map(o=>o.optionId)).size,3);assert.equal(x.options.filter(o=>o.optionId===x.correctOptionId).length,1);}
console.log("Mathematics book drafts: PASS (63 original items; staging only).");