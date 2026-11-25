# Ali & Aboudy Scholars 2

An independent, offline-first Android learning app for two separate learner pathways:

- **Ali** — KG2
- **Aboudy** — Grade 2

The app uses a shared learning engine while keeping each learner's subjects, activities, attempts, progress, and recommendations isolated.

## Project status

Early foundation stage. Product name, curriculum mappings, and package/application identifiers remain subject to final review where noted in the project decision log.

## Latest installable testing build

<details>
<summary>APK 0.1.0 (versionCode 1) — Debug / device testing</summary>

- **[Download AAS2 Debug APK](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/blob/delivery/apk-debug-2-c8cd0f54/Ali-Aboudy-Scholars-2-debug.apk?raw=1)**
- Build workflow: [AAS2 APK delivery — run #2](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/actions/runs/45623331882)
- Source branch: `foundation/slice-0-android`
- Source commit: `c8cd0f54ec35201178783714e6f8a13cd84da261`
- Package ID: `com.asimalimikial.aas2`
- APK size: 40,735,063 bytes (about 38.9 MiB)
- SHA-256: `6fa9c763d51169982c43bdb7d318c4c7ddb326fb066c3d4765b9053656723b72`

**Important:** This is a debug build for testing, not a production-signed release. The repository is private, so GitHub sign-in with repository access is required to download it. A formal GitHub Release has not yet been published.

</details>

## Principles

- Offline-first; no account or cloud dependency in the MVP.
- Native Android: Kotlin and Jetpack Compose.
- Local persistence with Room and DataStore.
- Versioned, validated JSON content packs.
- Original learning prompts and assets; no reproduction of protected textbook pages, text, illustrations, or exercises.
- No analytics SDK or unnecessary network permissions in the MVP.

## Repository boundary

This repository is **AAS2-NEW** only. It is independent of the legacy repository `Ali-andd-aboudy-scholars`. Do not copy, merge, move, or modify legacy project files as part of this work.

## Build

Build and test instructions will be added with the first verified Android foundation commit.
