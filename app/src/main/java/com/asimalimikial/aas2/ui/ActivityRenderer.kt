package com.asimalimikial.aas2.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.asimalimikial.aas2.R
import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.content.AnswerOption
import com.asimalimikial.aas2.learning.ActivityEvaluationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Renders one validated single-choice activity; submission is delegated to the caller. */
@Composable
fun ActivityRenderer(
    activity: ActivityDefinition,
    evaluation: ActivityEvaluationResult?,
    onSubmitAnswer: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isSubmitting: Boolean = false,
    submissionError: String? = null
) {
    var selectedOptionId by remember(activity.activityId, activity.options) { mutableStateOf<String?>(null) }
    val currentEvaluation = evaluation?.takeIf { it.activityId == activity.activityId }
    val layoutDirection = if (activity.language == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
    val canChangeAnswer = currentEvaluation == null && !isSubmitting
    val checkAnswerLabel = stringResource(
        if (activity.language == "ar") R.string.activity_check_answer_ar else R.string.activity_check_answer_en
    )
    val savingAnswerLabel = stringResource(
        if (activity.language == "ar") R.string.activity_saving_answer_ar else R.string.activity_saving_answer_en
    )

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Card(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(activity.prompt, style = MaterialTheme.typography.titleLarge)
                activity.options.forEach { option ->
                    val isSelected = selectedOptionId == option.optionId
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { selected = isSelected },
                        enabled = canChangeAnswer,
                        onClick = { selectedOptionId = option.optionId },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.surface
                            },
                            contentColor = if (isSelected) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            option.imageAssetPath?.let { path ->
                                OptionImage(
                                    assetPath = path,
                                    contentDescription = option.text.ifBlank {
                                        stringResource(R.string.picture_answer_option)
                                    }
                                )
                            }
                            val optionLabel = option.text.ifBlank {
                                stringResource(R.string.picture_answer_option)
                            }
                            Text(if (isSelected) "✓  $optionLabel" else optionLabel)
                        }
                    }
                }
                if (currentEvaluation == null) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedOptionId != null && !isSubmitting,
                        onClick = { selectedOptionId?.let(onSubmitAnswer) }
                    ) {
                        Text(if (isSubmitting) savingAnswerLabel else checkAnswerLabel)
                    }
                    if (submissionError != null) {
                        Text(
                            submissionError,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    Text(
                        currentEvaluation.feedback,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (currentEvaluation.isCorrect) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (!currentEvaluation.isCorrect) {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedOptionId = null
                                onRetry()
                            }
                        ) {
                            Text(stringResource(R.string.activity_try_again))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Loads an optional raster illustration from the app's bundled assets.
 * Missing, corrupt, or unsupported images resolve to null; the answer label remains usable.
 * The path is already checked by ContentPackValidator before a pack reaches this renderer.
 */
@Composable
private fun OptionImage(
    assetPath: String,
    contentDescription: String
) {
    val context = LocalContext.current
    val bitmap by produceState<android.graphics.Bitmap?>(initialValue = null, key1 = assetPath) {
        value = withContext(Dispatchers.IO) {
            try {
                context.assets.open(assetPath).use { input ->
                    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    BitmapFactory.decodeStream(input, null, bounds)
                    val sampleSize = calculateSampleSize(bounds.outWidth, bounds.outHeight)
                    context.assets.open(assetPath).use { imageInput ->
                        BitmapFactory.decodeStream(
                            imageInput,
                            null,
                            BitmapFactory.Options().apply { inSampleSize = sampleSize }
                        )
                    }
                }
            } catch (_: IOException) {
                null
            }
        }
    }

    bitmap?.let { decoded ->
        Image(
            bitmap = decoded.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp),
            contentScale = ContentScale.Fit
        )
    }
}


/** Keeps large supplied illustrations from allocating their full-resolution bitmap in memory. */
private fun calculateSampleSize(width: Int, height: Int): Int {
    if (width <= 0 || height <= 0) return 1
    var sampleSize = 1
    while (width / sampleSize > 1200 || height / sampleSize > 900) {
        sampleSize *= 2
    }
    return sampleSize
}
