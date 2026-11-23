package com.asimalimikial.aas2.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.asimalimikial.aas2.R
import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.ContentPackRepository
import com.asimalimikial.aas2.content.ContentPackSelectionPolicy
import com.asimalimikial.aas2.data.LearnerProfileEntity
import com.asimalimikial.aas2.data.LearningAttemptDao
import com.asimalimikial.aas2.learning.LearnerPracticeSummaryCalculator
import com.asimalimikial.aas2.learning.LearnerProgressCalculator
import kotlinx.coroutines.CancellationException

/** Read-only parent view of one learner's locally stored progress. */
@Composable
fun ParentDashboardScreen(
    profile: LearnerProfileEntity,
    learnerName: String,
    pathwayLabel: String,
    contentPackRepository: ContentPackRepository,
    learningAttemptDao: LearningAttemptDao,
    onOpenSettings: () -> Unit,
    onExit: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnExit by rememberUpdatedState(onExit)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) currentOnExit()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val packId = remember(profile.pathway) {
        ContentPackSelectionPolicy.packIdForPathway(profile.pathway)
    }
    val attempts by learningAttemptDao.observeForLearner(profile.learnerId).collectAsState(initial = emptyList())
    var contentPack by remember(profile.learnerId) { mutableStateOf<ContentPack?>(null) }
    var isLoading by remember(profile.learnerId) { mutableStateOf(true) }
    var loadError by remember(profile.learnerId) { mutableStateOf(false) }

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

    val loadedPack = contentPack
    val subjectGroups = remember(loadedPack, profile.pathway) {
        loadedPack?.skills
            ?.groupBy { skill ->
                ContentPackSelectionPolicy.subjectForDomain(skill.domain, profile.pathway)
            }
            .orEmpty()
    }
    val summary = remember(profile.learnerId, attempts) {
        LearnerPracticeSummaryCalculator.summarize(profile.learnerId, attempts)
    }

    BackHandler(onBack = onExit)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onExit
        ) {
            Text(stringResource(R.string.back_to_learning_space))
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onOpenSettings
        ) {
            Text(stringResource(R.string.parent_settings_button))
        }

        Text(
            stringResource(R.string.parent_dashboard_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            stringResource(R.string.parent_dashboard_learner, learnerName, pathwayLabel),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            stringResource(R.string.parent_dashboard_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        when {
            isLoading -> Text(
                stringResource(R.string.loading_content_pack),
                style = MaterialTheme.typography.bodyLarge
            )

            loadError || loadedPack == null -> Text(
                stringResource(R.string.content_pack_load_error),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )

            else -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            stringResource(R.string.parent_dashboard_overview),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            stringResource(
                                R.string.parent_dashboard_attempt_summary,
                                summary.scoredAttempts,
                                summary.correctAnswers,
                                summary.accuracyPercent
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                subjectGroups.forEach { (subject, skills) ->
                    Text(subject, style = MaterialTheme.typography.titleLarge)
                    skills.forEach { skill ->
                        val progress = remember(skill.skillId, attempts, profile.learnerId) {
                            LearnerProgressCalculator.summarize(
                                learnerId = profile.learnerId,
                                skillId = skill.skillId,
                                attempts = attempts
                            )
                        }
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    skill.observableOutcome,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                if (progress.scoredAttempts == 0) {
                                    Text(
                                        stringResource(R.string.parent_skill_not_assessed),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    Text(
                                        stringResource(
                                            R.string.parent_skill_attempt_summary,
                                            progress.scoredAttempts,
                                            progress.correctAttempts,
                                            progress.accuracyPercent
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
                if (subjectGroups.isEmpty()) {
                    Text(stringResource(R.string.parent_dashboard_no_skills))
                }
            }
        }
    }
}
