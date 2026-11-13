package com.asimalimikial.aas2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.asimalimikial.aas2.data.AppDatabase
import com.asimalimikial.aas2.data.LearnerProfileEntity
import com.asimalimikial.aas2.data.LearnerSelectionPolicy
import com.asimalimikial.aas2.data.UserPreferences
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext)
        val profileDao = database.learnerProfileDao()
        val userPreferences = UserPreferences(applicationContext)

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
            val selectedProfile = LearnerSelectionPolicy.selectedProfile(selectedLearnerId, profiles)

            LaunchedEffect(selectedLearnerId, profiles) {
                if (LearnerSelectionPolicy.shouldClearSelection(selectedLearnerId, profiles)) {
                    userPreferences.clearSelectedLearner()
                }
            }

            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (selectedProfile == null) {
                        LearnerProfileSelection(
                            profiles = LearnerSelectionPolicy.activeProfiles(profiles),
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
                    } else {
                        LearnerHome(
                            profile = selectedProfile,
                            onSwitchLearner = {
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

@Composable
private fun LearnerProfileSelection(
    profiles: List<LearnerProfileEntity>,
    onSelect: (LearnerProfileEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Ali & Aboudy Scholars", style = MaterialTheme.typography.headlineMedium)
        Text("Choose a learner", style = MaterialTheme.typography.titleMedium)
        Text(
            "Each learner has a separate profile and learning history.",
            style = MaterialTheme.typography.bodyMedium
        )

        if (profiles.isEmpty()) {
            Text("No active learner profiles are available.", style = MaterialTheme.typography.bodyMedium)
        } else {
            profiles.forEach { profile ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSelect(profile) }
                ) {
                    Text("${profile.displayName} · ${profile.pathway}")
                }
            }
        }
    }
}

@Composable
private fun LearnerHome(
    profile: LearnerProfileEntity,
    onSwitchLearner: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Welcome, ${profile.displayName}!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Your learning pathway: ${profile.pathway}",
            style = MaterialTheme.typography.titleMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Your learning space", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Your activities and progress are stored separately from the other learner.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Text("Learning areas", style = MaterialTheme.typography.titleLarge)
        listOf("English & Literacy", "Mathematics", "Science & Discovery", "Thinking & Creativity")
            .forEach { subject ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(subject, style = MaterialTheme.typography.titleMedium)
                        Text("In preparation", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSwitchLearner
        ) {
            Text("Switch learner")
        }
    }
}
