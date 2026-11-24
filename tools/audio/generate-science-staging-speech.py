#!/usr/bin/env python3
"""Generate an offline English speech preview corpus for staging-only Science Units 3–5."""
from __future__ import annotations

import argparse
import hashlib
import json
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
DRAFTS = [
    ROOT / "content/staging/science-g2-unit-03-senses-draft-v0.1.json",
    ROOT / "content/staging/science-g2-unit-04-materials-draft-v0.1.json",
    ROOT / "content/staging/science-g2-unit-05-night-sky-draft-v0.1.json",
]
GENERATOR = ROOT / "tools/audio/generate-offline-speech.py"
OUTPUT = ROOT / "content/staging/media/science-g2/speech"
RELATIVE_OUTPUT = "content/staging/media/science-g2/speech"


def read_json(path: Path):
    return json.loads(path.read_text(encoding="utf-8"))


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--model-dir", required=True)
    args = parser.parse_args()
    model_dir = Path(args.model_dir)
    drafts = {path: read_json(path) for path in DRAFTS}
    all_activities = [activity for draft in drafts.values() for activity in draft["activities"]]
    if len(all_activities) != 72:
        raise RuntimeError(f"Expected 72 staging science activities, found {len(all_activities)}")
    ids = [activity["activityId"] for activity in all_activities]
    if len(ids) != len(set(ids)):
        raise RuntimeError("Science staging activity IDs are not globally unique")

    with tempfile.TemporaryDirectory(prefix="aas2-science-audio-") as temp:
        temp = Path(temp)
        pack_dir = temp / "pack"
        audio_dir = temp / "audio"
        pack_dir.mkdir()
        pack = {
            "packId": "aas2-science-g2-units-03-05-staging",
            "activities": [
                {
                    "activityId": activity["activityId"],
                    "language": "en",
                    "prompt": activity["prompt"],
                    "options": activity["options"],
                    "feedbackCorrect": activity["feedbackCorrect"],
                    "feedbackIncorrect": activity["feedbackIncorrect"],
                }
                for activity in all_activities
            ],
        }
        pack_path = pack_dir / "science-staging.json"
        pack_path.write_text(json.dumps(pack, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        subprocess.run(
            [sys.executable, str(GENERATOR), "--pack-dir", str(pack_dir),
             "--model-dir", str(model_dir), "--output-dir", str(audio_dir)],
            check=True,
            cwd=ROOT,
        )
        generated_pack = read_json(pack_path)
        generated_by_id = {item["activityId"]: item for item in generated_pack["activities"]}
        generated_manifest = read_json(audio_dir / "manifest.json")
        if generated_manifest["count"] != 432:
            raise RuntimeError(f"Expected 432 clips (72 × 6), got {generated_manifest['count']}")
        if generated_manifest["settings"]["language"] != "en" or generated_manifest["settings"]["speed"] != 0.70:
            raise RuntimeError("Unexpected language or speed in generated audio manifest")

        OUTPUT.mkdir(parents=True, exist_ok=True)
        active_files = {item["file"] for item in generated_manifest["files"]}
        for old in OUTPUT.glob("*.ogg"):
            if old.name not in active_files:
                old.unlink()
        for filename in active_files:
            shutil.copy2(audio_dir / filename, OUTPUT / filename)

        file_by_name = {item["file"]: item for item in generated_manifest["files"]}
        for item in generated_manifest["files"]:
            item["path"] = f"{RELATIVE_OUTPUT}/{item['file']}"
            payload = (OUTPUT / item["file"]).read_bytes()
            if len(payload) != item["bytes"] or hashlib.sha256(payload).hexdigest() != item["sha256"]:
                raise RuntimeError(f"Staged audio integrity check failed: {item['file']}")

        activity_paths = {}
        for draft_path, draft in drafts.items():
            for activity in draft["activities"]:
                generated = generated_by_id[activity["activityId"]]
                for field in (
                    "promptAudioAssetPath", "optionAudioAssetPaths",
                    "feedbackCorrectAudioAssetPath", "feedbackIncorrectAudioAssetPath"
                ):
                    activity[field] = generated[field].replace(
                        "speech/", f"{RELATIVE_OUTPUT}/", 1
                    ) if isinstance(generated[field], str) else {
                        key: value.replace("speech/", f"{RELATIVE_OUTPUT}/", 1)
                        for key, value in generated[field].items()
                    }
                activity["audioStatus"] = "STAGED_GENERATED_PENDING_TRANSCRIPT_AND_PLAYBACK_QA"
                roles = generated_manifest["activities"][activity["activityId"]]
                activity_paths[activity["activityId"]] = {
                    "prompt": f"{RELATIVE_OUTPUT}/{roles['prompt']}",
                    "options": {key: f"{RELATIVE_OUTPUT}/{value}" for key, value in roles["options"].items()},
                    "feedbackCorrect": f"{RELATIVE_OUTPUT}/{roles['feedbackCorrect']}",
                    "feedbackIncorrect": f"{RELATIVE_OUTPUT}/{roles['feedbackIncorrect']}",
                }
            draft["releaseGate"]["mediaGenerated"] = True
            draft["audioManifestPath"] = f"{RELATIVE_OUTPUT}/manifest.json"
            draft_path.write_text(json.dumps(draft, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

        manifest = {
            **generated_manifest,
            "scope": "STAGING_ONLY_NOT_RUNTIME_INTEGRATED",
            "activityPaths": activity_paths,
            "files": generated_manifest["files"],
        }
        (OUTPUT / "manifest.json").write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        (OUTPUT / "NOTICE.txt").write_text(
            "Staging-only English audio for AAS2 Grade 2 Science Units 3–5.\n"
            "Generated with Supertonic 3 via sherpa-onnx 2.0.1 in GitHub Actions.\n"
            "English-only at speed 0.70. Machine-generated speech; listening/transcript QA is still required.\n"
            "Not integrated into the Android runtime. Model license: BigScience Open RAIL-M.\n",
            encoding="utf-8",
        )
        print(f"Staged {generated_manifest['count']} verified English clips for {len(all_activities)} science activities.")


if __name__ == "__main__":
    main()
