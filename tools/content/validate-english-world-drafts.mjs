import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("content/staging/english-world-level2-original-activity-drafts-v0.1.json","utf8"));
assert.equal(p.bookId,"english-world-level-2");assert.equal(p.summary.units,12);assert.equal(p.summary.lessons,26);assert.equal(p.summary.activities,52);assert.equal(p.records.length,52);assert.equal(p.summary.runtimeMapped,52);assert.equal(p.releaseGate,"INTEGRATED_PENDING_PEDAGOGY_AND_MEDIA_QA");
assert.equal(new Set(p.records.map(x=>x.activityId)).size,52);assert.equal(new Set(p.records.map(x=>x.lessonId)).size,26);
for(const l of new Set(p.records.map(x=>x.lessonId)))assert.equal(p.records.filter(x=>x.lessonId===l).length,2);
for(const x of p.records){assert.equal(x.runtimeEligible,true);assert.equal(x.reviewStatus,"INTEGRATED_PENDING_PEDAGOGY_QA");assert.equal(x.options.length,3);assert.equal(new Set(x.options.map(o=>o.optionId)).size,3);assert.equal(x.options.filter(o=>o.optionId===x.correctOptionId).length,1);}
const pack=JSON.parse(fs.readFileSync("app/src/main/assets/content-packs/aas2-grade2-foundation.json","utf8"));const runtime=new Map(pack.activities.map(x=>[x.activityId,x]));
for(const x of p.records){const a=runtime.get(x.activityId);assert.ok(a);assert.equal(a.bookId,x.bookId);assert.equal(a.unitId,x.unitId);assert.equal(a.lessonId,x.lessonId);assert.equal(a.skillId,x.skillId);assert.ok(a.promptImageAssetPath&&a.promptAudioAssetPath);assert.ok(a.assessmentPhase);assert.ok(a.misconceptionCode);assert.equal(Object.keys(a.optionRationales).length,3);}
console.log("English World Level 2: PASS (52 original items mapped across all 26 lessons; QA pending).");
