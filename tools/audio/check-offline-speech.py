#!/usr/bin/env python3
"""Check cached English TTS files before downloading the model or regenerating audio."""
from __future__ import annotations

import hashlib
import json
import os
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
PACK_DIR = ROOT / "app/src/main/assets/content-packs"
AUDIO_DIR = ROOT / "app/src/main/assets/speech"
MANIFEST_PATH = AUDIO_DIR / "manifest.json"
MODEL_VERSION = "supertonic3-int8-2026-05-11"
LANGUAGE = "en"
VOICE = "F1"
SID = 0
STEPS = 8
SPEED = 0.70


def normalise(value: object) -> str:
    return re.sub(r"\s+", " ", value).strip() if isinstance(value, str) else ""


def emit(needs_generation: bool) -> None:
    value = "true" if needs_generation else "false"
    output = os.environ.get("GITHUB_OUTPUT")
    if output:
        with open(output, "a", encoding="utf-8") as stream:
            stream.write(f"needs_generation={value}\n")
    print(f"needs_generation={value}")


def main() -> int:
    reasons: list[str] = []
    try:
        packs = [(path, json.loads(path.read_text(encoding="utf-8")))
                 for path in sorted(PACK_DIR.glob("*.json"))]
        manifest = json.loads(MANIFEST_PATH.read_text(encoding="utf-8"))
        files = manifest["files"]
        mappings = manifest["activities"]
        settings = manifest["settings"]
    except Exception as exc:
        print(f"Offline speech manifest or content packs unavailable: {exc}")
        emit(True)
        return 0

    expected: dict[str, dict] = {}
    expected_mappings: dict[str, dict] = {}
    english_activity_count = 0

    def add_record(activity: dict, role: str, text: object, option_id: str | None = None) -> str:
        value = normalise(text)
        if not value or re.search(r"[\u0600-\u06FF]", value):
            raise ValueError(f"Missing or non-English text: {activity.get('activityId')} / {role}")
        identity = "\n".join([
            MODEL_VERSION, LANGUAGE, VOICE, str(SID), f"{SPEED:.2f}",
            str(STEPS), activity["activityId"], role, option_id or "", value
        ])
        key = hashlib.sha256(identity.encode("utf-8")).hexdigest()
        expected[key] = {
            "key": key, "file": key + ".ogg", "text": value,
            "language": LANGUAGE, "voice": VOICE, "sid": SID,
            "speed": SPEED, "steps": STEPS,
            "activityId": activity["activityId"], "role": role,
            "optionId": option_id,
        }
        return key

    try:
        for _, pack in packs:
            for activity in pack.get("activities", []):
                if activity.get("language") != "en":
                    continue
                english_activity_count += 1
                aid = activity["activityId"]
                expected_mappings[aid] = {
                    "prompt": add_record(activity, "prompt", activity.get("prompt")),
                    "options": {},
                    "feedbackCorrect": add_record(activity, "feedback-correct", activity.get("feedbackCorrect")),
                    "feedbackIncorrect": add_record(activity, "feedback-incorrect", activity.get("feedbackIncorrect")),
                }
                for option in activity.get("options", []):
                    expected_mappings[aid]["options"][option["optionId"]] = add_record(
                        activity, "option", option.get("text"), option.get("optionId")
                    )
    except Exception as exc:
        print(f"Current content cannot be matched to English audio: {exc}")
        emit(True)
        return 0

    actual_by_key = {item.get("key"): item for item in files}
    if len(expected) != len(files) or set(expected) != set(actual_by_key):
        reasons.append("audio manifest keys/count do not match current English activity text")
    if manifest.get("count") != len(expected):
        reasons.append("manifest count does not match current English activity text")
    if manifest.get("sourceEnglishActivityCount") != english_activity_count:
        reasons.append("source English activity count does not match content packs")
    if set(mappings) != set(expected_mappings):
        reasons.append("activity audio map does not match current English activities")
    if manifest.get("model", {}).get("version") != MODEL_VERSION:
        reasons.append("pinned model version differs")
    if settings.get("language") != LANGUAGE or settings.get("voice") != VOICE or settings.get("sid") != SID or settings.get("steps") != STEPS or settings.get("speed") != SPEED:
        reasons.append("pinned speech settings differ")

    for key, expected_item in expected.items():
        actual = actual_by_key.get(key)
        if not actual:
            continue
        for field, value in expected_item.items():
            if actual.get(field) != value:
                reasons.append(f"manifest {field} mismatch for {key}")
                break
        filename = expected_item["file"]
        path = AUDIO_DIR / filename
        try:
            payload = path.read_bytes()
        except OSError:
            reasons.append(f"missing audio asset: {filename}")
            continue
        if len(payload) != actual.get("bytes") or hashlib.sha256(payload).hexdigest() != actual.get("sha256"):
            reasons.append(f"audio size/hash mismatch: {filename}")
        if actual.get("durationMs", 0) < 100 or actual.get("peak", 0) <= 0:
            reasons.append(f"invalid audio metadata: {filename}")

    for aid, expected_map in expected_mappings.items():
        activity = next((a for _, p in packs for a in p.get("activities", []) if a.get("activityId") == aid), None)
        actual_map = mappings.get(aid)
        if activity is None or actual_map is None:
            reasons.append(f"missing activity mapping: {aid}")
            continue
        if activity.get("promptAudioAssetPath") != "speech/" + actual_map["prompt"]:
            reasons.append(f"prompt path mismatch: {aid}")
        expected_option_paths = {k: "speech/" + v for k, v in actual_map["options"].items()}
        if activity.get("optionAudioAssetPaths") != expected_option_paths:
            reasons.append(f"option path mismatch: {aid}")
        if activity.get("feedbackCorrectAudioAssetPath") != "speech/" + actual_map["feedbackCorrect"]:
            reasons.append(f"correct feedback path mismatch: {aid}")
        if activity.get("feedbackIncorrectAudioAssetPath") != "speech/" + actual_map["feedbackIncorrect"]:
            reasons.append(f"incorrect feedback path mismatch: {aid}")

    if manifest.get("totalBytes") != sum(item.get("bytes", 0) for item in files):
        reasons.append("manifest totalBytes does not match file records")

    if reasons:
        print("Offline English speech requires regeneration:")
        for reason in reasons[:25]:
            print(f"- {reason}")
        emit(True)
    else:
        print(f"All {len(expected)} English clips for {english_activity_count} activities are current.")
        emit(False)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
