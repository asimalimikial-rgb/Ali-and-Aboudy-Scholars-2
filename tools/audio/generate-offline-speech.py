#!/usr/bin/env python3
"""Generate English-only offline speech assets for AAS2 in GitHub Actions."""
import argparse
import hashlib
import json
import math
import re
import subprocess
import tempfile
from pathlib import Path

import numpy as np
import sherpa_onnx
import soundfile as sf

MODEL_VERSION = "supertonic3-int8-2026-05-11"
MODEL_SHA256 = "82fa96f91c4ef8abaae3a14a3f4153facf88bed821d1f7331cec2700f432c427"
VOICE = "F1"
SID = 0
STEPS = 8
THREADS = 2
ENGLISH_SPEED = 0.70
LANGUAGE = "en"


def normalise(value):
    return re.sub(r"\s+", " ", value).strip() if isinstance(value, str) else ""


def add_record(records, activity, role, text, option_id=None):
    text = normalise(text)
    if not text:
        raise ValueError(f"Missing English audio text: {activity['activityId']} / {role}")
    if re.search(r"[\u0600-\u06FF]", text):
        raise ValueError(f"Arabic text is not permitted in the English-only audio corpus: {activity['activityId']} / {role}")
    identity = "\n".join([
        MODEL_VERSION, LANGUAGE, VOICE, str(SID), f"{ENGLISH_SPEED:.2f}",
        str(STEPS), activity["activityId"], role, option_id or "", text
    ])
    key = hashlib.sha256(identity.encode("utf-8")).hexdigest()
    records[key] = {
        "key": key,
        "file": key + ".ogg",
        "text": text,
        "language": LANGUAGE,
        "voice": VOICE,
        "sid": SID,
        "speed": ENGLISH_SPEED,
        "steps": STEPS,
        "activityId": activity["activityId"],
        "role": role,
        "optionId": option_id,
    }
    return key


def load_packs(pack_dir):
    return [(path, json.loads(path.read_text(encoding="utf-8")))
            for path in sorted(Path(pack_dir).glob("*.json"))]


def collect(packs):
    records = {}
    mappings = {}
    english_activity_count = 0
    for _, pack in packs:
        for activity in pack.get("activities", []):
            if activity.get("language") != "en":
                continue
            english_activity_count += 1
            aid = activity["activityId"]
            mapped = {
                "prompt": add_record(records, activity, "prompt", activity.get("prompt")),
                "options": {},
                "feedbackCorrect": add_record(records, activity, "feedback-correct", activity.get("feedbackCorrect")),
                "feedbackIncorrect": add_record(records, activity, "feedback-incorrect", activity.get("feedbackIncorrect")),
            }
            for option in activity.get("options", []):
                mapped["options"][option["optionId"]] = add_record(
                    records, activity, "option", option.get("text"), option.get("optionId")
                )
            mappings[aid] = mapped
    if english_activity_count == 0:
        raise RuntimeError("No English activities found in the supplied content packs.")
    return records, mappings, english_activity_count


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--pack-dir", required=True)
    parser.add_argument("--model-dir", required=True)
    parser.add_argument("--output-dir", required=True)
    args = parser.parse_args()

    packs = load_packs(args.pack_dir)
    records, mappings, english_activity_count = collect(packs)
    model_dir = Path(args.model_dir)
    required = [
        "duration_predictor.int8.onnx", "text_encoder.int8.onnx",
        "vector_estimator.int8.onnx", "vocoder.int8.onnx", "tts.json",
        "unicode_indexer.bin", "voice.bin"
    ]
    for name in required:
        if not (model_dir / name).is_file():
            raise FileNotFoundError(str(model_dir / name))

    config = sherpa_onnx.OfflineTtsConfig(
        model=sherpa_onnx.OfflineTtsModelConfig(
            supertonic=sherpa_onnx.OfflineTtsSupertonicModelConfig(
                duration_predictor=str(model_dir / "duration_predictor.int8.onnx"),
                text_encoder=str(model_dir / "text_encoder.int8.onnx"),
                vector_estimator=str(model_dir / "vector_estimator.int8.onnx"),
                vocoder=str(model_dir / "vocoder.int8.onnx"),
                tts_json=str(model_dir / "tts.json"),
                unicode_indexer=str(model_dir / "unicode_indexer.bin"),
                voice_style=str(model_dir / "voice.bin"),
            ),
            num_threads=THREADS,
            debug=False,
            provider="cpu",
        )
    )
    if not config.validate():
        raise RuntimeError("sherpa-onnx rejected the pinned Supertonic 3 configuration.")
    tts = sherpa_onnx.OfflineTts(config)

    output_dir = Path(args.output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)
    manifest_path = output_dir / "manifest.json"
    cached = {}
    if manifest_path.is_file():
        try:
            old = json.loads(manifest_path.read_text(encoding="utf-8"))
            for entry in old.get("files", []):
                audio_file = output_dir / entry.get("file", "")
                if audio_file.is_file():
                    data = audio_file.read_bytes()
                    if len(data) == entry.get("bytes") and hashlib.sha256(data).hexdigest() == entry.get("sha256"):
                        cached[entry["key"]] = entry
        except Exception as exc:
            print(f"Existing audio cache could not be reused: {exc}")

    files = []
    reused = 0
    generated = 0
    with tempfile.TemporaryDirectory() as temp_dir:
        temp_dir = Path(temp_dir)
        for index, (key, item) in enumerate(sorted(records.items()), 1):
            existing = cached.get(key)
            output_file = output_dir / item["file"]
            if existing and output_file.is_file():
                files.append({**existing, **item, "sha256": existing["sha256"],
                              "bytes": existing["bytes"], "durationMs": existing["durationMs"],
                              "sampleRate": existing["sampleRate"], "peak": existing["peak"]})
                reused += 1
                continue

            generation = sherpa_onnx.GenerationConfig()
            generation.sid = SID
            generation.num_steps = STEPS
            generation.speed = ENGLISH_SPEED
            generation.extra["lang"] = LANGUAGE
            result = tts.generate(item["text"], generation)
            audio = np.asarray(result.samples, dtype=np.float32).reshape(-1)
            sample_rate = int(result.sample_rate)
            if audio.size == 0 or sample_rate <= 0 or not np.isfinite(audio).all():
                raise RuntimeError(f"Invalid generated audio: {key}")
            peak = float(np.max(np.abs(audio)))
            if not math.isfinite(peak) or peak < 1e-5:
                raise RuntimeError(f"Silent generated audio: {key}")

            wav_file = temp_dir / (key + ".wav")
            sf.write(str(wav_file), audio, samplerate=sample_rate, format="WAV", subtype="PCM_16")
            subprocess.run([
                "ffmpeg", "-hide_banner", "-loglevel", "error", "-y",
                "-i", str(wav_file), "-c:a", "libopus", "-b:a", "48k",
                "-vbr", "on", "-application", "audio", str(output_file)
            ], check=True)
            payload = output_file.read_bytes()
            duration_ms = int(round(audio.size * 1000 / sample_rate))
            if duration_ms < 100 or len(payload) < 100:
                raise RuntimeError(f"Invalid OGG output: {key}")
            files.append({
                **item,
                "sha256": hashlib.sha256(payload).hexdigest(),
                "bytes": len(payload),
                "durationMs": duration_ms,
                "sampleRate": sample_rate,
                "peak": peak,
            })
            generated += 1
            if index % 50 == 0 or index == len(records):
                print(f"Processed {index}/{len(records)} audio clips ({reused} reused, {generated} generated).")

    by_key = {entry["key"]: entry for entry in files}
    activity_manifest = {}
    for activity_id, roles in mappings.items():
        activity_manifest[activity_id] = {
            "prompt": by_key[roles["prompt"]]["file"],
            "options": {option_id: by_key[key]["file"] for option_id, key in roles["options"].items()},
            "feedbackCorrect": by_key[roles["feedbackCorrect"]]["file"],
            "feedbackIncorrect": by_key[roles["feedbackIncorrect"]]["file"],
        }

    # Attach local paths to the v4 activities so the Android UI never needs a remote lookup.
    for pack_path, pack in packs:
        changed = False
        for activity in pack.get("activities", []):
            if activity.get("language") != "en":
                continue
            audio = activity_manifest[activity["activityId"]]
            fields = {
                "promptAudioAssetPath": "speech/" + audio["prompt"],
                "optionAudioAssetPaths": {key: "speech/" + value for key, value in audio["options"].items()},
                "feedbackCorrectAudioAssetPath": "speech/" + audio["feedbackCorrect"],
                "feedbackIncorrectAudioAssetPath": "speech/" + audio["feedbackIncorrect"],
            }
            if any(activity.get(key) != value for key, value in fields.items()):
                activity.update(fields)
                changed = True
        if changed:
            pack_path.write_text(json.dumps(pack, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

    manifest = {
        "schemaVersion": 1,
        "model": {
            "name": "Supertone Supertonic 3",
            "version": MODEL_VERSION,
            "archiveSha256": MODEL_SHA256,
            "license": "BigScience Open RAIL-M",
            "licenseUrl": "https://huggingface.co/Supertone/supertonic-3/blob/3cadd1ee6394adea1bd021217a0e650ede09a323/LICENSE",
            "machineGenerated": True,
        },
        "generator": {"runtime": "sherpa-onnx 2.0.1", "execution": "GitHub Actions only"},
        "settings": {"language": "en", "voice": VOICE, "sid": SID, "steps": STEPS,
                     "speed": ENGLISH_SPEED, "threads": THREADS},
        "sourceEnglishActivityCount": english_activity_count,
        "count": len(files),
        "totalBytes": sum(entry["bytes"] for entry in files),
        "activities": activity_manifest,
        "files": files,
    }
    manifest_path.write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    (output_dir / "NOTICE.txt").write_text(
        "Offline pre-generated English educational speech for Ali & Aboudy Scholars 2.\n"
        "Generated with Supertonic 3 via sherpa-onnx 2.0.1 in GitHub Actions only.\n"
        "The TTS runtime and model weights are not included in the Android application.\n"
        "English words and sentences use speed 0.70.\n"
        "Model license: BigScience Open RAIL-M; use-based restrictions apply.\n"
        "Preserve this notice and disclose that the voice is machine-generated.\n",
        encoding="utf-8",
    )
    print(f"Complete: {len(files)} English clips, {manifest['totalBytes']} bytes, {english_activity_count} English activities.")


if __name__ == "__main__":
    main()
