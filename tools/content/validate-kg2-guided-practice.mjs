import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("content/staging/kg2-hop-skip-guided-practice-v0.1.json","utf8"));
assert.equal(p.bookId,"hop-skip-jump-senior-kg");assert.equal(p.summary.guidedActivities,6);assert.equal(p.summary.scoredQuestions,0);assert.equal(p.summary.runtimeIntegrated,6);assert.equal(p.records.length,6);assert.equal(new Set(p.records.map(x=>x.activityId)).size,6);
for(const x of p.records){assert.ok(x.lessonId&&x.skillId);assert.ok(x.instructions.length>=3);assert.ok(x.observableCriteria.length>=2);assert.match(x.assessment,/Adult observation/);}
const pack=JSON.parse(fs.readFileSync("app/src/main/assets/content-packs/aas2-kg2-foundation.json","utf8"));const runtime=new Map(pack.guidedTasks.map(x=>[x.activityId,x]));
for(const x of p.records){const t=runtime.get(x.activityId);assert.ok(t);assert.equal(t.bookId,x.bookId);assert.equal(t.unitId,x.unitId);assert.equal(t.lessonId,x.lessonId);assert.equal(t.skillId,x.skillId);assert.equal(t.title,x.title);}
console.log("KG2 guided practice validation: PASS (6 non-scored cards integrated; adult observation remains required).");