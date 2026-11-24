#!/usr/bin/env python3
"""Skip redundant Science staging TTS runs when manifest and OGG assets are current."""
from __future__ import annotations

import hashlib
import json
import os
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
DRAFTS = [
    ROOT / "content/staging/science-g2-unit-03-senses-draft-v0.1.json",
    ROOT / "content/staging/science-g2-unit-04-materials-draft-v0.1.json",
    ROOT / "content/staging/science-g2-unit-05-night-sky-draft-v0.1.json",
]
MANIFEST = ROOT / "content/staging/media/science-g2/speech/manifest.json"


def emit(value: bool) -> None:
    output = os.environ.get("GITHUB_OUTPUT")
    if output:
        with open(output, "a", encoding="utf-8") as stream:
            stream.write(f"needs_generation={'true' if value else 'false'}\n")
    print(f"needs_generation={'true' if value else 'false'}")


def main() -> int:
    reasons: list[str] = []
    try:
        manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))
        activities = []
        for path in DRAFTS:
            draft = json.loads(path.read_text(encoding="utf-8"))
            activities.extend(draft["activities"])
        files = manifest["files"]
        activity_paths = manifest["activityPaths"]
        settings = manifest["settings"]
    except Exception as exc:
        print(f"Audio manifest/draft unavailable or invalid: {exc}")
        emit(True)
        return 0

    expected = Counter()
    for activity in activities:
        activity_id = activity["activityId"]
        expected[(activity_id, "prompt", None, activity["prompt"])] += 1
        for option in activity["options"]:
            expected[(activity_id, "option", option["optionId"], option["text"])] += 1
        expected[(activity_id, "feedback-correct", None, activity["feedbackCorrect"])] += 1
        expected[(activity_id, "feedback-incorrect", None, activity["feedbackIncorrect"])] += 1

    actual = Counter(
        (item.get("activityId"), item.get("role"), item.get("optionId"), item.get("text"))
        for item in files
    )
    if len(activities) != 72 or manifest.get("count") != 432 or len(files) != 432:
        reasons.append("expected exactly 72 activities and 432 manifest files")
    if actual != expected:
        reasons.append("manifest speech text/roles/options do not match current Science drafts")
    if settings.get("language") != "en" or settings.get("voice") != "F1" or settings.get("speed") != 0.7 or settings.get("steps") != 8:
        reasons.append("pinned speech settings differ from the current staging configuration")
    if manifest.get("scope") != "STAGING_ONLY_NOT_RUNTIME_INTEGRATED":
        reasons.append("manifest scope is not staging-only")

    for activity in activities:
        activity_id = activity["activityId"]
        paths = activity_paths.get(activity_id)
        if not paths:
            reasons.append(f"missing activityPaths entry: {activity_id}")
            continue
        if activity.get("promptAudioAssetPath") != paths.get("prompt"):
            reasons.append(f"prompt path mismatch: {activity_id}")
        if activity.get("optionAudioAssetPaths") != paths.get("options"):
            reasons.append(f"option path mismatch: {activity_id}")
        if activity.get("feedbackCorrectAudioAssetPath") != paths.get("feedbackCorrect"):
            reasons.append(f"correct-feedback path mismatch: {activity_id}")
        if activity.get("feedbackIncorrectAudioAssetPath") != paths.get("feedbackIncorrect"):
            reasons.append(f"incorrect-feedback path mismatch: {activity_id}")

    for item in files:
        rel = item.get("path")
        if not rel:
            reasons.append("manifest file entry has no path")
            continue
        asset = ROOT / rel
        try:
            payload = asset.read_bytes()
        except OSError:
            reasons.append(f"missing audio asset: {rel}")
            continue
        if len(payload) != item.get("bytes"):
            reasons.append(f"byte-count mismatch: {rel}")
        if hashlib.sha256(payload).hexdigest() != item.get("sha256"):
            reasons.append(f"SHA-256 mismatch: {rel}")
        if not item.get("durationMs") or item.get("sampleRate") != 44100 or not item.get("peak"):
            reasons.append(f"invalid audio metadata: {rel}")

    if reasons:
        print("Science staging speech requires regeneration:")
        for reason in reasons[:20]:
            print(f"- {reason}")
        emit(True)
    else:
        print("All 432 clips, text bindings, paths, hashes and audio metadata are current.")
        emit(False)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
