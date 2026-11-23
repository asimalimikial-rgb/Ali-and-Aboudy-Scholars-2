package com.asimalimikial.aas2.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.asimalimikial.aas2.R
import com.asimalimikial.aas2.data.ParentAppLanguagePolicy
import com.asimalimikial.aas2.data.UserPreferences
import kotlinx.coroutines.launch

/** Parent-only preferences. The caller must enter through the protected Parent Dashboard. */
@Composable
fun ParentSettingsScreen(
    userPreferences: UserPreferences,
    onBack: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnBack by rememberUpdatedState(onBack)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) currentOnBack()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val savedLanguage by userPreferences.parentAppLanguage.collectAsState(
        initial = ParentAppLanguagePolicy.SYSTEM
    )
    var selectedLanguage by remember(savedLanguage) { mutableStateOf(savedLanguage) }
    var isSaving by remember { mutableStateOf(false) }
    var saveError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    BackHandler(onBack = onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBack
        ) {
            Text(stringResource(R.string.back_to_parent_dashboard))
        }

        Text(
            stringResource(R.string.parent_settings_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            stringResource(R.string.parent_settings_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    stringResource(R.string.parent_settings_language_title),
                    style = MaterialTheme.typography.titleMedium
                )
                LanguageOption(
                    label = stringResource(R.string.parent_settings_language_system),
                    selected = selectedLanguage == ParentAppLanguagePolicy.SYSTEM,
                    onClick = { selectedLanguage = ParentAppLanguagePolicy.SYSTEM }
                )
                LanguageOption(
                    label = stringResource(R.string.parent_settings_language_english),
                    selected = selectedLanguage == ParentAppLanguagePolicy.ENGLISH,
                    onClick = { selectedLanguage = ParentAppLanguagePolicy.ENGLISH }
                )
                LanguageOption(
                    label = stringResource(R.string.parent_settings_language_arabic),
                    selected = selectedLanguage == ParentAppLanguagePolicy.ARABIC,
                    onClick = { selectedLanguage = ParentAppLanguagePolicy.ARABIC }
                )
            }
        }

        Text(
            stringResource(R.string.parent_settings_child_language_note),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            stringResource(R.string.parent_settings_audio_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (saveError) {
            Text(
                stringResource(R.string.parent_settings_save_error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving,
            onClick = {
                isSaving = true
                saveError = false
                scope.launch {
                    try {
                        userPreferences.setParentAppLanguage(selectedLanguage)
                        onBack()
                    } catch (_: Exception) {
                        saveError = true
                    } finally {
                        isSaving = false
                    }
                }
            }
        ) {
            Text(
                stringResource(
                    if (isSaving) R.string.parent_settings_saving else R.string.parent_settings_save
                )
            )
        }
    }
}

@Composable
private fun LanguageOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(10.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}
