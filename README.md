# Ali & Aboudy Scholars 2

An independent, offline-first Android learning app with two learner pathways:

- **Ali — KG2**
- **Aboudy — Grade 2**

## Web experience

The repository includes a child-first, book-led learning experience in `docs/`.

- **[Open the live website](https://aas2-web.vercel.app)** — Vercel production deployment.
- Navigation: learner → subject → personal bookshelf → book units / learning collections → illustrated activities.
- The web library is generated from the Android KG2 and Grade 2 content packs (v0.2.4 and v0.4.4): 282 English activities, 108 for Ali and 174 for Aboudy.
- Original generated question and answer illustrations are included for the activity bank. The web app provides skill-level clues, answer feedback, and separate local progress.
- Seven book records and publisher scope sequences are shown with evidence-aware status labels. Only Macmillan Science Units 1–2 currently open into mapped lesson groups; the other publisher sequences are not falsely presented as approved app lessons.
- Copyrighted textbook cover images are personal-use-only and are not copied into the public Vercel site. The public bookshelf uses original typographic book artwork.
- Child-facing content is English-only. Visited content and images are cached for offline use after the first successful load.

### Hosting and deployment

- Provider: Vercel.
- Project: `aas2-web`.
- Production URL: https://aas2-web.vercel.app
- Source: `main` branch, repository directory `docs/`.
- Latest verified production update: PR #5 merged to `main` on 2026-11-25; Vercel deployment status: `READY`.
- The stable production alias `https://aas2-web.vercel.app` was checked after the merge and served the new learner-first experience.
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
