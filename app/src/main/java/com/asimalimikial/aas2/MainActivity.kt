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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
            val selectedProfile = LearnerSelectionPolicy.selectedProfile(selectedLearnerId, profiles)
            var showLearningExperience by remember { mutableStateOf(false) }
            var showParentDashboard by remember { mutableStateOf(false) }
            var showParentSettings by remember { mutableStateOf(false) }
            val parentSurfaceActive = showParentDashboard || showParentSettings
            val appLocale = remember(parentAppLanguage, parentSurfaceActive) {
                ParentAppLanguagePolicy.localeForActiveScreen(parentSurfaceActive, parentAppLanguage)
            }
            val baseConfiguration = LocalConfiguration.current
            val localizedConfiguration = remember(baseConfiguration, appLocale) {
                Configuration(baseConfiguration).apply {
                    setLocale(appLocale)
                    setLayoutDirection(appLocale)
                }
            }
            val baseContext = LocalContext.current
            val localizedContext = remember(baseContext, localizedConfiguration) {
                baseContext.createConfigurationContext(localizedConfiguration)
            }

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
                LocalLayoutDirection provides if (ParentAppLanguagePolicy.isRightToLeft(appLocale)) {
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
                            onBack = { showParentSettings = false },
                            onLeaveForeground = {
                                showParentSettings = false
                                showParentDashboard = false
                            }
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
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF4EC), Color(0xFFF4F7FF), Color(0xFFEAF5F2))))
            .verticalScroll(androidx.compose.foundation.rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 30.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text("ALI & ABOUDY SCHOLARS", color = Color(0xFF71829A), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold)
        Text(stringResource(R.string.app_title), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = Color(0xFF263D5B))
        Text(stringResource(R.string.choose_learner), style = MaterialTheme.typography.titleLarge, color = Color(0xFF52647C), fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.learner_separation_note), style = MaterialTheme.typography.bodyMedium, color = Color(0xFF78869A))
        when {
            isLoading -> Text(stringResource(R.string.preparing_profiles), style = MaterialTheme.typography.bodyLarge)
            profiles.isEmpty() -> Text(stringResource(R.string.no_active_profiles), style = MaterialTheme.typography.bodyLarge)
            else -> profiles.forEach { profile ->
                val isAli = profile.learnerId == "ali"
                val displayName = localizedLearnerName(profile)
                val displayPathway = localizedPathway(profile.pathway)
                val accent = if (isAli) Color(0xFFE87968) else Color(0xFF4D88CF)
                val tint = if (isAli) Color(0xFFFFF0EA) else Color(0xFFEDF4FF)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = tint),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(if (isAli) "🌳" else "🚀", style = MaterialTheme.typography.displayMedium)
                        Text(displayName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = accent)
                        Text(displayPathway, style = MaterialTheme.typography.titleMedium, color = Color(0xFF52647C))
                        Text(if (isAli) "Letters, sounds, numbers and little discoveries." else "Reading, reasoning, science and bigger discoveries.",
                            style = MaterialTheme.typography.bodyMedium, color = Color(0xFF6F7D90))
                        Button(modifier = Modifier.fillMaxWidth().height(58.dp), onClick = { onSelect(profile) },
                            shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(containerColor = accent)) {
                            Text(stringResource(R.string.learner_label, displayName, displayPathway), fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
        Text("A personal learning journey for each child", color = Color(0xFF8793A4), style = MaterialTheme.typography.labelMedium)
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
    val isAli = profile.learnerId == "ali"
    val accent = if (isAli) Color(0xFFE87968) else Color(0xFF4D88CF)
    val soft = if (isAli) Color(0xFFFFF0EA) else Color(0xFFEDF4FF)
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
    val accuracy = practiceSummary.accuracyPercent
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(soft, Color(0xFFF8FAFD), Color.White)))
            .verticalScroll(androidx.compose.foundation.rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("YOUR LEARNING WORLD", color = accent, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
                Text(stringResource(R.string.welcome_name, displayName), style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black, color = Color(0xFF263D5B))
                Text(displayPathway, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF77859A))
            }
            OutlinedButton(onClick = onSwitchLearner, shape = RoundedCornerShape(16.dp)) { Text("⇄") }
        }
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF263D5B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(if (isAli) "🌟" else "🚀", style = MaterialTheme.typography.displaySmall)
                Text(if (practiceSummary.scoredAttempts == 0) "Your adventure is ready!" else "Look how far you have come!",
                    style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.ExtraBold)
                Text(if (practiceSummary.scoredAttempts == 0) "Choose a world and start collecting discoveries."
                    else "Every activity is another step in your learning journey.",
                    style = MaterialTheme.typography.bodyMedium, color = Color(0xFFDCE7F2))
                Button(modifier = Modifier.fillMaxWidth().height(60.dp), onClick = onStartLearning,
                    shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(containerColor = accent)) {
                    Text(stringResource(R.string.start_learning), fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(Triple(practiceSummary.scoredAttempts.toString(), "Activities", "📚"),
                Triple(practiceSummary.correctAnswers.toString(), "Correct", "⭐"),
                Triple(if (practiceSummary.scoredAttempts == 0) "—" else "$accuracy%", "Accuracy", "🎯")).forEach { item ->
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp, horizontal = 7.dp),
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(item.third, style = MaterialTheme.typography.titleLarge)
                        Text(item.first, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = accent)
                        Text(item.second, style = MaterialTheme.typography.labelSmall, color = Color(0xFF8491A2))
                    }
                }
            }
        }
        Text(stringResource(R.string.learning_areas), style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black, color = Color(0xFF263D5B))
        when {
            isLoadingSubjects -> Text(stringResource(R.string.loading_learning_areas), style = MaterialTheme.typography.bodyMedium)
            availableSubjects.isEmpty() -> Text(stringResource(R.string.no_learning_areas), style = MaterialTheme.typography.bodyMedium)
            else -> availableSubjects.forEachIndexed { index, (subject, activityCount) ->
                val icon = when {
                    subject.contains("English", ignoreCase = true) || subject.contains("Language", ignoreCase = true) -> "📖"
                    subject.contains("Math", ignoreCase = true) || subject.contains("Numeracy", ignoreCase = true) -> "🔢"
                    subject.contains("Science", ignoreCase = true) || subject.contains("Discovery", ignoreCase = true) -> "🔬"
                    else -> listOf("🧩", "🎨", "🌱")[index % 3]
                }
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(icon, style = MaterialTheme.typography.headlineMedium)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(subject, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF314763))
                            Text(stringResource(R.string.available_activity_count, activityCount), style = MaterialTheme.typography.bodySmall, color = Color(0xFF8290A2))
                        }
                        Text("›", style = MaterialTheme.typography.headlineMedium, color = accent)
                    }
                }
            }
        }
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7E5))) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("🌱  Keep growing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF80602B))
                Text(if (practiceSummary.scoredAttempts == 0) "Try your first activity. Every great journey starts with one step."
                    else "You have completed " + practiceSummary.scoredAttempts + " activities. Keep exploring at your own pace.",
                    style = MaterialTheme.typography.bodyMedium, color = Color(0xFF8A744B))
            }
        }
        OutlinedButton(modifier = Modifier.fillMaxWidth().height(54.dp), onClick = onOpenParentDashboard,
            shape = RoundedCornerShape(17.dp)) { Text(stringResource(R.string.open_parent_dashboard), fontWeight = FontWeight.Bold) }
        OutlinedButton(modifier = Modifier.fillMaxWidth().height(50.dp), onClick = onSwitchLearner,
            shape = RoundedCornerShape(17.dp)) { Text(stringResource(R.string.switch_learner)) }
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
