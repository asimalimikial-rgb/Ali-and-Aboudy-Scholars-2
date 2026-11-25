# Ali & Aboudy Scholars 2

An independent, offline-first Android learning app with two learner pathways:

- **Ali — KG2**
- **Aboudy — Grade 2**

## Web experience

The repository includes a responsive, child-first web learning experience in the `docs/` directory.

- **[Open the live website](https://aas2-web.vercel.app)** — public Vercel production deployment.
- Main interface: `docs/index.html` and `docs/styles.css`.
- Child-first navigation: choose Ali or Aboudy, tap a subject icon, then open a learning collection and its activities.
- The current web data groups activities by subject and skill ID; official textbook-to-unit mapping is not yet complete in the web data, so skill collections are not presented as verified textbook chapters.
- 282 English-only single-choice activities are included in the web preview (108 KG2 and 174 Grade 2).
- Learner progress is stored separately in the browser's local storage on the current device.
- A service worker caches the site and question bank for offline use after the first successful visit.
- No account, analytics, or cloud progress sync is used.
- Child-facing content and controls are English-only.

**Web status:** The web experience has a dedicated child-first navigation flow and retains single-choice practice, positive feedback, separate local progress, and offline caching. It is still not feature-equivalent to Android: media assets, audio, adaptive learning, parent controls, and the complete guided-help experience are not yet ported.

### Hosting and deployment

- Provider: Vercel.
- Project: `aas2-web`.
- Production URL: https://aas2-web.vercel.app
- Source: `main` branch, repository directory `docs/`.
- Verified production deployment commit: `6178caa4130dd3a57dcbd978fb241fb55d4a039d`.
- Deployment status at publication: `READY`.
- The Vercel project is connected to this GitHub repository; subsequent commits to the configured production branch can trigger deployments.

## Latest installable testing build

<details>
<summary>APK 0.1.0 (versionCode 1) — Debug / device testing</summary>

- **[Download AAS2 Debug APK](https://github.com/asimalimikial-rgb/Ali-and-Aboudy-Scholars-2/blob/delivery/apk-debug-2-c8cd0f54/Ali-Aboudy-Scholars-2-debug.apk?raw=1)**
- Build workflow: [AAS2 APK delivery — run #2](https://github.com/asimalimikial/Ali-and-Aboudy-Scholars-2/actions/runs/45623331882)
- Source branch: `foundation/slice-0-android`
- Source commit: `c8cd0f54ec35201178783714e6f8a13cd84da261`
- Package ID: `com.asimalimikial.aas2`
- APK size: 40,735,063 bytes (about 38.9 MiB)
- SHA-256: `6fa9c763d51169982c43bdb7d318c4c7ddb326fb066c3d4765b9053656723b72`

**Important:** This is a debug build for testing, not a production-signed release. The repository is private, so GitHub sign-in with repository access is required to download it. A formal GitHub Release has not yet been published.

</details>

## Android project status

The Android implementation remains on its existing development branches. Draft PR #1 is not merged: its own acceptance gates require automated tests, API 35 emulator checks, completion/review of the remaining hints and illustrations, media QA, and physical-device testing. The web-only work in this update does not bypass those gates.

## Principles

- Offline-first; no account or cloud dependency in the MVP.
- Native Android: Kotlin and Jetpack Compose.
- Local persistence with Room and DataStore.
- Versioned, validated JSON content packs.
- Original learning prompts and assets; no reproduction of protected textbook pages, text, illustrations, or exercises.
- No analytics SDK or unnecessary network permissions in the MVP.

## Repository boundary

This repository is **AAS2-NEW** only. It is independent of the legacy repository `Ali-andd-aboudy-scholars`. Do not copy, merge, move, or modify legacy project files as part of this work.
