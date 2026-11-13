package com.asimalimikial.aas2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.learning.ActivityEvaluationResult

/**
 * Reusable renderer for validated single-choice activities.
 * Loading packs and persisting attempts remain the responsibility of the caller.
 */
@Composable
fun ActivityRenderer(
    activity: ActivityDefinition,
    evaluation: ActivityEvaluationResult?,
    onSubmitAnswer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOptionId by remember(activity.activityId) { mutableStateOf<String?>(null) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(activity.prompt, style = MaterialTheme.typography.titleLarge)

            activity.options.forEach { option ->
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = evaluation == null,
                    onClick = { selectedOptionId = option.optionId }
                ) {
                    Text(
                        if (selectedOptionId == option.optionId) "✓  ${option.text}" else option.text
                    )
                }
            }

            if (evaluation == null) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedOptionId != null,
                    onClick = { selectedOptionId?.let(onSubmitAnswer) }
                ) {
                    Text(if (activity.language == "ar") "تحقق من الإجابة" else "Check answer")
                }
            } else {
                Text(
                    evaluation.feedback,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (evaluation.isCorrect) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }
}
