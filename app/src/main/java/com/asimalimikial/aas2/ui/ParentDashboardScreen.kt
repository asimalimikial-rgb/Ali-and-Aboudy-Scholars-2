package com.asimalimikial.aas2.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.lifecycle.ProcessLifecycleOwner
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
import com.asimalimikial.aas2.learning.MasteryLevel
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
    val lifecycleOwner = ProcessLifecycleOwner.get()
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
    val attemptsFlow = remember(profile.learnerId) { learningAttemptDao.observeForLearner(profile.learnerId) }
    val attempts by attemptsFlow.collectAsState(initial = emptyList())
    var contentPack by remember(profile.learnerId) { mutableStateOf<ContentPack?>(null) }
    var isLoading by remember(profile.learnerId) { mutableStateOf(true) }
    var loadError by remember(profile.learnerId) { mutableStateOf(false) }

    LaunchedEffect(packId) {
        isLoading = true
        loadError = false
        contentPack = null
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

    val packActivities = remember(loadedPack) { loadedPack?.activities.orEmpty() }
    val packActivityById = remember(packActivities) { packActivities.associateBy { it.activityId } }
    val packAttempts = remember(attempts, packId) {
        attempts.filter { it.contentPackId == packId && it.isCorrect != null }
    }
    val attemptedActivityIds = remember(packAttempts) { packAttempts.map { it.activityId }.toSet() }
    val startedLessonIds = remember(packActivityById, attemptedActivityIds) {
        attemptedActivityIds.mapNotNull { packActivityById[it]?.lessonId }.toSet()
    }
    val practicedSkillIds = remember(packAttempts) { packAttempts.map { it.skillId }.toSet() }
    val recentAttempts = remember(packAttempts) {
        val cutoff = System.currentTimeMillis() - 7L * 24L * 60L * 60L * 1000L
        packAttempts.filter { it.createdAtEpochMillis >= cutoff }
    }
    val recentCorrect = remember(recentAttempts) { recentAttempts.count { it.isCorrect == true } }

    BackHandler(onBack = onExit)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF0F5FB), Color(0xFFF9FAFC), Color.White)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF263D5B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("PARENT SPACE  ·  PRIVATE", style = MaterialTheme.typography.labelLarge, color = Color(0xFFD5E5F3))
                Text(stringResource(R.string.parent_dashboard_title), style = MaterialTheme.typography.headlineSmall, color = Color.White)
                Text(stringResource(R.string.parent_dashboard_learner, learnerName, pathwayLabel), style = MaterialTheme.typography.bodyMedium, color = Color(0xFFD5E5F3))
            }
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onExit,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(stringResource(R.string.back_to_learning_space))
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onOpenSettings
        ) {
            Text(stringResource(R.string.parent_settings_button))
        }

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
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)) {
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
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    stringResource(
                                        R.string.parent_dashboard_activities_started,
                                        attemptedActivityIds.size,
                                        packActivities.size
                                    ),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    stringResource(
                                        R.string.parent_dashboard_lessons_started,
                                        startedLessonIds.size,
                                        loadedPack.lessons.size
                                    ),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    stringResource(
                                        R.string.parent_dashboard_skills_practised,
                                        practicedSkillIds.size,
                                        loadedPack.skills.size
                                    ),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Text(
                            stringResource(
                                R.string.parent_dashboard_recent_week,
                                recentAttempts.size,
                                recentCorrect
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    skill.observableOutcome,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    stringResource(
                                        when (progress.masteryLevel) {
                                            MasteryLevel.NOT_STARTED -> R.string.parent_mastery_not_started
                                            MasteryLevel.PRACTICING -> R.string.parent_mastery_practising
                                            MasteryLevel.DEVELOPING -> R.string.parent_mastery_developing
                                            MasteryLevel.MASTERED -> R.string.parent_mastery_mastered
                                        }
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (progress.scoredAttempts > 0) {
                                    LinearProgressIndicator(
                                        progress = { progress.accuracyPercent / 100f },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        stringResource(
                                            R.string.parent_skill_detailed_summary,
                                            progress.scoredAttempts,
                                            progress.correctAttempts,
                                            progress.incorrectAttempts,
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
