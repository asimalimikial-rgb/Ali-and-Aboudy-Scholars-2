import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("content/staging/english-world-level2-original-activity-drafts-v0.1.json","utf8"));
assert.equal(p.bookId,"english-world-level-2"); assert.equal(p.summary.units,12); assert.equal(p.summary.lessons,26); assert.equal(p.summary.activities,52); assert.equal(p.records.length,52); assert.equal(p.summary.runtimeMapped,0); assert.equal(p.releaseGate,"STAGING_ONLY");
assert.equal(new Set(p.records.map(x=>x.activityId)).size,48); assert.equal(new Set(p.records.map(x=>x.lessonId)).size,26);
for(const l of new Set(p.records.map(x=>x.lessonId))) assert.equal(p.records.filter(x=>x.lessonId===l).length,2);
for(const x of p.records){assert.equal(x.runtimeEligible,false);assert.equal(x.reviewStatus,"PENDING_PEDAGOGY_MEDIA_QA");assert.equal(x.options.length,3);assert.equal(new Set(x.options.map(o=>o.optionId)).size,3);assert.equal(x.options.filter(o=>o.optionId===x.correctOptionId).length,1);}
console.log("English World Level 2 draft validation: PASS (48 original items; staging only).");