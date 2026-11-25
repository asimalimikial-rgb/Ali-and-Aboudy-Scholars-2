package com.asimalimikial.aas2.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.asimalimikial.aas2.R
import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.learning.ActivityEvaluationResult
import com.asimalimikial.aas2.learning.ActivitySubmissionService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * Connects the activity UI to the learner-scoped evaluation and persistence service.
 * The caller must provide the currently selected learner and a validated content pack.
 */
@Composable
fun ActivitySession(
    learnerId: String,
    contentPack: ContentPack,
    activity: ActivityDefinition,
    submissionService: ActivitySubmissionService,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val saveErrorText = stringResource(
        if (activity.language == "ar") R.string.activity_save_error_ar else R.string.activity_save_error_en
    )
    val sessionKey = ActivitySessionKey(
        learnerId = learnerId,
        contentPackId = contentPack.packId,
        contentPackVersion = contentPack.version,
        activityId = activity.activityId
    )
    var evaluation by remember(sessionKey) { mutableStateOf<ActivityEvaluationResult?>(null) }
    var usedHint by remember(sessionKey) { mutableStateOf(false) }
    var isSubmitting by remember(sessionKey) { mutableStateOf(false) }
    var submissionError by remember(sessionKey) { mutableStateOf<String?>(null) }

    // Recreate the renderer subtree when the learner, pack version, or activity changes.
    // This resets renderer-local state such as the selected answer between sessions.
    key(sessionKey) {
        ActivityRenderer(
            activity = activity,
            evaluation = evaluation,
            isSubmitting = isSubmitting,
            submissionError = submissionError,
            onSupportUsed = { usedHint = true },
            modifier = modifier,
            onRetry = {
                if (!isSubmitting) {
                    evaluation = null
                    submissionError = null
                }
            },
            onSubmitAnswer = { selectedOptionId ->
                if (!isSubmitting && evaluation == null) {
                    isSubmitting = true
                    submissionError = null
                    scope.launch {
                        try {
                            evaluation = submissionService.submit(
                                learnerId = learnerId,
                                contentPack = contentPack,
                                activityId = activity.activityId,
                                selectedOptionId = selectedOptionId,
                                usedHint = usedHint
                            )
                        } catch (cancelled: CancellationException) {
                            throw cancelled
                        } catch (_: Exception) {
                            submissionError = saveErrorText
                        } finally {
                            isSubmitting = false
                        }
                    }
                }
            }
        )
    }
}
