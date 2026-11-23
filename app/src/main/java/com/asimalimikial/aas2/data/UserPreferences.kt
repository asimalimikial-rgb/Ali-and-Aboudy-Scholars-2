package com.asimalimikial.aas2.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    private val selectedLearnerKey = stringPreferencesKey("selected_learner_id")
    private val parentAppLanguageKey = stringPreferencesKey("parent_app_language")

    val selectedLearnerId: Flow<String?> =
        context.dataStore.data.map { preferences: Preferences ->
            preferences[selectedLearnerKey]
        }

    val parentAppLanguage: Flow<String> =
        context.dataStore.data.map { preferences ->
            ParentAppLanguagePolicy.normalize(preferences[parentAppLanguageKey])
        }

    suspend fun setParentAppLanguage(languageTag: String) {
        require(languageTag in ParentAppLanguagePolicy.supportedTags) {
            "Unsupported parent app language: $languageTag"
        }
        context.dataStore.edit { preferences ->
            preferences[parentAppLanguageKey] = languageTag
        }
    }

    suspend fun setSelectedLearner(learnerId: String) {
        require(learnerId.isNotBlank()) { "learnerId must not be blank" }
        context.dataStore.edit { preferences ->
            preferences[selectedLearnerKey] = learnerId
        }
    }

    suspend fun clearSelectedLearner() {
        context.dataStore.edit { preferences ->
            preferences.remove(selectedLearnerKey)
        }
    }
}
