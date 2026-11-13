package com.asimalimikial.aas2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.asimalimikial.aas2.data.AppDatabase
import com.asimalimikial.aas2.data.LearnerProfileEntity
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

            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LearnerProfileSelection(
                        profiles = profiles,
                        selectedLearnerId = selectedLearnerId,
                        onSelect = { profile ->
                            lifecycleScope.launch {
                                // Persist only a learner that still exists in the local profile table.
                                if (profileDao.findById(profile.learnerId) != null) {
                                    userPreferences.setSelectedLearner(profile.learnerId)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LearnerProfileSelection(
    profiles: List<LearnerProfileEntity>,
    selectedLearnerId: String?,
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

        profiles.forEach { profile ->
            val isSelected = profile.learnerId == selectedLearnerId
            Button(onClick = { onSelect(profile) }) {
                Text(
                    if (isSelected) {
                        "✓ ${profile.displayName} · ${profile.pathway}"
                    } else {
                        "${profile.displayName} · ${profile.pathway}"
                    }
                )
            }
        }
    }
}
