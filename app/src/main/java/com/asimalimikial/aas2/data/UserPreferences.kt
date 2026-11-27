package com.asimalimikial.aas2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(context: Context) {
    private val appContext = context.applicationContext
    private val selectedLearnerKey = stringPreferencesKey("selected_learner_id")
    private val parentAppLanguageKey = stringPreferencesKey("parent_app_language")

    val selectedLearnerId: Flow<String?> = appContext.dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> preferences[selectedLearnerKey] }

    val parentAppLanguage: Flow<String> = appContext.dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> ParentAppLanguagePolicy.normalize(preferences[parentAppLanguageKey]) }

    suspend fun setParentAppLanguage(languageTag: String) {
        val normalized = ParentAppLanguagePolicy.normalize(languageTag)
        appContext.dataStore.edit { preferences -> preferences[parentAppLanguageKey] = normalized }
    }

    suspend fun setSelectedLearner(learnerId: String) {
        require(learnerId.isNotBlank()) { "learnerId must not be blank" }
        appContext.dataStore.edit { preferences -> preferences[selectedLearnerKey] = learnerId }
    }

    suspend fun clearSelectedLearner() {
        appContext.dataStore.edit { preferences -> preferences.remove(selectedLearnerKey) }
    }
}
