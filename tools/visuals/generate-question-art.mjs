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
  if (/counting-pictures|counting-to-five/.test(skill)) {
    return { type: "counting", skill: "math", textFirst: false };
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

function numberCardSvg(value) {
  const digits = String(value).trim();
  const segments = {
    "0": "abcdef", "1": "bc", "2": "abged", "3": "abgcd", "4": "fgbc",
    "5": "afgcd", "6": "afgecd", "7": "abc", "8": "abcdefg", "9": "abfgcd"
  };
  const segmentShape = {
    a: [5, 3, 16, 4], b: [21, 7, 4, 14], c: [21, 25, 4, 14],
    d: [5, 39, 16, 4], e: [1, 25, 4, 14], f: [1, 7, 4, 14], g: [5, 21, 16, 4]
  };
  const gap = 5;
  const digitWidth = 26;
  const totalWidth = digits.length * digitWidth + Math.max(0, digits.length - 1) * gap;
  const startX = (160 - totalWidth) / 2;
  const glyphs = [...digits].map((digit, index) => {
    const active = segments[digit] || "";
    const shapes = [...active].map(segment => {
      const [x, y, width, height] = segmentShape[segment];
      return `<rect x="${startX + index * (digitWidth + gap) + x}" y="${y + 30}" width="${width}" height="${height}" rx="2" fill="#24415c"/>`;
    }).join("");
    return shapes;
  }).join("");
  return `<svg xmlns="http://www.w3.org/2000/svg" width="160" height="112" viewBox="0 0 160 112"><rect x="3" y="3" width="154" height="106" rx="15" fill="#ffffff" stroke="#7da6c7" stroke-width="2"/><rect x="14" y="13" width="132" height="7" rx="3.5" fill="#d9eaf7"/>${glyphs}<circle cx="24" cy="91" r="4" fill="#48a987"/><circle cx="136" cy="91" r="4" fill="#d79a38"/></svg>`;
}


function countedObjectsSvg(kind, count) {
  const amount = Math.max(1, Math.min(20, Number(count) || 1));
  const source = visuals.svg(kind, kind);
  const opening = source.match(/^<svg\b[^>]*>/);
  const inner = opening
    ? source.slice(opening[0].length, source.lastIndexOf("</svg>")).replace(/<title>[\s\S]*?<\/title>/, "")
    : "";
  const columns = amount <= 4 ? amount : 5;
  const rows = Math.ceil(amount / columns);
  const cellWidth = 136 / columns;
  const cellHeight = 68 / rows;
  const size = Math.min(44, cellWidth * 0.82, cellHeight * 0.82);
  const objects = Array.from({ length: amount }, (_, index) => {
    const x = 12 + (index % columns) * cellWidth + (cellWidth - size) / 2;
    const y = 19 + Math.floor(index / columns) * cellHeight + (cellHeight - size) / 2;
    return `<g transform="translate(${x} ${y}) scale(${size / 100})">${inner}</g>`;
  }).join("");
  return `<svg xmlns="http://www.w3.org/2000/svg" width="160" height="112" viewBox="0 0 160 112"><rect x="3" y="3" width="154" height="106" rx="15" fill="#ffffff" stroke="#7da6c7" stroke-width="2"/>${objects}</svg>`;
}

const PNG_CRC_TABLE = new Uint32Array(256);
for (let index = 0; index < 256; index++) {
  let value = index;
  for (let bit = 0; bit < 8; bit++) value = (value & 1) ? (0xedb88320 ^ (value >>> 1)) : (value >>> 1);
  PNG_CRC_TABLE[index] = value >>> 0;
}

function crc32(buffer) {
  let value = 0xffffffff;
  for (const byte of buffer) value = PNG_CRC_TABLE[(value ^ byte) & 0xff] ^ (value >>> 8);
  return (value ^ 0xffffffff) >>> 0;
}

function pngTextChunk(keyword, value) {
  const type = Buffer.from("tEXt", "ascii");
  const data = Buffer.concat([
    Buffer.from(keyword, "latin1"),
    Buffer.from([0]),
    Buffer.from(value, "latin1")
  ]);
  const length = Buffer.alloc(4);
  length.writeUInt32BE(data.length, 0);
  const crcInput = Buffer.concat([type, data]);
  const checksum = Buffer.alloc(4);
  checksum.writeUInt32BE(crc32(crcInput), 0);
  return Buffer.concat([length, type, data, checksum]);
}

function addPngMetadata(png, activityId, visualProfile) {
  const signature = png.subarray(0, 8);
  const chunks = [];
  let offset = 8;
  while (offset + 12 <= png.length) {
    const length = png.readUInt32BE(offset);
    const type = png.toString("ascii", offset + 4, offset + 8);
    const end = offset + 12 + length;
    if (end > png.length) throw new Error("Malformed PNG chunk while adding question metadata.");
    if (type === "IEND") {
      const metadata = Buffer.concat([
        pngTextChunk("AAS2-Activity-ID", activityId),
        pngTextChunk("AAS2-Visual-Profile", visualProfile)
      ]);
      return Buffer.concat([signature, ...chunks, metadata, png.subarray(offset, end)]);
    }
    chunks.push(png.subarray(offset, end));
    offset = end;
  }
  throw new Error("PNG IEND chunk was not found.");
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
  return `<svg xmlns="http://www.w3.org/2000/svg" width="${width}" height="${height}" viewBox="0 0 ${width} ${height}"><rect width="100%" height="100%" rx="20" fill="#f7fbff"/><rect x="1" y="1" width="${width-2}" height="${height-2}" rx="19" fill="none" stroke="${accent}" stroke-width="3"/>${nested}</svg>`;
}

const packFiles = fs.readdirSync(packDir).filter(name => name.endsWith(".json")).sort();
const records = [];
for (const filename of packFiles) {
  const pack = JSON.parse(fs.readFileSync(path.join(packDir, filename), "utf8"));
  for (const activity of pack.activities || []) {
    if (!activity.promptImageAssetPath) continue;
    if (activity.language !== "en") throw new Error(`Question art must be generated only for English activities: ${activity.activityId}`);
    const profile = visualProfile(activity);
    const promptText = String(activity.prompt || "");
    const numberWords = { zero: 0, one: 1, two: 2, three: 3, four: 4, five: 5, six: 6, seven: 7, eight: 8, nine: 9, ten: 10 };
    const countMatch = promptText.match(/\b(?:shows?|has)\s+(\d{1,2}|zero|one|two|three|four|five|six|seven|eight|nine|ten)\s+([a-z-]+)/i);
    const numberToken = countMatch?.[1] || promptText.match(/\b\d{1,2}\b/)?.[0] || promptText.match(/\b(zero|one|two|three|four|five|six|seven|eight|nine|ten)\b/i)?.[0];
    const promptNumber = numberToken
      ? (Number.isFinite(Number(numberToken)) ? Number(numberToken) : (numberWords[numberToken.toLowerCase()] ?? 5))
      : 5;
    const promptKinds = visuals.findKinds(activity.prompt || "");
    const countedEntity = promptKinds.find(kind => !["number", "dot", "group"].includes(kind)) || "dot";
    const question = {
      id: activity.activityId,
      type: profile.type,
      skill: profile.skill,
      prompt: activity.prompt,
      options: (activity.options || []).map(option => option.text),
      legacyData: profile.type === "counting" ? { entities: [countedEntity], count: promptNumber } : {}
    };
    const promptFragments = profile.type === "counting"
      ? [countedObjectsSvg(countedEntity, promptNumber)]
      : profile.skill === "math"
        ? [mathPromptSvg(activity)]
        : profile.skill === "english-grammar"
          ? [wordCardSvg(activity.prompt)]
          : profile.type === "reading_comprehension"
            ? [visuals.svg("book", "reading")]
            : extractSvgFragments(visuals.render(question, {}));
    const optionFragments = (activity.options || []).map(option => {
      const optionText = String(option.text || "").trim();
      if (profile.textFirst) return wordCardSvg(optionText);
      if (profile.skill === "math" && !/shape/.test(String(activity.skillId || "").toLowerCase())) {
        if (/^\d{1,4}$/.test(optionText)) return numberCardSvg(optionText);
        return wordCardSvg(optionText);
      }
      const visualKind = visuals.optionVisualKind(question, optionText);
      if (visualKind === "word-card") return wordCardSvg(optionText);
      const optionHtml = visuals.renderOption(question, optionText);
      const rendered = extractSvgFragments(optionHtml)[0];
      if (rendered) return rendered;
      const kinds = visuals.findKinds(optionText);
      return kinds.length ? visuals.svg(kinds[0], optionText) : wordCardSvg(optionText);
    });
    const fragments = optionFragments.length ? optionFragments.concat(promptFragments) : promptFragments;
    const svg = makeQuestionSvg(activity, fragments);
    const renderedPng = new Resvg(svg, { fitTo: { mode: "width", value: 384 } }).render().asPng();
    const png = addPngMetadata(renderedPng, activity.activityId, profile.skill);
    const absolute = path.join(assetRoot, activity.promptImageAssetPath);
    fs.mkdirSync(path.dirname(absolute), { recursive: true });
    fs.writeFileSync(absolute, png);
    records.push({
      activityId: activity.activityId,
      path: activity.promptImageAssetPath,
      bytes: png.length,
      sha256: crypto.createHash("sha256").update(png).digest("hex"),
      engineVersion: visuals.version,
      skillId: activity.skillId,
      visualProfile: profile.skill,
      optionLabels: (activity.options || []).map(option => String(option.text || "")),
    });
  }
}
if (records.length === 0) throw new Error("No activities declare promptImageAssetPath.");
if (new Set(records.map(record => record.activityId)).size !== records.length) throw new Error("Duplicate activity IDs in generated art.");
if (new Set(records.map(record => record.path)).size !== records.length) throw new Error("Question image paths must be unique.");
if (new Set(records.map(record => record.sha256)).size !== records.length) throw new Error("Question illustration files must have distinct SHA-256 values; refusing duplicate asset bytes.");
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
