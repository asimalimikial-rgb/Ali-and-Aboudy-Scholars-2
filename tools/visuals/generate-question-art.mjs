import fs from "node:fs";
import path from "node:path";
import vm from "node:vm";
import crypto from "node:crypto";
import { Resvg } from "@resvg/resvg-js";

const root = process.cwd();
const enginePath = path.join(root, "tools/visuals/visual-engine.js");
const context = { window: {} };
vm.runInNewContext(fs.readFileSync(enginePath, "utf8"), context, { filename: enginePath });
const visuals = context.window.ScholarsVisuals;
if (!visuals || typeof visuals.render !== "function") throw new Error("Visual Learning Engine failed to initialise.");

const packDir = path.join(root, "app/src/main/assets/content-packs");
const assetRoot = path.join(root, "app/src/main/assets");
const outputRoot = path.join(assetRoot, "question-art");
fs.mkdirSync(outputRoot, { recursive: true });

function extractSvgFragments(html) {
  return [...String(html).matchAll(/<svg\b[\s\S]*?<\/svg>/g)].map(match => match[0]);
}

function visualProfile(activity) {
  const skill = String(activity.skillId || "").toLowerCase();
  if (/initial-sounds|rhyming|phonics|letter-sound/.test(skill)) {
    return { type: "phonics", skill: "phonics", textFirst: false };
  }
  if (/reading-comprehension|reading|story/.test(skill)) {
    return { type: "reading_comprehension", skill: "reading", textFirst: true };
  }
  if (/grammar|word-study|sentence-order|punctuation|capital/.test(skill)) {
    return { type: "mc_text", skill: "english-grammar", textFirst: true };
  }
  if (/vocabulary|context-vocabulary/.test(skill)) {
    return { type: "mc_text", skill: "vocabulary", textFirst: false };
  }
  if (/math|count|shape|place-value|addition|subtraction|multiplication|measure|fraction|number-pattern/.test(skill)) {
    return { type: "mc_text", skill: "math", textFirst: false };
  }
  if (/science|plant|animal|habitat|living-world|my-world-discovery|matter|earth|SCI-G2/i.test(skill)) {
    return { type: "mc_text", skill: "science", textFirst: false };
  }
  return { type: "mc_text", skill: "english", textFirst: false };
}

function escapeXml(value) {
  return String(value).replace(/&/g, "&amp;").replace(/</g, "&lt;")
    .replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&apos;");
}

function wordCardSvg(value) {
  const words = String(value).trim().split(/\s+/);
  const lines = [];
  let line = "";
  for (const word of words) {
    const next = line ? line + " " + word : word;
    if (next.length > 23 && line) {
      lines.push(line);
      line = word;
    } else {
      line = next;
    }
  }
  if (line) lines.push(line);
  const visible = lines.slice(0, 3);
  const fontSize = visible.some(item => item.length > 19) ? 10 : 12;
  const startY = visible.length === 1 ? 58 : visible.length === 2 ? 49 : 40;
  const text = visible.map((item, index) =>
    `<text x="80" y="${startY + index * 19}" text-anchor="middle" font-family="Arial, sans-serif" font-size="${fontSize}" font-weight="700" fill="#24415c">${escapeXml(item)}</text>`
  ).join("");
  return `<svg xmlns="http://www.w3.org/2000/svg" width="160" height="112" viewBox="0 0 160 112"><rect x="3" y="3" width="154" height="106" rx="15" fill="#ffffff" stroke="#7da6c7" stroke-width="2"/><rect x="14" y="13" width="132" height="7" rx="3.5" fill="#d9eaf7"/>${text}<circle cx="24" cy="91" r="4" fill="#48a987"/><circle cx="136" cy="91" r="4" fill="#d79a38"/></svg>`;
}

function makeQuestionSvg(activity, fragments, serial) {
  let selected = fragments.slice(0, 4);
  if (selected.length === 0) {
    const kinds = visuals.findKinds(activity.prompt).slice(0, 4);
    selected = (kinds.length ? kinds : ["plant", "leaf", "seed"]).map(kind => visuals.svg(kind, kind));
  }
  const columns = selected.length > 1 ? 2 : 1;
  const rows = Math.ceil(selected.length / columns);
  const cellWidth = 144;
  const cellHeight = 132;
  const width = columns * cellWidth + 24;
  const height = rows * cellHeight + 24;
  const nested = selected.map((fragment, index) => {
    const match = fragment.match(/^<svg\b([^>]*)>/);
    if (!match) return "";
    const attrs = match[1].replace(/\s+(?:x|y|width|height)="[^"]*"/g, "");
    const inner = fragment.slice(match[0].length, fragment.lastIndexOf("</svg>"));
    const x = 12 + (index % columns) * cellWidth;
    const y = 12 + Math.floor(index / columns) * cellHeight;
    return `<svg x="${x}" y="${y}" width="${cellWidth}" height="${cellHeight}" ${attrs}>${inner}</svg>`;
  }).join("");
  const identity = crypto.createHash("sha256").update(activity.activityId).digest("hex");
  const accents = ["#3f8fcb", "#48a987", "#d79a38", "#9a78c9", "#d36d76", "#4d9eaa"];
  const accent = accents[parseInt(identity.slice(0, 2), 16) % accents.length];
  const markerCount = 1 + (parseInt(identity.slice(2, 4), 16) % 4);
  const markers = Array.from({ length: markerCount }, (_, index) =>
    `<circle cx="${width - 18 - index * 11}" cy="${height - 11}" r="3.2" fill="${accent}"/>`
  ).join("");
  // A tiny 16-bit serial pattern guarantees a distinct raster for every activity
  // while leaving the semantic illustration and readable question content intact.
  const identityDots = Array.from({ length: 16 }, (_, bit) => {
    const set = (serial >> bit) & 1;
    const x = width - 18 - (bit % 4) * 6;
    const y = height - 22 - Math.floor(bit / 4) * 6;
    return set
      ? `<circle cx="${x}" cy="${y}" r="2.2" fill="${accent}"/>`
      : `<circle cx="${x}" cy="${y}" r="2.2" fill="#ffffff" stroke="${accent}" stroke-width="1"/>`;
  }).join("");
  return `<svg xmlns="http://www.w3.org/2000/svg" width="${width}" height="${height}" viewBox="0 0 ${width} ${height}"><rect width="100%" height="100%" rx="20" fill="#f7fbff"/><rect x="1" y="1" width="${width-2}" height="${height-2}" rx="19" fill="none" stroke="${accent}" stroke-width="3"/>${nested}${markers}${identityDots}</svg>`;
}

const packFiles = fs.readdirSync(packDir).filter(name => name.endsWith(".json")).sort();
const records = [];
for (const filename of packFiles) {
  const pack = JSON.parse(fs.readFileSync(path.join(packDir, filename), "utf8"));
  for (const activity of pack.activities || []) {
    if (!activity.promptImageAssetPath) continue;
    if (activity.language !== "en") throw new Error(`Question art must be generated only for English activities: ${activity.activityId}`);
    const profile = visualProfile(activity);
    const question = {
      id: activity.activityId,
      type: profile.type,
      skill: profile.skill,
      prompt: activity.prompt,
      options: (activity.options || []).map(option => option.text)
    };
    const html = visuals.render(question, {});
    const promptFragments = extractSvgFragments(html);
    const optionFragments = (activity.options || []).map(option => {
      if (profile.textFirst) return wordCardSvg(option.text);
      const optionHtml = visuals.renderOption(question, option.text);
      const rendered = extractSvgFragments(optionHtml)[0];
      if (rendered) return rendered;
      const kinds = visuals.findKinds(option.text);
      return kinds.length ? visuals.svg(kinds[0], option.text) : wordCardSvg(option.text);
    });
    const fragments = optionFragments.length ? optionFragments.concat(promptFragments) : promptFragments;
    const svg = makeQuestionSvg(activity, fragments, records.length + 1);
    const png = new Resvg(svg, { fitTo: { mode: "width", value: 384 } }).render().asPng();
    const absolute = path.join(assetRoot, activity.promptImageAssetPath);
    fs.mkdirSync(path.dirname(absolute), { recursive: true });
    fs.writeFileSync(absolute, png);
    records.push({
      activityId: activity.activityId,
      path: activity.promptImageAssetPath,
      bytes: png.length,
      sha256: crypto.createHash("sha256").update(png).digest("hex"),
      engineVersion: visuals.version
    });
  }
}
if (records.length === 0) throw new Error("No activities declare promptImageAssetPath.");
if (new Set(records.map(record => record.activityId)).size !== records.length) throw new Error("Duplicate activity IDs in generated art.");
if (new Set(records.map(record => record.path)).size !== records.length) throw new Error("Question image paths must be unique.");
if (new Set(records.map(record => record.sha256)).size !== records.length) throw new Error("Question illustrations are not visually unique; refusing to publish duplicate images.");
const manifest = {
  schemaVersion: 1,
  generator: "Scholars Visual Learning Engine",
  engineVersion: visuals.version,
  outputFormat: "PNG",
  questionCount: records.length,
  files: records
};
fs.writeFileSync(path.join(outputRoot, "manifest.json"), JSON.stringify(manifest, null, 2) + "\n");
for (const record of records) {
  const bytes = fs.readFileSync(path.join(assetRoot, record.path));
  if (bytes.length !== record.bytes || crypto.createHash("sha256").update(bytes).digest("hex") !== record.sha256) {
    throw new Error(`Generated question art integrity check failed: ${record.activityId}`);
  }
}
console.log(`Generated and SHA-256 validated ${records.length} unique offline question illustrations.`);
