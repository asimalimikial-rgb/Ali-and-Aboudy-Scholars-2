import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("app/src/main/assets/content-packs/aas2-grade2-foundation.json","utf8"));
const paths=["content/staging/science-g2-unit-03-senses-draft-v0.1.json","content/staging/science-g2-unit-04-materials-draft-v0.1.json","content/staging/science-g2-unit-05-night-sky-draft-v0.1.json"];
const drafts=paths.map(x=>JSON.parse(fs.readFileSync(x,"utf8")));const all=drafts.flatMap(x=>x.activities);
assert.equal(all.length,72);assert.equal(new Set(all.map(x=>x.activityId)).size,72);
const lessons=new Map(p.lessons.map(x=>[x.lessonId,x]));const allowed=new Set(["DIAGNOSTIC","GUIDED","INDEPENDENT","TRANSFER","DELAYED_REVIEW"]);
for(const d of drafts){assert.equal(d.status,"INTEGRATED_PENDING_INDEPENDENT_QA");assert.equal(d.releaseGate.runtimeIntegrated,true);assert.equal(d.summary.runtimeMapped,d.activities.length);
 for(const x of d.activities){const a=p.activities.find(y=>y.activityId===x.activityId);assert.ok(a,"Missing runtime Science activity: "+x.activityId);assert.equal(a.bookId,"macmillan-science-level2");assert.equal(a.unitId,x.unitId);assert.equal(a.lessonId,x.lessonId);assert.equal(a.skillId,x.skillId);const l=lessons.get(x.lessonId);assert.ok(l&&l.unitId===x.unitId&&l.skillIds.includes(x.skillId));assert.ok(allowed.has(x.assessmentPhase));assert.ok(x.misconceptionCode);assert.equal(x.options.length,3);assert.equal(x.options.filter(o=>o.optionId===x.correctOptionId).length,1);assert.deepEqual(Object.keys(x.optionRationales).sort(),x.options.map(o=>o.optionId).sort());assert.ok(x.runtimeFeedbackCorrect.length<=32);assert.ok(x.runtimeFeedbackIncorrect.length<=48);assert.equal(a.assessmentPhase,x.assessmentPhase);assert.equal(a.feedbackCorrect,x.runtimeFeedbackCorrect);assert.equal(a.feedbackIncorrect,x.runtimeFeedbackIncorrect);}
}
const counts=Object.fromEntries([...new Set(all.map(x=>x.lessonId))].map(id=>[id,all.filter(x=>x.lessonId===id).length]));
assert.equal(Object.values(counts).filter(x=>x===8).length,5);assert.equal(Object.values(counts).filter(x=>x===5).length,4);assert.equal(Object.values(counts).filter(x=>x===4).length,3);
assert.equal(p.activities.filter(x=>x.bookId==="macmillan-science-level2").length,134);
console.log("Science Level 2: PASS (72 original AAS2 items mapped across all 12 lessons in Units 3–5; independent teacher QA remains pending).");
