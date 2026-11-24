#!/usr/bin/env python3
"""Download verified textbook cover references for the private AAS2 family app."""
from __future__ import annotations

import hashlib
import io
import json
import time
from pathlib import Path
from urllib.request import Request, urlopen

from PIL import Image

ROOT = Path.cwd()
OUTPUT = ROOT / "app/src/main/assets/book-covers"
MAX_SIZE = (960, 1280)
USER_AGENT = "Mozilla/5.0 (compatible; AAS2PrivateBookCoverImporter/1.0)"

BOOKS = [
    {
        "coverId": "hop-skip-jump-senior-kg",
        "learner": "ali",
        "title": "Hop, Skip and Jump — Senior KG Complete Kit",
        "isbn": "9789356660113",
        "file": "hop-skip-jump-senior-kg.jpg",
        "note": "Complete-kit product photograph. This is a kit image, not one individual component cover.",
        "sources": [
            {
                "image": "https://shantistationers.com/cdn/shop/files/WhatsAppImage2026-03-14at1.20.07PM_1.jpg?v=1773481170&width=897",
                "page": "https://shantistationers.com/products/hop-skip-and-jump-senior-kg-comprehensive-educational-kit",
            },
            {
                "image": "https://gracebasket.com/2295-thickbox_default/macmillan-education-hop-skip-and-jump-for-senior-kg.jpg",
                "page": "https://gracebasket.com/products/1669-macmillan-education-hop-skip-and-jump-for-senior-kg.html",
            },
            {
                "image": "https://bookscape-s3-bucket.s3.amazonaws.com/product_images/9789356660113.jpg",
                "page": "https://bookscape.com/product-details/hop-skip-and-jump-2023-sr-kg-9789356660113",
            },
        ],
    },
    {
        "coverId": "maths-xpress-senior-kg",
        "learner": "ali",
        "title": "Maths Xpress — Senior KG",
        "isbn": "9789356664975",
        "file": "maths-xpress-senior-kg.jpg",
        "note": "Seller product image for the Senior KG / UKG book.",
        "sources": [
            {
                "image": "https://raajkart.com/media/catalog/product/cache/378cf9a83101843e5b8b1271b991c285/m/a/maths_ukg.jpg",
                "page": "https://raajkart.com/macmillan-maths-xpress-for-senior-kg-revised.html",
            },
        ],
    },
    {
        "coverId": "english-world-level-2",
        "learner": "aboudy",
        "title": "English World — Level 2 Pupil's Book Pack",
        "isbn": "9781035117154",
        "file": "english-world-level-2.jpg",
        "note": "Candidate cover from the Level 2 Pupil's Book Pack product listing; exact physical copy still to be compared.",
        "sources": [
            {
                "image": "https://down-th.img.susercontent.com/file/th-11134207-7r98r-lvcpzgszfvgpc5",
                "page": "https://shopee.co.th/DKTODAY-%E0%B8%AB%E0%B8%99%E0%B8%B1%E0%B8%87%E0%B8%AA%E0%B8%B7%E0%B8%AD-ENGLISH-WORLD-2-PUPIL%27S-BOOK-PACK-i.204155689.29900078366",
            },
            {
                "image": "https://assets.asaxiy.uz/product/items/desktop/1679091c5a880faf6fb5e6087eb1b2dc2023091113011193945qolojjEV4K.jpg.webp",
                "page": "https://asaxiy.uz/product/bowen-hocking-macmillan-english-world-level-2-comlect-cd",
            },
        ],
    },
    {
        "coverId": "macmillan-mathematics-2a",
        "learner": "aboudy",
        "title": "Macmillan Mathematics — Level 2A",
        "isbn": "9781035130382",
        "file": "macmillan-mathematics-2a.jpg",
        "note": "Product image is matched to the current 2A Pupil's Book with eBook Pack listing.",
        "sources": [
            {
                "image": "https://cdn.dc5.ro/img-prod/3815428032-0.jpeg",
                "page": "https://carturesti.ro/carte/macmillan-mathematics-level-2a-pupil-s-book-with-ebook-3815428032",
            },
        ],
    },
    {
        "coverId": "macmillan-mathematics-2b",
        "learner": "aboudy",
        "title": "Macmillan Mathematics — Level 2B",
        "isbn": "9781380000613",
        "file": "macmillan-mathematics-2b.jpg",
        "note": "Product image visibly identifies the 2B learner book.",
        "sources": [
            {
                "image": "https://static.deltabook.ru/iblock/995/9956a35ccbbc7afd7c5a021b4e48e932.jpg",
                "page": "https://deltabook.ru/product/63616",
            },
            {
                "image": "https://www.intbook.ru/upload/iblock/14c/qt1p73vz11cddhok6lrfwl8kp8da0sbd/9781380000613.jpg",
                "page": "https://www.intbook.ru/catalog/international-curriculum/macmillan-mathematics-level-2b-pupil-s-book-with-ebook/",
            },
        ],
    },
    {
        "coverId": "macmillan-science-level-2",
        "learner": "aboudy",
        "title": "Macmillan Science — Level 2 Pupil's Book and eBook Pack",
        "isbn": "9781035130740",
        "file": "macmillan-science-level-2.jpg",
        "note": "Seller image from the current Pupil's Book and eBook Pack listing; compare with the household copy.",
        "sources": [
            {
                "image": "https://media.zid.store/c3c80b9f-f176-496c-9f27-04187e1270f9/346678d1-ab4c-4fc1-a76d-c63a6de33877.jpg",
                "page": "https://rushd.sa/products/macmillan-science-level-2-students-book-ebook-pack",
            },
        ],
    },
    {
        "coverId": "maths-xpress-book-2",
        "learner": "aboudy",
        "title": "Maths Xpress — Book 2",
        "isbn": "9789356664999",
        "file": "maths-xpress-book-2.jpg",
        "note": "Class 2 product image; keep distinct from Macmillan Mathematics 2B.",
        "sources": [
            {
                "image": "https://raajkart.com/media/catalog/product/cache/378cf9a83101843e5b8b1271b991c285/m/a/macmillan_maths_xpress_class_2.jpg",
                "page": "https://raajkart.com/macmillan-maths-xpress-for-class-2-2.html",
            },
        ],
    },
]


def download(url: str) -> bytes:
    request = Request(url, headers={"User-Agent": USER_AGENT, "Accept": "image/avif,image/webp,image/apng,image/*,*/*;q=0.8"})
    with urlopen(request, timeout=45) as response:
        data = response.read()
        content_type = response.headers.get("Content-Type", "").lower()
    if len(data) < 2_000:
        raise ValueError(f"Image response is too small ({len(data)} bytes), content-type={content_type}")
    return data


def normalise_image(data: bytes, target: Path) -> dict:
    with Image.open(io.BytesIO(data)) as image:
        image.verify()
    with Image.open(io.BytesIO(data)) as image:
        if image.width < 240 or image.height < 240:
            raise ValueError(f"Image resolution is too small: {image.width}x{image.height}")
        source_dimensions = [image.width, image.height]
        image.thumbnail(MAX_SIZE, Image.Resampling.LANCZOS)
        if image.mode not in ("RGB", "L"):
            image = image.convert("RGB")
        elif image.mode == "L":
            image = image.convert("RGB")
        image.save(target, format="JPEG", quality=88, optimize=True, progressive=True)
    payload = target.read_bytes()
    with Image.open(target) as verified:
        verified.verify()
    return {
        "bytes": len(payload),
        "sha256": hashlib.sha256(payload).hexdigest(),
        "sourceDimensions": source_dimensions,
    }


def main() -> None:
    OUTPUT.mkdir(parents=True, exist_ok=True)
    entries = []
    failures = []
    for book in BOOKS:
        target = OUTPUT / book["file"]
        saved = None
        for source in book["sources"]:
            try:
                print(f"Downloading {book['coverId']} from {source['image']}")
                data = download(source["image"])
                checks = normalise_image(data, target)
                saved = {
                    "coverId": book["coverId"],
                    "learner": book["learner"],
                    "title": book["title"],
                    "isbn": book["isbn"],
                    "file": book["file"],
                    "assetPath": f"book-covers/{book['file']}",
                    "sourceImageUrl": source["image"],
                    "sourceProductUrl": source["page"],
                    "note": book["note"],
                    "format": "JPEG",
                    "personalUseOnly": True,
                    **checks,
                }
                break
            except Exception as exc:
                failures.append({"coverId": book["coverId"], "url": source["image"], "error": str(exc)})
                print(f"Source failed for {book['coverId']}: {exc}")
                time.sleep(1)
        if saved is None:
            raise RuntimeError(f"No valid source image could be obtained for {book['coverId']}")
        entries.append(saved)

    manifest = {
        "schemaVersion": 1,
        "project": "Ali & Aboudy Scholars 2",
        "purpose": "Private, offline bookshelf display for the user's two children.",
        "distribution": "PRIVATE_PERSONAL_USE_ONLY",
        "publicRedistributionApproved": False,
        "processedWithoutCropping": True,
        "maxDimensions": list(MAX_SIZE),
        "count": len(entries),
        "covers": entries,
        "failedFallbackSources": failures,
    }
    (OUTPUT / "manifest.json").write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    (OUTPUT / "NOTICE.txt").write_text(
        "Personal-use textbook cover assets for the private Ali & Aboudy Scholars 2 app.\n"
        "These images are included only to help the two children recognize their own learning books.\n"
        "Book covers remain the property of their respective publishers and rights holders.\n"
        "Do not publish this repository, APK, or cover files publicly or redistribute them.\n"
        "Images were resized without cropping or content alteration for offline display.\n",
        encoding="utf-8",
    )
    print(f"Imported and verified {len(entries)} local book covers.")
    if failures:
        print(f"Fallback sources used after {len(failures)} source failures.")


if __name__ == "__main__":
    main()
