import fs from "node:fs"; import assert from "node:assert/strict";
const p=JSON.parse(fs.readFileSync("app/src/main/assets/content-packs/aas2-grade2-foundation.json","utf8"));
const e=JSON.parse(fs.readFileSync("content/staging/english-world-level2-original-activity-drafts-v0.1.json","utf8"));
const m=JSON.parse(fs.readFileSync("content/staging/mathematics-books-original-activity-drafts-v0.1.json","utf8"));
const scienceFiles=["content/staging/science-g2-unit-03-senses-draft-v0.1.json","content/staging/science-g2-unit-04-materials-draft-v0.1.json","content/staging/science-g2-unit-05-night-sky-draft-v0.1.json"].map(x=>JSON.parse(fs.readFileSync(x,"utf8")));
assert.equal(p.version,"0.4.8");assert.equal(p.books.length,5);assert.equal(p.units.length,46);assert.equal(p.lessons.length,76);assert.equal(p.activities.length,367);
const lessonSkills=new Map();for(const lesson of p.lessons)for(const skill of lesson.skillIds){if(!lessonSkills.has(skill))lessonSkills.set(skill,[]);lessonSkills.get(skill).push(lesson.lessonId);}
const english=p.activities.filter(x=>x.activityId.startsWith("aas2-ew2-"));assert.equal(english.length,52);assert.equal(e.summary.runtimeMapped,52);
for(const x of e.records){const a=p.activities.find(y=>y.activityId===x.activityId);assert.ok(a,"English activity missing: "+x.activityId);assert.equal(a.skillId,x.skillId);assert.equal(lessonSkills.get(x.skillId)?.length,1);assert.equal(lessonSkills.get(x.skillId)[0],x.lessonId);}
for(const id of new Set(e.records.map(x=>x.lessonId)))assert.equal(e.records.filter(x=>x.lessonId===id).length,2);
const math=p.activities.filter(x=>x.activityId.startsWith("aas2-2a-")||x.activityId.startsWith("aas2-2b-")||x.activityId.startsWith("aas2-xpress2-"));assert.equal(math.length,63);assert.equal(m.summary.runtimeMapped,63);assert.equal(m.summary.stagingOnlyBookCount,0);assert.ok(m.companionPathOnlyBookIds.includes("maths-xpress-book-2"));
for(const x of m.records){assert.equal(x.runtimeEligible,true);assert.ok(x.lessonId);const a=p.activities.find(y=>y.activityId===x.activityId);assert.ok(a,"Math activity missing: "+x.activityId);assert.equal(a.skillId,x.skillId);assert.equal(lessonSkills.get(x.skillId)?.length,1);assert.equal(lessonSkills.get(x.skillId)[0],x.lessonId);assert.equal(x.lessonId,x.unitId+"-L01");}
const candidates=["aas2-aboudy-place-value","aas2-aboudy-addition-and-subtraction","aas2-aboudy-multiplication","aas2-aboudy-fractions-and-measure","aas2-aboudy-measure-and-apply","aas2-aboudy-data-handling"];
for(const s of candidates)assert.equal(lessonSkills.has(s),false,"Legacy candidate must remain Additional Practice: "+s);
const science=scienceFiles.flatMap(x=>x.activities);assert.equal(science.length,72);
for(const x of science){const a=p.activities.find(y=>y.activityId===x.activityId);assert.ok(a,"Science activity missing: "+x.activityId);assert.equal(a.bookId,"macmillan-science-level2");assert.equal(a.unitId,x.unitId);assert.equal(a.lessonId,x.lessonId);assert.equal(a.skillId,x.skillId);assert.equal(a.feedbackCorrect,x.runtimeFeedbackCorrect);assert.equal(a.feedbackIncorrect,x.runtimeFeedbackIncorrect);const l=p.lessons.find(y=>y.lessonId===x.lessonId);assert.ok(l&&l.skillIds.includes(x.skillId),"Science lesson-skill mismatch: "+x.activityId);}
assert.equal(p.activities.filter(x=>x.bookId==="macmillan-science-level2").length,134);
assert.equal(p.activities.filter(x=>x.activityId.startsWith("aas2-xpress2-")).length,15);
console.log("Grade 2 coverage: PASS (52 English World + 63 Mathematics + 72 Science Units 3–5 items mapped; legacy candidates remain supplementary; Maths Xpress Book 2 explicitly AAS2 companion path).");
