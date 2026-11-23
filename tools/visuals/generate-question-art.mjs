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
  return [...String(html).matchAll(/<svg\\b[\\s\\S]*?<\\/svg>/g)].map(match => match[0]);
}

function makeQuestionSvg(activity, fragments) {
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
    const match = fragment.match(/^<svg\\b([^>]*)>/);
    if (!match) return "";
    const attrs = match[1].replace(/\\s+(?:x|y|width|height)="[^"]*"/g, "");
    const inner = fragment.slice(match[0].length, fragment.lastIndexOf("</svg>"));
    const x = 12 + (index % columns) * cellWidth;
    const y = 12 + Math.floor(index / columns) * cellHeight;
    return `<svg x="${x}" y="${y}" width="${cellWidth}" height="${cellHeight}" ${attrs}>${inner}</svg>`;
  }).join("");
  return `<svg xmlns="http://www.w3.org/2000/svg" width="${width}" height="${height}" viewBox="0 0 ${width} ${height}"><rect width="100%" height="100%" rx="20" fill="#f7fbff"/><rect x="1" y="1" width="${width-2}" height="${height-2}" rx="19" fill="none" stroke="#dce8f2" stroke-width="2"/>${nested}</svg>`;
}

const packFiles = fs.readdirSync(packDir).filter(name => name.endsWith(".json")).sort();
const records = [];
for (const filename of packFiles) {
  const pack = JSON.parse(fs.readFileSync(path.join(packDir, filename), "utf8"));
  for (const activity of pack.activities || []) {
    if (!activity.promptImageAssetPath) continue;
    if (activity.language !== "en") throw new Error(`Question art must be generated only for English activities: ${activity.activityId}`);
    const question = {
      id: activity.activityId,
      type: "mc_text",
      skill: "science plant life",
      prompt: activity.prompt,
      options: []
    };
    const html = visuals.render(question, {});
    const fragments = extractSvgFragments(html);
    const svg = makeQuestionSvg(activity, fragments);
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
const manifest = {
  schemaVersion: 1,
  generator: "Scholars Visual Learning Engine",
  engineVersion: visuals.version,
  outputFormat: "PNG",
  questionCount: records.length,
  files: records
};
fs.writeFileSync(path.join(outputRoot, "manifest.json"), JSON.stringify(manifest, null, 2) + "\\n");
for (const record of records) {
  const bytes = fs.readFileSync(path.join(assetRoot, record.path));
  if (bytes.length !== record.bytes || crypto.createHash("sha256").update(bytes).digest("hex") !== record.sha256) {
    throw new Error(`Generated question art integrity check failed: ${record.activityId}`);
  }
}
console.log(`Generated and SHA-256 validated ${records.length} unique offline question illustrations.`);
