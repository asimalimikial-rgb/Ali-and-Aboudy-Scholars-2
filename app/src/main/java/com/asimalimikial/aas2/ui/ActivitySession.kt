package com.asimalimikial.aas2.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    var evaluation by remember(
        learnerId,
        contentPack.packId,
        contentPack.version,
        activity.activityId
    ) { mutableStateOf<ActivityEvaluationResult?>(null) }
    var isSubmitting by remember(
        learnerId,
        contentPack.packId,
        contentPack.version,
        activity.activityId
    ) { mutableStateOf(false) }
    var submissionError by remember(
        learnerId,
        contentPack.packId,
        contentPack.version,
        activity.activityId
    ) { mutableStateOf<String?>(null) }

    ActivityRenderer(
        activity = activity,
        evaluation = evaluation,
        isSubmitting = isSubmitting,
        submissionError = submissionError,
        modifier = modifier,
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
                            selectedOptionId = selectedOptionId
                        )
                    } catch (cancelled: CancellationException) {
                        throw cancelled
                    } catch (_: Exception) {
                        submissionError = if (activity.language == "ar") {
                            "تعذر حفظ الإجابة. تحقق من الاتصال بقاعدة البيانات ثم حاول مرة أخرى."
                        } else {
                            "Your answer could not be saved. Please try again."
                        }
                    } finally {
                        isSubmitting = false
                    }
                }
            }
        }
    )
}
