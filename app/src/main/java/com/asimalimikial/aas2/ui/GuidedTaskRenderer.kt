package com.asimalimikial.aas2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asimalimikial.aas2.R
import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.GuidedTaskDefinition
import com.asimalimikial.aas2.learning.GuidedTaskCompletionService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/** Adult-guided activity renderer. These tasks are deliberately never scored. */
@Composable
fun GuidedTaskRenderer(
    learnerId: String,
    contentPack: ContentPack,
    task: GuidedTaskDefinition,
    completionService: GuidedTaskCompletionService,
    isCompleted: Boolean,
    existingObservation: String?,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var observation by rememberSaveable(task.activityId, existingObservation) {
        mutableStateOf(existingObservation.orEmpty())
    }
    var isSaving by remember(task.activityId) { mutableStateOf(false) }
    var saveError by remember(task.activityId) { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                stringResource(R.string.guided_task_badge),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(task.title, style = MaterialTheme.typography.headlineSmall, color = Color(0xFF263D5B))
            Text(stringResource(R.string.guided_task_instructions), style = MaterialTheme.typography.titleMedium)
            task.instructions.forEachIndexed { index, instruction ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("${index + 1}.", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Text(instruction, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                }
            }
            Text(stringResource(R.string.guided_task_observation_criteria), style = MaterialTheme.typography.titleMedium)
            task.observableCriteria.forEach { criterion ->
                Text("•  $criterion", style = MaterialTheme.typography.bodyMedium)
            }
            Text(task.assessment, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(
                value = observation,
                onValueChange = { observation = it.take(500); saveError = false },
                modifier = Modifier.fillMaxWidth().heightIn(min = 110.dp),
                enabled = !isCompleted && !isSaving,
                label = { Text(stringResource(R.string.guided_task_observation_title)) },
                placeholder = { Text(stringResource(R.string.guided_task_observation_hint)) },
                supportingText = { Text("${observation.length}/500") }
            )
            if (isCompleted) {
                Text(
                    stringResource(R.string.guided_task_completed),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSaving,
                    onClick = {
                        scope.launch {
                            isSaving = true
                            saveError = false
                            try {
                                completionService.complete(
                                    learnerId = learnerId,
                                    contentPack = contentPack,
                                    guidedTaskId = task.activityId,
                                    observationNote = observation
                                )
                                onCompleted()
                            } catch (cancelled: CancellationException) {
                                throw cancelled
                            } catch (_: Exception) {
                                saveError = true
                            } finally {
                                isSaving = false
                            }
                        }
                    }
                ) {
                    Text(
                        stringResource(
                            if (isSaving) R.string.guided_task_saving else R.string.guided_task_mark_complete
                        )
                    )
                }
                if (saveError) {
                    Text(
                        stringResource(R.string.guided_task_save_error),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
