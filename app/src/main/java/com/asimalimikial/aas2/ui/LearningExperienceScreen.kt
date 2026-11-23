package com.asimalimikial.aas2.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.asimalimikial.aas2.R
import com.asimalimikial.aas2.content.ActivityDefinition
import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.ContentPackRepository
import com.asimalimikial.aas2.content.ContentPackSelectionPolicy
import com.asimalimikial.aas2.data.LearningAttemptDao
import com.asimalimikial.aas2.learning.ActivitySubmissionService
import kotlinx.coroutines.CancellationException

/** Loads the learner's offline pack and presents an English-first, subject-grouped activity catalogue. */
@Composable
fun LearningExperienceScreen(
    learnerId: String,
    learnerPathway: String,
    contentPackRepository: ContentPackRepository,
    learningAttemptDao: LearningAttemptDao,
    submissionService: ActivitySubmissionService,
    onExit: () -> Unit
) {
    val packId = remember(learnerPathway) {
        ContentPackSelectionPolicy.packIdForPathway(learnerPathway)
    }
    val attempts by learningAttemptDao.observeForLearner(learnerId).collectAsState(initial = emptyList())
    var contentPack by remember(packId) { mutableStateOf<ContentPack?>(null) }
    var isLoading by remember(packId) { mutableStateOf(true) }
    var loadError by remember(packId) { mutableStateOf(false) }
    var selectedDomain by remember(packId) { mutableStateOf<String?>(null) }
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

    val activities = remember(contentPack) {
        contentPack?.let { ContentPackSelectionPolicy.englishActivities(it) }.orEmpty()
    }
    val loadedPack = contentPack
    val skillById = remember(loadedPack) {
        loadedPack?.skills?.associateBy { it.skillId }.orEmpty()
    }
    val activitiesByDomain = remember(activities, skillById) {
        activities.groupBy { activity ->
            val domain = skillById[activity.skillId]?.domain ?: "General practice"
            ContentPackSelectionPolicy.subjectForDomain(domain, learnerPathway)
        }
    }
    val practicedActivityIds = remember(attempts, packId) {
        attempts.asSequence()
            .filter { it.contentPackId == packId }
            .map { it.activityId }
            .toSet()
    }
    val selectedActivity = activities.singleOrNull { it.activityId == selectedActivityId }
    val selectedDomainActivities = selectedDomain?.let(activitiesByDomain::get).orEmpty()

    BackHandler {
        when {
            selectedActivityId != null -> selectedActivityId = null
            selectedDomain != null -> selectedDomain = null
            else -> onExit()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                when {
                    selectedActivityId != null -> selectedActivityId = null
                    selectedDomain != null -> selectedDomain = null
                    else -> onExit()
                }
            }
        ) {
            Text(
                stringResource(
                    when {
                        selectedActivityId != null -> R.string.back_to_activities
                        selectedDomain != null -> R.string.back_to_learning_areas
                        else -> R.string.back_to_learning_space
                    }
                )
            )
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
                Text(
                    selectedDomain.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                ActivitySession(
                    learnerId = learnerId,
                    contentPack = loadedPack,
                    activity = selectedActivity,
                    submissionService = submissionService,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            selectedDomain != null -> {
                Text(
                    stringResource(R.string.choose_activity),
                    style = MaterialTheme.typography.headlineSmall
                )
                selectedDomainActivities.forEachIndexed { index, activity ->
                    ActivityCatalogueCard(
                        index = index + 1,
                        activity = activity,
                        isPractised = activity.activityId in practicedActivityIds,
                        onClick = { selectedActivityId = activity.activityId }
                    )
                }
                if (selectedDomainActivities.isEmpty()) {
                    Text(
                        stringResource(R.string.no_activities_available),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            activities.isEmpty() -> {
                Text(
                    stringResource(R.string.no_activities_available),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                Text(
                    stringResource(R.string.choose_learning_area),
                    style = MaterialTheme.typography.headlineSmall
                )
                activitiesByDomain.forEach { (domain, domainActivities) ->
                    val practicedCount = domainActivities.count { it.activityId in practicedActivityIds }
                    LearningDomainCard(
                        domain = domain,
                        practicedCount = practicedCount,
                        activityCount = domainActivities.size,
                        onClick = { selectedDomain = domain }
                    )
                }
            }
        }
    }
}

@Composable
private fun LearningDomainCard(
    domain: String,
    practicedCount: Int,
    activityCount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(domain, style = MaterialTheme.typography.titleLarge)
            Text(
                stringResource(R.string.learning_area_progress, practicedCount, activityCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ActivityCatalogueCard(
    index: Int,
    activity: ActivityDefinition,
    isPractised: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                stringResource(R.string.activity_number, index),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                activity.prompt,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            if (isPractised) {
                Text(
                    stringResource(R.string.activity_practised),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
