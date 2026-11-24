package com.asimalimikial.aas2.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class PersonalBookCoverManifest(val covers: List<PersonalBookCover> = emptyList())

@Serializable
private data class PersonalBookCover(
    val coverId: String,
    val learner: String,
    val title: String,
    val assetPath: String
)

/** Shows the learner's real study-book covers locally; no network request is made. */
@Composable
internal fun PersonalBookShelf(learnerPathway: String) {
    val context = LocalContext.current
    val covers by produceState(initialValue = emptyList<PersonalBookCover>()) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                val json = context.assets.open("book-covers/manifest.json").bufferedReader().use { it.readText() }
                Json { ignoreUnknownKeys = true }.decodeFromString<PersonalBookCoverManifest>(json).covers
            }.getOrDefault(emptyList())
        }
    }
    val learner = if (learnerPathway == "KG2") "ali" else "aboudy"
    val ownBooks = covers.filter { it.learner == learner }
    if (ownBooks.isNotEmpty()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(if (learner == "ali") "Ali's Books" else "Aboudy's Books", style = MaterialTheme.typography.titleLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(ownBooks, key = { it.coverId }) { cover -> PersonalBookCoverCard(cover) }
            }
        }
    }
}

@Composable
private fun PersonalBookCoverCard(cover: PersonalBookCover) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, key1 = cover.assetPath) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                context.assets.open(cover.assetPath).use { input ->
                    BitmapFactory.decodeStream(input, null, BitmapFactory.Options().apply { inSampleSize = 2 })
                }
            }.getOrNull()
        }
    }
    Card(modifier = Modifier.width(132.dp)) {
        Column(modifier = Modifier.width(132.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = cover.title,
                    modifier = Modifier.fillMaxWidth().height(156.dp),
                    contentScale = ContentScale.Fit
                )
            } ?: Text("Cover unavailable", modifier = Modifier.height(156.dp))
            Text(
                cover.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
