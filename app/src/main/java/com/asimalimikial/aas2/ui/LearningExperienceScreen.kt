package com.asimalimikial.aas2.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asimalimikial.aas2.R
import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.ContentPackRepository
import com.asimalimikial.aas2.content.ContentPackSelectionPolicy
import com.asimalimikial.aas2.learning.ActivitySubmissionService
import kotlinx.coroutines.CancellationException

/** Loads the learner's bundled pilot pack and presents its activities in the device language. */
@Composable
fun LearningExperienceScreen(
    learnerId: String,
    learnerPathway: String,
    preferredLanguage: String,
    contentPackRepository: ContentPackRepository,
    submissionService: ActivitySubmissionService,
    onExit: () -> Unit
) {
    val packId = remember(learnerPathway) {
        ContentPackSelectionPolicy.packIdForPathway(learnerPathway)
    }
    var contentPack by remember(packId) { mutableStateOf<ContentPack?>(null) }
    var isLoading by remember(packId) { mutableStateOf(true) }
    var loadError by remember(packId) { mutableStateOf(false) }
    var selectedActivityId by remember(packId) { mutableStateOf<String?>(null) }

    LaunchedEffect(packId) {
        if (packId == null) {
            loadError = true
            isLoading = false
            return@LaunchedEffect
        }
        try {
            contentPack = contentPackRepository.load(packId)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: Exception) {
            loadError = true
        } finally {
            isLoading = false
        }
    }

    val activities = remember(contentPack, preferredLanguage) {
        contentPack?.let {
            ContentPackSelectionPolicy.activitiesForLanguage(it, preferredLanguage)
        }.orEmpty()
    }
    val loadedPack = contentPack
    val selectedActivity = activities.singleOrNull { it.activityId == selectedActivityId }

    BackHandler {
        if (selectedActivityId != null) {
            selectedActivityId = null
        } else {
            onExit()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onExit
        ) {
            Text(stringResource(R.string.back_to_learning_space))
        }

        when {
            isLoading -> {
                Text(
                    stringResource(R.string.loading_content_pack),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            loadError || loadedPack == null -> {
                Text(
                    stringResource(R.string.content_pack_load_error),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            selectedActivity != null -> {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { selectedActivityId = null }
                ) {
                    Text(stringResource(R.string.back_to_activities))
                }
                ActivitySession(
                    learnerId = learnerId,
                    contentPack = loadedPack,
                    activity = selectedActivity,
                    submissionService = submissionService,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            activities.isEmpty() -> {
                Text(
                    stringResource(R.string.no_activities_available),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                Text(
                    stringResource(R.string.learning_activities),
                    style = MaterialTheme.typography.headlineSmall
                )
                activities.forEachIndexed { index, activity ->
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { selectedActivityId = activity.activityId }
                    ) {
                        Text(stringResource(R.string.activity_list_item, index + 1, activity.prompt))
                    }
                }
            }
        }
    }
}
