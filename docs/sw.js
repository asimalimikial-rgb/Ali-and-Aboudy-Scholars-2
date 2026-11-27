"use strict";

const CACHE_NAME = "aas2-web-v4";
const CORE_ASSETS = ["./", "./index.html", "./styles.css", "./app.js", "./data/learning-library.json", "./manifest.webmanifest"];

self.addEventListener("install", event => {
  event.waitUntil((async () => {
    const cache = await caches.open(CACHE_NAME);
    await cache.addAll(CORE_ASSETS);
    try {
      const response = await fetch("./data/learning-library.json", { cache: "no-store" });
      if (response.ok) {
        const library = await response.json();
        const paths = new Set();
        for (const activity of Array.isArray(library.activities) ? library.activities : []) {
          if (typeof activity.image === "string" && activity.image.trim()) paths.add("./assets/" + activity.image.replace(/^\/+/, ""));
          for (const option of Array.isArray(activity.options) ? activity.options : []) {
            if (typeof option.image === "string" && option.image.trim()) paths.add("./assets/" + option.image.replace(/^\/+/, ""));
          }
        }
        await Promise.allSettled([...paths].map(async path => {
          const asset = await fetch(path);
          if (asset.ok && asset.type !== "opaque") await cache.put(path, asset);
        }));
      }
    } catch (_) { /* Optional media must not block installation of the app shell. */ }
    await self.skipWaiting();
  })());
});

self.addEventListener("activate", event => {
  event.waitUntil((async () => {
    const keys = await caches.keys();
    await Promise.all(keys.filter(key => key.startsWith("aas2-web-") && key !== CACHE_NAME).map(key => caches.delete(key)));
    await self.clients.claim();
  })());
});

self.addEventListener("fetch", event => {
  const request = event.request;
  if (request.method !== "GET") return;
  const url = new URL(request.url);
  if (url.origin !== self.location.origin) return;
  event.respondWith((async () => {
    const cache = await caches.open(CACHE_NAME);
    const cached = await cache.match(request);
    if (cached) return cached;
    try {
      const response = await fetch(request);
      if (response.ok && response.type !== "opaque") await cache.put(request, response.clone());
      return response;
    } catch (_) {
      if (request.mode === "navigate") return (await cache.match("./index.html")) || Response.error();
      return Response.error();
    }
  })());
});
