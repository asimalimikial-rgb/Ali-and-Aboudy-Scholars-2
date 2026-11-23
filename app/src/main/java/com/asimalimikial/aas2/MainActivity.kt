package com.asimalimikial.aas2

import android.app.Activity
import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import java.util.Locale
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import com.asimalimikial.aas2.content.AndroidAssetContentPackTextSource
import com.asimalimikial.aas2.content.AssetContentPackRepository
import com.asimalimikial.aas2.content.ContentPackSelectionPolicy
import com.asimalimikial.aas2.learning.ActivitySubmissionService
import com.asimalimikial.aas2.learning.LearnerPracticeSummaryCalculator
import com.asimalimikial.aas2.ui.LearningExperienceScreen
import com.asimalimikial.aas2.ui.ParentDashboardScreen
import com.asimalimikial.aas2.ui.ParentSettingsScreen
import com.asimalimikial.aas2.data.AppDatabase
import com.asimalimikial.aas2.data.LearnerProfileEntity
import com.asimalimikial.aas2.data.LearningAttemptDao
import com.asimalimikial.aas2.data.LearnerSelectionPolicy
import com.asimalimikial.aas2.data.ParentAppLanguagePolicy
import com.asimalimikial.aas2.data.UserPreferences

class MainActivity : ComponentActivity() {
    private lateinit var parentGateLauncher: ActivityResultLauncher<Intent>
    private var pendingParentGateAction: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentGateLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val authorizedAction = pendingParentGateAction
            pendingParentGateAction = null
            if (result.resultCode == Activity.RESULT_OK) {
                authorizedAction?.invoke()
            }
        }

        val database = AppDatabase.getInstance(applicationContext)
        val profileDao = database.learnerProfileDao()
        val userPreferences = UserPreferences(applicationContext)
        val contentPackRepository = AssetContentPackRepository(
            AndroidAssetContentPackTextSource(applicationContext)
        )
        val submissionService = ActivitySubmissionService(
            learnerProfileDao = profileDao,
            learningAttemptDao = database.learningAttemptDao()
        )

        lifecycleScope.launch {
            profileDao.insertDefaultsIfMissing(
                listOf(
                    LearnerProfileEntity(
                        learnerId = "ali",
                        displayName = "Ali",
                        pathway = "KG2"
                    ),
                    LearnerProfileEntity(
                        learnerId = "aboudy",
                        displayName = "Aboudy",
                        pathway = "Grade 2"
                    )
                )
            )
        }

        setContent {
            val profiles by profileDao.observeProfiles().collectAsState(initial = emptyList())
            val selectedLearnerId by userPreferences.selectedLearnerId.collectAsState(initial = null)
            val parentAppLanguage by userPreferences.parentAppLanguage.collectAsState(
                initial = ParentAppLanguagePolicy.SYSTEM
            )
            val parentLocale = remember(parentAppLanguage) {
                ParentAppLanguagePolicy.localeFor(parentAppLanguage)
            }
            val baseConfiguration = LocalConfiguration.current
            val localizedConfiguration = remember(baseConfiguration, parentLocale) {
                Configuration(baseConfiguration).apply {
                    setLocale(parentLocale)
                    setLayoutDirection(parentLocale)
                }
            }
            val baseContext = LocalContext.current
            val localizedContext = remember(baseContext, localizedConfiguration) {
                baseContext.createConfigurationContext(localizedConfiguration)
            }
            val selectedProfile = LearnerSelectionPolicy.selectedProfile(selectedLearnerId, profiles)
            var showLearningExperience by remember { mutableStateOf(false) }
            var showParentDashboard by remember { mutableStateOf(false) }
            var showParentSettings by remember { mutableStateOf(false) }

            LaunchedEffect(selectedProfile?.learnerId) {
                showLearningExperience = false
                showParentDashboard = false
                showParentSettings = false
            }

            LaunchedEffect(selectedLearnerId, profiles) {
                if (LearnerSelectionPolicy.shouldClearSelection(selectedLearnerId, profiles)) {
                    userPreferences.clearSelectedLearner()
                }
            }

            CompositionLocalProvider(
                LocalConfiguration provides localizedConfiguration,
                LocalContext provides localizedContext,
                LocalLayoutDirection provides if (ParentAppLanguagePolicy.isRightToLeft(parentLocale)) {
                    LayoutDirection.Rtl
                } else {
                    LayoutDirection.Ltr
                }
            ) {
                MaterialTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                    if (selectedProfile == null) {
                        LearnerProfileSelection(
                            profiles = LearnerSelectionPolicy.activeProfiles(profiles),
                            isLoading = profiles.isEmpty(),
                            onSelect = { profile ->
                                lifecycleScope.launch {
                                    // Persist only a learner that still exists and is active.
                                    val currentProfile = profileDao.findById(profile.learnerId)
                                    if (currentProfile?.isActive == true) {
                                        userPreferences.setSelectedLearner(profile.learnerId)
                                    }
                                }
                            }
                        )
                    } else if (showParentSettings) {
                        ParentSettingsScreen(
                            userPreferences = userPreferences,
                            onBack = { showParentSettings = false }
                        )
                    } else if (showParentDashboard) {
                        ParentDashboardScreen(
                            profile = selectedProfile,
                            learnerName = localizedLearnerName(selectedProfile),
                            pathwayLabel = localizedPathway(selectedProfile.pathway),
                            contentPackRepository = contentPackRepository,
                            learningAttemptDao = database.learningAttemptDao(),
                            onOpenSettings = { showParentSettings = true },
                            onExit = { showParentDashboard = false; showParentSettings = false }
                        )
                    } else if (showLearningExperience) {
                        LearningExperienceScreen(
                            learnerId = selectedProfile.learnerId,
                            learnerPathway = selectedProfile.pathway,
                            contentPackRepository = contentPackRepository,
                            learningAttemptDao = database.learningAttemptDao(),
                            submissionService = submissionService,
                            onExit = { showLearningExperience = false }
                        )
                    } else {
                        LearnerHome(
                            profile = selectedProfile,
                            contentPackRepository = contentPackRepository,
                            learningAttemptDao = database.learningAttemptDao(),
                            onStartLearning = { showLearningExperience = true },
                            onOpenParentDashboard = {
                                requestParentGate { showParentDashboard = true }
                            },
                            onSwitchLearner = {
                                showLearningExperience = false
                                showParentDashboard = false
                                showParentSettings = false
                                lifecycleScope.launch {
                                    userPreferences.clearSelectedLearner()
                                }
                            }
                        )
                    }
                }
            }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun requestParentGate(onAuthorized: () -> Unit) {
        val keyguardManager = getSystemService(KeyguardManager::class.java)
        if (keyguardManager == null || !keyguardManager.isDeviceSecure) {
            showParentGateSetupRequired()
            return
        }

        val credentialIntent = keyguardManager.createConfirmDeviceCredentialIntent(
            getString(R.string.parent_gate_title),
            getString(R.string.parent_gate_description)
        )
        if (credentialIntent == null) {
            showParentGateSetupRequired()
            return
        }

        pendingParentGateAction = onAuthorized
        parentGateLauncher.launch(credentialIntent)
    }

    private fun showParentGateSetupRequired() {
        AlertDialog.Builder(this)
            .setTitle(R.string.parent_gate_setup_title)
            .setMessage(R.string.parent_gate_setup_message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private companion object {
        const val PARENT_GATE_REQUEST_CODE = 7314
    }
}

@Composable
private fun LearnerProfileSelection(
    profiles: List<LearnerProfileEntity>,
    isLoading: Boolean,
    onSelect: (LearnerProfileEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.app_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.choose_learner), style = MaterialTheme.typography.titleMedium)
        Text(
            stringResource(R.string.learner_separation_note),
            style = MaterialTheme.typography.bodyMedium
        )

        when {
            isLoading -> Text(stringResource(R.string.preparing_profiles), style = MaterialTheme.typography.bodyMedium)
            profiles.isEmpty() -> Text(stringResource(R.string.no_active_profiles), style = MaterialTheme.typography.bodyMedium)
            else -> profiles.forEach { profile ->
                val displayName = localizedLearnerName(profile)
                val displayPathway = localizedPathway(profile.pathway)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSelect(profile) }
                ) {
                    Text(stringResource(R.string.learner_label, displayName, displayPathway))
                }
            }
        }
    }
}

@Composable
private fun LearnerHome(
    profile: LearnerProfileEntity,
    contentPackRepository: AssetContentPackRepository,
    learningAttemptDao: LearningAttemptDao,
    onStartLearning: () -> Unit,
    onOpenParentDashboard: () -> Unit,
    onSwitchLearner: () -> Unit
) {
    val displayName = localizedLearnerName(profile)
    val displayPathway = localizedPathway(profile.pathway)
    val attempts by learningAttemptDao.observeForLearner(profile.learnerId).collectAsState(initial = emptyList())
    var availableSubjects by remember(profile.learnerId) { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }
    var isLoadingSubjects by remember(profile.learnerId) { mutableStateOf(true) }
    LaunchedEffect(profile.learnerId, profile.pathway) {
        isLoadingSubjects = true
        val packId = ContentPackSelectionPolicy.packIdForPathway(profile.pathway)
        if (packId == null) {
            availableSubjects = emptyList()
            isLoadingSubjects = false
        } else {
            try {
                val pack = contentPackRepository.load(packId)
                val skillById = pack.skills.associateBy { it.skillId }
                availableSubjects = ContentPackSelectionPolicy.englishActivities(pack)
                    .groupBy { activity ->
                        val domain = skillById[activity.skillId]?.domain ?: "General practice"
                        ContentPackSelectionPolicy.subjectForDomain(domain, profile.pathway)
                    }
                    .map { (subject, subjectActivities) -> subject to subjectActivities.size }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                availableSubjects = emptyList()
            } finally {
                isLoadingSubjects = false
            }
        }
    }
    val practiceSummary = remember(profile.learnerId, attempts) {
        LearnerPracticeSummaryCalculator.summarize(profile.learnerId, attempts)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            stringResource(R.string.welcome_name, displayName),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            stringResource(R.string.learning_pathway, displayPathway),
            style = MaterialTheme.typography.titleMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(stringResource(R.string.learning_space_title), style = MaterialTheme.typography.titleLarge)
                Text(
                    stringResource(R.string.learning_space_description),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    stringResource(R.string.practice_summary_title),
                    style = MaterialTheme.typography.titleLarge
                )
                if (practiceSummary.scoredAttempts == 0) {
                    Text(
                        stringResource(R.string.practice_summary_empty),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        stringResource(R.string.practice_summary_scored_attempts, practiceSummary.scoredAttempts),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        stringResource(R.string.practice_summary_correct_answers, practiceSummary.correctAnswers),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        stringResource(R.string.practice_summary_accuracy, practiceSummary.accuracyPercent),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        stringResource(
                            R.string.practice_summary_hint_breakdown,
                            practiceSummary.independentCorrectAnswers,
                            practiceSummary.hintedCorrectAnswers
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onStartLearning
        ) {
            Text(stringResource(R.string.start_learning))
        }

        Text(stringResource(R.string.learning_areas), style = MaterialTheme.typography.titleLarge)
        when {
            isLoadingSubjects -> Text(
                stringResource(R.string.loading_learning_areas),
                style = MaterialTheme.typography.bodyMedium
            )
            availableSubjects.isEmpty() -> Text(
                stringResource(R.string.no_learning_areas),
                style = MaterialTheme.typography.bodyMedium
            )
            else -> availableSubjects.forEach { (subject, activityCount) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(subject, style = MaterialTheme.typography.titleMedium)
                        Text(
                            stringResource(R.string.available_activity_count, activityCount),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onOpenParentDashboard
        ) {
            Text(stringResource(R.string.open_parent_dashboard))
        }

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSwitchLearner
        ) {
            Text(stringResource(R.string.switch_learner))
        }
    }
}

@Composable
private fun localizedLearnerName(profile: LearnerProfileEntity): String =
    when (profile.learnerId) {
        "ali" -> stringResource(R.string.learner_ali)
        "aboudy" -> stringResource(R.string.learner_aboudy)
        else -> profile.displayName
    }

@Composable
private fun localizedPathway(pathway: String): String =
    when (pathway.trim().uppercase(Locale.ROOT)) {
        "KG2" -> stringResource(R.string.pathway_kg2)
        "GRADE 2", "GRADE_2", "GRADE2" -> stringResource(R.string.pathway_grade_2)
        else -> pathway
    }
