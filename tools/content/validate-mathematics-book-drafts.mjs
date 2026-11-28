import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("content/staging/mathematics-books-original-activity-drafts-v0.1.json","utf8"));
assert.equal(p.summary.books,3);assert.equal(p.summary.units,29);assert.equal(p.summary.activities,63);assert.equal(p.records.length,63);assert.equal(p.summary.runtimeMapped,48);assert.equal(p.summary.stagingOnlyBookCount,1);assert.equal(p.releaseGate,"PARTIAL_INTEGRATION_PENDING_PEDAGOGY_AND_MEDIA_QA");
assert.equal(new Set(p.records.map(x=>x.activityId)).size,63);
for(const [book,count] of Object.entries(p.summary.bookCounts))assert.equal(p.records.filter(x=>x.bookId===book).length,count);
for(const x of p.records){const shouldMap=x.bookId!=="maths-xpress-book-2";assert.equal(x.runtimeEligible,shouldMap);assert.equal(x.options.length,3);assert.equal(new Set(x.options.map(o=>o.optionId)).size,3);assert.equal(x.options.filter(o=>o.optionId===x.correctOptionId).length,1);assert.equal(x.reviewStatus,shouldMap?"INTEGRATED_PENDING_PEDAGOGY_QA":"PENDING_SCOPE_VERIFICATION_AND_PEDAGOGY_QA");}
assert.equal(p.records.find(x=>x.activityId==="aas2-2b-mm2b-u14-01").options.find(o=>o.optionId===p.records.find(x=>x.activityId==="aas2-2b-mm2b-u14-01").correctOptionId).text,"377","245 + 132 must equal 377.");
console.log("Mathematics: PASS (48 original items mapped to 2A/2B; 15 Maths Xpress Book 2 items remain staged pending scope verification).");
