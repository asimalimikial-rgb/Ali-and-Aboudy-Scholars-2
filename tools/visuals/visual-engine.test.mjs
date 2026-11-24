import assert from "node:assert/strict";
import fs from "node:fs";
import vm from "node:vm";

const context = { window: {} };
vm.runInNewContext(
  fs.readFileSync("tools/visuals/visual-engine.js", "utf8"),
  context,
  { filename: "visual-engine.js" }
);
const visuals = context.window.ScholarsVisuals;
assert.ok(visuals, "visual engine should initialise");

const waterAndSun = visuals.renderOption(
  { prompt: "Which pair helps a plant grow?", type: "mc_text" },
  "Water and sunlight"
);
assert.match(waterAndSun, /aria-label="sun"/);
assert.match(waterAndSun, /aria-label="water"/);
assert.doesNotMatch(waterAndSun, /aria-label="planet"/);

const rootsClaim = visuals.renderOption(
  { prompt: "Which statement about plants is correct?", type: "mc_text" },
  "Roots make sunlight"
);
assert.match(rootsClaim, /aria-label="roots"/);
assert.match(rootsClaim, /aria-label="sun"/);

const plantNegation = visuals.renderOption(
  { prompt: "Which statement about plants is correct?", type: "mc_text" },
  "Plants do not need sunlight"
);
assert.match(plantNegation, /data-option-visual-kind="negation"/);

const moonDaylight = visuals.renderOption(
  { prompt: "Which statement is correct?", type: "mc_text" },
  "The Moon gives Earth daylight"
);
assert.match(moonDaylight, /aria-label="moon"/);
assert.match(moonDaylight, /aria-label="planet"/);
assert.doesNotMatch(moonDaylight, /aria-label="sun"/);

const sunDaylight = visuals.renderOption(
  { prompt: "Which statement is correct?", type: "mc_text" },
  "The Sun gives Earth daylight"
);
assert.match(sunDaylight, /aria-label="sun"/);
assert.match(sunDaylight, /aria-label="planet"/);

console.log("Visual option semantics passed.");
