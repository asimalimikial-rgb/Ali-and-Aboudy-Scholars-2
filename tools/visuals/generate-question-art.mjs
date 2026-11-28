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
const optionRoot = path.join(assetRoot, "option-art");
function pruneGeneratedDirectory(directory, keepNames) {
  let removed = 0;
  for (const entry of fs.readdirSync(directory, { withFileTypes: true })) {
    if (!entry.isFile() || keepNames.has(entry.name)) continue;
    fs.unlinkSync(path.join(directory, entry.name));
    removed += 1;
  }
  return removed;
}

fs.mkdirSync(outputRoot, { recursive: true });
fs.mkdirSync(optionRoot, { recursive: true });

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
  if (/math|mm2a|mm2b|mx2|count|shape|place-value|addition|subtraction|multiplication|measure|fraction|number-pattern/.test(skill)) {
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

function phonicsPromptSvg(activity) {
  const prompt = String(activity.prompt || "");
  const rhyme = prompt.match(/\brhymes?\s+with\s+([a-z]+)/i);
  if (rhyme) {
    const kind = visuals.kindOf(rhyme[1]);
    return kind !== "generic" && visuals.hasSpecificVisual(kind) ? visuals.svg(kind, rhyme[1]) : wordCardSvg(rhyme[1]);
  }
  const phoneme = prompt.match(/\/([a-z]{1,2})\//i);
  const pattern = prompt.match(/\b([a-z]_[a-z])\b/i);
  const letter = (phoneme?.[1] || pattern?.[1] || "A").toUpperCase();
  return '<svg xmlns="http://www.w3.org/2000/svg" width="160" height="112" viewBox="0 0 160 112"><rect x="3" y="3" width="154" height="106" rx="15" fill="#fffaf0" stroke="#d7aa50" stroke-width="2"/><circle cx="80" cy="56" r="34" fill="#fff0c8" stroke="#d7aa50" stroke-width="3"/><text x="80" y="72" text-anchor="middle" font-family="Arial, sans-serif" font-size="' + (letter.length > 1 ? 32 : 48) + '" font-weight="800" fill="#815b1e">' + letter + '</text><path d="M23 43q-12 13 0 26m114-26q12 13 0 26M13 35q-18 21 0 42m134-42q18 21 0 42" fill="none" stroke="#5d9fbd" stroke-width="3" stroke-linecap="round"/></svg>';
}

function fallbackPromptFragments(activity, profile) {
  const prompt = String(activity.prompt || "").toLowerCase();
  const scene = kinds => kinds.map(kind => visuals.svg(kind, kind));
  if (profile.skill === "science") {
    if (/moon|crater|constellation|star|night sky/.test(prompt)) return scene(["moon", "star", "cloud"]);
    if (/float|sink/.test(prompt)) return scene(["boat", "water", "wood"]);
    if (/capacity|funnel|liquid|dissolv|salt|sugar|oil/.test(prompt)) return scene(["cup", "water", "glass", "spoon"]);
    if (/hot|stove|smoke|ice|melt/.test(prompt)) return scene(["stove", "sun", "ice"]);
    if (/sound|hearing|hear|pitch|loud|vibrat/.test(prompt)) return scene(["ear", "eye", "nose"]);
    if (/smell|nose/.test(prompt)) return scene(["nose", "flower", "lemon"]);
    if (/taste|tongue|lemon/.test(prompt)) return scene(["lemon", "apple", "person"]);
    if (/touch|texture|rough|smooth|skin/.test(prompt)) return scene(["wood", "glass", "cotton"]);
    return [wordCardSvg(activity.prompt)];
  }

  if (/blend|long [a-z] sound|magic-e|phonics/.test(prompt)) return [phonicsPromptSvg(activity)];
  if (/ends with the letters/.test(prompt)) return [wordCardSvg("ld")];
  if (/half past|what time/.test(prompt)) return [visuals.svg("clock", "clock")];
  if (/potato/.test(prompt)) return scene(["potato", "plant", "soil"]);
  if (/cotton/.test(prompt)) return scene(["cotton", "plant", "shirt"]);
  if (/bean/.test(prompt)) return scene(["bean", "seed", "plant"]);
  if (/herbivore/.test(prompt)) return scene(["rabbit", "goat", "plant"]);
  if (/circulation/.test(prompt)) return scene(["heart", "lungs"]);
  if (/hygiene/.test(prompt)) return scene(["toothbrush", "soap", "water"]);
  if (/solid|solids/.test(prompt)) return scene(["block", "rock", "ice"]);
  if (/mammal/.test(prompt)) return scene(["goat", "cow", "dog"]);
  if (/desert animal/.test(prompt)) return scene(["camel", "sun", "rock"]);
  if (/material/.test(prompt)) return scene(["wood", "glass", "metal", "plastic"]);
  if (/magnet/.test(prompt)) return scene(["magnet", "iron", "paper"]);
  if (/animal needs/.test(prompt)) return scene(["water", "food", "wind", "plant"]);
  if (/body parts/.test(prompt)) return scene(["eye", "ear", "nose", "tooth"]);
  if (/safety/.test(prompt)) return scene(["helmet", "stove", "person"]);
  if (/floating/.test(prompt)) return scene(["boat", "water", "wood", "stone"]);
  if (/melting/.test(prompt)) return scene(["ice", "water", "sun"]);
  if (/food chain/.test(prompt)) return scene(["sun", "plant", "rabbit", "fox"]);
  if (/life cycle/.test(prompt)) return /frog/.test(prompt) ? scene(["egg", "tadpole", "frog"]) : scene(["egg", "caterpillar", "pupa", "butterfly"]);
  if (/octopus/.test(prompt)) return scene(["octopus", "water"]);
  if (/spider/.test(prompt)) return scene(["spider", "insect"]);
  if (/predator|prey|camouflage/.test(prompt)) return scene(["fox", "rabbit", "insect"]);
  if (/vertebrate|invertebrate|backbone|animal grouping/.test(prompt)) return scene(["rib_cage", "frog", "spider"]);
  if (/photosynthesis/.test(prompt)) return scene(["sun", "plant", "water", "wind"]);
  if (/water cycle|evaporation/.test(prompt)) return scene(["sun", "water", "cloud", "rain"]);
  if (/day and night|earth rotation/.test(prompt)) return scene(["sun", "planet", "moon"]);
  if (/balanced diet|nutrition/.test(prompt)) return scene(["plate", "carrot", "milk", "bread"]);
  if (/freez|ice/.test(prompt)) return scene(["water", "ice"]);
  if (/heat conductor|conductors/.test(prompt)) return scene(["metal", "wood", "plastic"]);
  if (/gravity/.test(prompt)) return scene(["apple", "planet"]);
  if (/ramp|simple machine/.test(prompt)) return scene(["ramp", "pulley", "lever"]);
  if (/shadow/.test(prompt)) return scene(["sun", "person", "shadow"]);
  if (/environment|playground|clean|rubbish|litter/.test(prompt)) return scene(["bin", "tree", "plant"]);
  if (/recycl/.test(prompt)) return scene(["paper", "bin"]);
  if (/water conservation|clean water/.test(prompt)) return scene(["tap", "water", "plant"]);
  if (/skeleton|rib cage/.test(prompt)) return scene(["rib_cage", "heart", "lungs"]);
  if (/suitable habitat|habitat/.test(prompt)) return scene(["pond", "tree", "water", "sun"]);
  if (/animal is living|living animal|living thing/.test(prompt)) return scene(["fish", "bird", "plant"]);
  const promptKinds = visuals.findKinds(prompt).filter(kind => visuals.hasSpecificVisual(kind));
  if (promptKinds.length) return scene(promptKinds.slice(0, 4));
  if (profile.skill === "vocabulary") return [wordCardSvg(activity.prompt)];
  if (profile.skill === "reading") return [readingPromptSvg()];
  if (profile.skill === "math") return [mathPromptSvg(activity)];
  if (profile.skill === "english") return [wordCardSvg(activity.prompt)];
  if (profile.skill === "phonics") return [phonicsPromptSvg(activity)];
  return [];
}
function optionVisualFragments(question, option) {
  const label = String(option.text || "").trim();
  if (!label) return [];
  if (/^\d{1,3}$/.test(label)) return [numberCardSvg(label)];
  if (/^\d{1,2}:\d{2}$/.test(label)) return [visuals.svg("clock", label)];
  const visualKind = visuals.optionVisualKind(question, label);
  if (visualKind === "word-card") return [];
  const html = visuals.renderOption(question, label);
  const rendered = extractSvgFragments(html).filter(fragment => {
    const kind = fragment.match(/data-visual-icon-kind="([^"]+)"/)?.[1];
    return !kind || visuals.hasSpecificVisual(kind);
  });
  if (rendered.length) return rendered.slice(0, 3);
  return visuals.findKinds(label).filter(kind => visuals.hasSpecificVisual(kind)).slice(0, 3).map(kind => visuals.svg(kind, label));
}

function mathPromptSvg(activity) {
  const prompt = String(activity.prompt || "").toLowerCase();
  const numbers = [...prompt.matchAll(/\b\d{1,3}\b/g)].map(match => match[0]);
  const shell = inner => '<svg xmlns="http://www.w3.org/2000/svg" width="320" height="150" viewBox="0 0 320 150"><rect x="2" y="2" width="316" height="146" rx="18" fill="#f7fbff" stroke="#8aaec2" stroke-width="3"/>' + inner + '</svg>';
  if (/pattern/.test(prompt) && numbers.length) {
    const cells = numbers.slice(0, 4).map(n => '<rect x="18" y="38" width="54" height="58" rx="9" fill="#fff" stroke="#6c9ab3" stroke-width="2"/><text x="45" y="77" text-anchor="middle" font-size="27" font-weight="700" fill="#24485c">' + n + '</text>').join("");
    return shell(cells + '<path d="M76 67h14m-5-5 5 5-5 5M132 67h14m-5-5 5 5-5 5M188 67h14m-5-5 5 5-5 5" stroke="#6c9ab3" stroke-width="3" fill="none"/><rect x="246" y="38" width="54" height="58" rx="9" fill="#fff3cf" stroke="#d3a33e" stroke-width="2"/><text x="273" y="77" text-anchor="middle" font-size="30" font-weight="700" fill="#9a6a16">?</text>');
  }
  if (/calculate|add|plus|subtract|minus/.test(prompt) && numbers.length >= 2) {
    const sign = /plus|add|\+/.test(prompt) ? "+" : "−";
    return shell('<text x="160" y="92" text-anchor="middle" font-size="48" font-weight="800" fill="#24485c">' + numbers[0] + " " + sign + " " + numbers[1] + " = ?" + '</text>');
  }
  if (/pentagon/.test(prompt)) return visuals.svg("pentagon", "pentagon");
  if (/hexagon/.test(prompt)) return visuals.svg("hexagon", "hexagon");
  if (/straight sides|sides/.test(prompt)) return shell('<polygon points="70,18 120,50 100,112 40,112 20,50" fill="#f2c34e" stroke="#9a742f" stroke-width="4"/><text x="205" y="82" text-anchor="middle" font-size="42" font-weight="800" fill="#24485c">?</text>');
  if (/mass|weigh|weight/.test(prompt)) return visuals.svg("scale", "scale");
  if (/clock|time|hour|minutes/.test(prompt)) return visuals.svg("clock", "clock");
  if (/fraction|half|quarter|equal parts/.test(prompt)) return visuals.svg("fraction", "fraction");
  if (/centimet|metre|length|ruler/.test(prompt)) return visuals.svg("ruler", "ruler");
  if (/money|pounds|piasters|cost|change/.test(prompt)) return visuals.svg("coin", "coin");
  if (/place value|hundreds|tens|ones|digit/.test(prompt)) return numbers.length ? numberCardSvg(numbers[0]) : visuals.svg("number", "number");
  if (/groups|times|multiply|multiplication/.test(prompt)) return visuals.svg("array", "array");
  return numbers.length ? numberCardSvg(numbers[0]) : visuals.svg("number", "number");
}

function languageTaskSvg(activity) {
  const prompt = String(activity.prompt || "").toLowerCase();
  const shell = inner => '<svg xmlns="http://www.w3.org/2000/svg" width="320" height="150" viewBox="0 0 320 150"><rect x="2" y="2" width="316" height="146" rx="18" fill="#fffaf0" stroke="#d4b36a" stroke-width="3"/>' + inner + '</svg>';
  if (/sentence.*order|words.*correct order|word order/.test(prompt)) return shell('<rect x="24" y="48" width="70" height="40" rx="8" fill="#b9dff0"/><rect x="125" y="48" width="70" height="40" rx="8" fill="#f7d28b"/><rect x="226" y="48" width="70" height="40" rx="8" fill="#bfe2b5"/><path d="M98 68h22m-6-6 6 6-6 6m82-6h22m-6-6 6 6-6 6" stroke="#54788d" stroke-width="4" fill="none"/>');
  if (/opposite/.test(prompt)) return shell('<path d="M45 75h95m-15-15 15 15-15 15M275 75h-95m15-15-15 15 15 15" stroke="#477e98" stroke-width="9" fill="none" stroke-linecap="round" stroke-linejoin="round"/>');
  if (/past tense|yesterday/.test(prompt)) return shell('<path d="M55 82h210m-12-12 12 12-12 12" stroke="#477e98" stroke-width="5" fill="none"/><circle cx="90" cy="82" r="15" fill="#f4c56a"/><circle cx="230" cy="82" r="15" fill="#8bc6df"/><text x="90" y="42" text-anchor="middle" font-size="19" fill="#74531e">Past</text><text x="230" y="42" text-anchor="middle" font-size="19" fill="#315b70">Now</text>');
  if (/punctuation|punctuated|question mark/.test(prompt)) return shell('<text x="70" y="108" font-size="82" font-weight="800" fill="#477e98">?</text><text x="145" y="108" font-size="82" font-weight="800" fill="#e4a33f">!</text><text x="220" y="108" font-size="82" font-weight="800" fill="#6aa56c">.</text><text x="280" y="108" font-size="82" font-weight="800" fill="#a879d6">,</text>');
  if (/city|place/.test(prompt)) return shell('<path d="M25 120V65h45v55m10 0V38h50v82m12 0V70h44v50m10 0V52h60v68" fill="#8bc6df" stroke="#477e98" stroke-width="3"/><path d="M40 78h12m-12 15h12m43-37h15m-15 18h15m-15 18h15m30-4h12m-12 17h12m33-37h16m-16 18h16" stroke="#fff" stroke-width="4"/>');
  if (/turtle|adverb|how the .* moves/.test(prompt)) return visuals.svg("turtle", "turtle");
  if (/contraction|do not/.test(prompt)) return shell('<rect x="28" y="48" width="100" height="48" rx="9" fill="#b9dff0"/><rect x="190" y="48" width="100" height="48" rx="9" fill="#f7d28b"/><path d="M132 72h50m-12-12 12 12-12 12" stroke="#54788d" stroke-width="4" fill="none"/><text x="160" y="36" text-anchor="middle" font-size="28" font-weight="700" fill="#477e98">’</text>');
  if (/synonym|same meaning/.test(prompt)) return shell('<rect x="28" y="48" width="100" height="48" rx="9" fill="#b9dff0"/><rect x="190" y="48" width="100" height="48" rx="9" fill="#bfe2b5"/><text x="160" y="86" text-anchor="middle" font-size="42" font-weight="800" fill="#477e98">=</text>');
  if (/comma|list/.test(prompt)) return shell('<circle cx="62" cy="70" r="18" fill="#f2c34e"/><circle cx="160" cy="70" r="18" fill="#8bc6df"/><circle cx="258" cy="70" r="18" fill="#8dc98c"/><text x="111" y="86" font-size="42" fill="#477e98">,</text><text x="209" y="86" font-size="42" fill="#477e98">,</text>');
  if (/main idea|story/.test(prompt)) return shell('<path d="M42 42q38-15 76 0v70q-38-15-76 0Zm76 0q38-15 76 0v70q-38-15-76 0Z" fill="#fff" stroke="#477e98" stroke-width="4"/><circle cx="235" cy="62" r="22" fill="none" stroke="#d49b3e" stroke-width="7"/><path d="m251 79 24 24" stroke="#d49b3e" stroke-width="8" stroke-linecap="round"/>');
  if (/possessive/.test(prompt)) return shell('<rect x="60" y="48" width="150" height="55" rx="10" fill="#b9dff0"/><text x="235" y="89" font-size="50" font-weight="800" fill="#477e98">’s</text>');
  if (/joins ideas|reason|because/.test(prompt)) return shell('<circle cx="75" cy="70" r="30" fill="#b9dff0"/><circle cx="245" cy="70" r="30" fill="#f7d28b"/><path d="M108 70h104m-14-12 14 12-14 12" stroke="#54788d" stroke-width="5" fill="none"/>');
  if (/future/.test(prompt)) return shell('<rect x="100" y="28" width="120" height="92" rx="9" fill="#fff" stroke="#477e98" stroke-width="4"/><path d="M100 50h120M130 18v22m60-22v22" stroke="#477e98" stroke-width="5"/><path d="M135 85h70m-15-15 15 15-15 15" stroke="#d49b3e" stroke-width="5" fill="none"/>');
  if (/prefix|un-/.test(prompt)) return shell('<rect x="38" y="48" width="94" height="50" rx="9" fill="#f7d28b"/><rect x="188" y="48" width="94" height="50" rx="9" fill="#b9dff0"/><text x="85" y="82" text-anchor="middle" font-size="30" font-weight="800" fill="#74531e">un-</text><path d="M140 73h40m-12-12 12 12-12 12" stroke="#54788d" stroke-width="4" fill="none"/>');
  if (/sea|see|sounds like/.test(prompt)) return shell('<path d="M20 100q30-25 60 0t60 0 60 0 60 0" fill="none" stroke="#4c9fc7" stroke-width="8"/><path d="M225 38q30-20 55 8-25 28-55 8-10-8 0-16Z" fill="#fff" stroke="#477e98" stroke-width="4"/><circle cx="250" cy="54" r="7" fill="#477e98"/>');
  if (/complete:|___|blank/.test(prompt)) return shell('<rect x="25" y="55" width="270" height="40" rx="8" fill="#fff" stroke="#477e98" stroke-width="3"/><path d="M40 75h65m20 0h55m20 0h75" stroke="#8aaec2" stroke-width="3" stroke-linecap="round"/><rect x="128" y="63" width="48" height="24" rx="4" fill="#f7d28b"/>');
  if (/instruction/.test(prompt)) return shell('<path d="M75 112V42m-18 18 18-18 18 18" stroke="#477e98" stroke-width="8" fill="none" stroke-linecap="round" stroke-linejoin="round"/><rect x="140" y="42" width="135" height="62" rx="10" fill="#f7d28b" stroke="#bd8c35" stroke-width="3"/><path d="M170 74h75m-15-15 15 15-15 15" stroke="#815b1e" stroke-width="5" fill="none"/>');
  return wordCardSvg(activity.prompt);
}

function readingPromptSvg() {
  return '<svg xmlns="http://www.w3.org/2000/svg" width="320" height="150" viewBox="0 0 320 150"><rect x="2" y="2" width="316" height="146" rx="18" fill="#f7fbff" stroke="#8aaec2" stroke-width="3"/><path d="M35 36q42-18 84 0v82q-42-18-84 0Zm84 0q42-18 84 0v82q-42-18-84 0Z" fill="#fff" stroke="#477e98" stroke-width="4"/><circle cx="235" cy="62" r="24" fill="none" stroke="#d49b3e" stroke-width="7"/><path d="m253 81 28 28" stroke="#d49b3e" stroke-width="9" stroke-linecap="round"/></svg>';
}

function makeQuestionSvg(activity, fragments) {
  const selected = fragments.slice(0, 4);
  if (selected.length === 0) throw new Error(`No semantically relevant visual fragments for ${activity.activityId}.`);
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
  return `<svg xmlns="http://www.w3.org/2000/svg" width="${width}" height="${height}" viewBox="0 0 ${width} ${height}" role="img" aria-label="${escapeXml(activity.activityId)}" data-activity-id="${escapeXml(activity.activityId)}"><metadata>AAS2|${escapeXml(activity.activityId)}</metadata><rect width="100%" height="100%" rx="20" fill="#f7fbff"/><rect x="1" y="1" width="${width-2}" height="${height-2}" rx="19" fill="none" stroke="${accent}" stroke-width="3"/>${nested}</svg>`;
}

const packFiles = fs.readdirSync(packDir).filter(name => name.endsWith(".json")).sort();
const records = [];
const optionRecords = new Map();
for (const filename of packFiles) {
  const packPath = path.join(packDir, filename);
  const pack = JSON.parse(fs.readFileSync(packPath, "utf8"));
  let packChanged = false;
  for (const activity of pack.activities || []) {
    if (activity.language !== "en") continue;
    const profile = visualProfile(activity);
    const promptText = String(activity.prompt || "");
    const numberWords = { zero: 0, one: 1, two: 2, three: 3, four: 4, five: 5, six: 6, seven: 7, eight: 8, nine: 9, ten: 10 };
    const countMatch = promptText.match(/\b(?:shows?|has)\s+(\d{1,2}|zero|one|two|three|four|five|six|seven|eight|nine|ten)\s+([a-z-]+)/i);
    const numberToken = countMatch?.[1] || promptText.match(/\b\d{1,2}\b/)?.[0] || promptText.match(/\b(zero|one|two|three|four|five|six|seven|eight|nine|ten)\b/i)?.[0];
    const promptNumber = numberToken
      ? (Number.isFinite(Number(numberToken)) ? Number(numberToken) : (numberWords[numberToken.toLowerCase()] ?? 5))
      : 5;
    const promptKinds = visuals.findKinds(promptText).filter(kind => visuals.hasSpecificVisual(kind));
    const countedEntity = promptKinds.find(kind => !["number", "dot", "group"].includes(kind));
    if (profile.type === "counting" && !countedEntity) {
      throw new Error("Counting activity has no identifiable object: " + activity.activityId + " — " + promptText);
    }
    const question = {
      id: activity.activityId,
      type: profile.type,
      skill: profile.skill,
      prompt: activity.prompt,
      options: (activity.options || []).map(option => option.text),
      legacyData: profile.type === "counting" ? { entities: [countedEntity], count: promptNumber } : {}
    };
    let promptFragments = profile.type === "counting"
      ? [countedObjectsSvg(countedEntity, promptNumber)]
      : profile.skill === "phonics"
        ? [phonicsPromptSvg(activity)]
        : profile.skill === "math"
          ? [mathPromptSvg(activity)]
          : (profile.skill === "english-grammar" || /sentence.*order|words.*correct order|word order|opposite|past tense|yesterday|punctuation|punctuated|question mark|city|place|turtle|adverb|contraction|do not|synonym|same meaning|comma|list|main idea|story|possessive|joins ideas|reason|because|future|prefix|sea|see|sounds like|complete:|___|blank|instruction/i.test(promptText))
            ? [languageTaskSvg(activity)]
            : profile.skill === "reading"
              ? [readingPromptSvg()]
              : fallbackPromptFragments(activity, profile);
    promptFragments = promptFragments.filter(fragment => {
      const kind = fragment.match(/data-visual-icon-kind="([^"]+)"/)?.[1];
      return !kind || visuals.hasSpecificVisual(kind);
    });
    if (!promptFragments.length) promptFragments = fallbackPromptFragments(activity, profile);
    if (!promptFragments.length) {
      throw new Error("No relevant question illustration found for " + activity.activityId + ": " + promptText);
    }

    const optionAssetPaths = [];
    for (const option of activity.options || []) {
      const optionFragments = optionVisualFragments(question, option);
      if (!optionFragments.length) {
        if (option.imageAssetPath) {
          delete option.imageAssetPath;
          packChanged = true;
        }
        continue;
      }
      const optionKey = crypto.createHash("sha256").update(profile.skill + "|" + optionFragments.join("")).digest("hex").slice(0, 24);
      const optionPath = "option-art/" + optionKey + ".svg";
      const optionSvg = makeQuestionSvg({ activityId: "option-" + optionKey }, optionFragments);
      const optionAbsolute = path.join(assetRoot, optionPath);
      fs.mkdirSync(path.dirname(optionAbsolute), { recursive: true });
      fs.writeFileSync(optionAbsolute, optionSvg);
      const optionBytes = Buffer.from(optionSvg, "utf8");
      const previous = optionRecords.get(optionPath);
      optionRecords.set(optionPath, {
        path: optionPath,
        bytes: optionBytes.length,
        sha256: crypto.createHash("sha256").update(optionBytes).digest("hex"),
        labels: [...(previous?.labels || []), String(option.text || "")]
      });
      if (option.imageAssetPath !== optionPath) {
        option.imageAssetPath = optionPath;
        packChanged = true;
      }
      optionAssetPaths.push(optionPath);
    }

    const format = profile.textFirst || profile.type === "reading_comprehension" ? "png" : "svg";
    const targetPath = "question-art/" + activity.activityId + "." + format;
    if (activity.promptImageAssetPath !== targetPath) {
      activity.promptImageAssetPath = targetPath;
      packChanged = true;
    }
    const svg = makeQuestionSvg(activity, promptFragments);
    const absolute = path.join(assetRoot, targetPath);
    fs.mkdirSync(path.dirname(absolute), { recursive: true });
    let bytes;
    if (format === "png") {
      const renderedPng = new Resvg(svg, { fitTo: { mode: "width", value: 384 } }).render().asPng();
      bytes = addPngMetadata(renderedPng, activity.activityId, profile.skill);
    } else {
      bytes = Buffer.from(svg, "utf8");
    }
    fs.writeFileSync(absolute, bytes);
    records.push({
      activityId: activity.activityId,
      path: targetPath,
      format,
      bytes: bytes.length,
      sha256: crypto.createHash("sha256").update(bytes).digest("hex"),
      engineVersion: visuals.version,
      skillId: activity.skillId,
      visualProfile: profile.skill,
      matchedPromptKinds: promptKinds,
      optionImagePaths: optionAssetPaths,
      optionLabels: (activity.options || []).map(option => String(option.text || ""))
    });
  }
  if (packChanged) fs.writeFileSync(packPath, JSON.stringify(pack, null, 2) + "\n", "utf8");
}
if (records.length === 0) throw new Error("No English activities were found for question-art generation.");
if (new Set(records.map(record => record.activityId)).size !== records.length) throw new Error("Duplicate activity IDs in generated art.");
if (new Set(records.map(record => record.path)).size !== records.length) throw new Error("Question image paths must be unique.");
if (new Set(records.map(record => record.sha256)).size !== records.length) throw new Error("Question illustration files must have distinct SHA-256 values; refusing duplicate asset bytes.");
const optionFiles = [...optionRecords.values()];
const manifest = {
  schemaVersion: 2,
  generator: "Scholars Visual Learning Engine",
  engineVersion: visuals.version,
  outputFormat: "Mixed SVG and PNG",
  questionCount: records.length,
  optionImageCount: optionFiles.length,
  formats: records.reduce((result, record) => {
    result[record.format] = (result[record.format] || 0) + 1;
    return result;
  }, {}),
  files: records
};
fs.writeFileSync(path.join(outputRoot, "manifest.json"), JSON.stringify(manifest, null, 2) + "\n");
fs.writeFileSync(path.join(optionRoot, "manifest.json"), JSON.stringify({
  schemaVersion: 1,
  generator: "Scholars Visual Learning Engine",
  engineVersion: visuals.version,
  count: optionFiles.length,
  files: optionFiles
}, null, 2) + "\n");
const removedQuestionFiles = pruneGeneratedDirectory(
  outputRoot,
  new Set([...records.map(record => path.basename(record.path)), "manifest.json"])
);
const removedOptionFiles = pruneGeneratedDirectory(
  optionRoot,
  new Set([...optionFiles.map(record => path.basename(record.path)), "manifest.json"])
);
for (const record of records.concat(optionFiles)) {
  const bytes = fs.readFileSync(path.join(assetRoot, record.path));
  if (bytes.length !== record.bytes || crypto.createHash("sha256").update(bytes).digest("hex") !== record.sha256) {
    throw new Error("Generated visual asset integrity check failed: " + record.path);
  }
  if (record.path.endsWith(".svg")) {
    const svgText = bytes.toString("utf8");
    if (!svgText.trimStart().startsWith("<svg") || !svgText.includes("</svg>") || /(?:href|src)=["'](?:https?:|file:)/i.test(svgText)) {
      throw new Error("Invalid or non-offline SVG asset: " + record.path);
    }
  } else if (!bytes.subarray(0, 8).equals(Buffer.from([0x89,0x50,0x4e,0x47,0x0d,0x0a,0x1a,0x0a]))) {
    throw new Error("Invalid PNG asset: " + record.path);
  }
}
console.log("Generated and SHA-256 validated " + records.length + " question illustrations in " +
  Object.entries(manifest.formats).map(([format, count]) => count + " " + format.toUpperCase()).join(", ") +
  " plus " + optionFiles.length + " reusable option illustrations. " +
  "Removed " + removedQuestionFiles + " stale question files and " + removedOptionFiles + " stale option files.");