package com.asimalimikial.aas2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.learning.ActivityEvaluationResult

/** Renders one validated single-choice activity; submission is delegated to the caller. */
@Composable
fun ActivityRenderer(
    activity: ActivityDefinition,
    evaluation: ActivityEvaluationResult?,
    onSubmitAnswer: (String) -> Unit,
    modifier: Modifier = Modifier,
    isSubmitting: Boolean = false,
    submissionError: String? = null
) {
    var selectedOptionId by remember(activity.activityId, activity.options) { mutableStateOf<String?>(null) }
    val currentEvaluation = evaluation?.takeIf { it.activityId == activity.activityId }
    val layoutDirection = if (activity.language == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
    val canChangeAnswer = currentEvaluation == null && !isSubmitting

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
                        Text(if (isSelected) "✓  ${option.text}" else option.text)
                    }
                }
                if (currentEvaluation == null) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedOptionId != null && !isSubmitting,
                        onClick = { selectedOptionId?.let(onSubmitAnswer) }
                    ) {
                        Text(
                            when {
                                isSubmitting && activity.language == "ar" -> "جارٍ حفظ الإجابة…"
                                isSubmitting -> "Saving answer…"
                                activity.language == "ar" -> "تحقق من الإجابة"
                                else -> "Check answer"
                            }
                        )
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
                        else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
