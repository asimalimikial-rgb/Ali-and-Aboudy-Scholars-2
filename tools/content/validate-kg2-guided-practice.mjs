import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("content/staging/kg2-hop-skip-guided-practice-v0.1.json","utf8"));
assert.equal(p.bookId,"hop-skip-jump-senior-kg");assert.equal(p.summary.guidedActivities,4);assert.equal(p.summary.scoredQuestions,0);assert.equal(p.summary.runtimeIntegrated,0);assert.equal(p.records.length,4);assert.equal(new Set(p.records.map(x=>x.activityId)).size,4);
for(const x of p.records){assert.ok(x.lessonId&&x.skillId);assert.ok(x.instructions.length>=3);assert.ok(x.observableCriteria.length>=2);assert.match(x.assessment,/Adult observation/);}
console.log("KG2 guided practice validation: PASS (4 non-scored cards; staging only).");