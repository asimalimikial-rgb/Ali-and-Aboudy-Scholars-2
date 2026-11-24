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
import com.asimalimikial.aas2.content.BookDefinition
import com.asimalimikial.aas2.content.ContentPack
import com.asimalimikial.aas2.content.ContentPackRepository
import com.asimalimikial.aas2.content.ContentPackSelectionPolicy
import com.asimalimikial.aas2.content.LessonDefinition
import com.asimalimikial.aas2.content.UnitDefinition
import com.asimalimikial.aas2.data.LearningAttemptDao
import com.asimalimikial.aas2.learning.ActivitySubmissionService
import kotlinx.coroutines.CancellationException

/** Loads the learner's offline pack and presents a subject → book → unit → lesson catalogue. */
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
    var selectedBookId by remember(packId) { mutableStateOf<String?>(null) }
    var selectedUnitId by remember(packId) { mutableStateOf<String?>(null) }
    var selectedLessonId by remember(packId) { mutableStateOf<String?>(null) }
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
    val selectedBook = loadedPack?.books?.singleOrNull { it.bookId == selectedBookId }
    val selectedUnit = loadedPack?.units?.singleOrNull { it.unitId == selectedUnitId }
    val selectedLesson = loadedPack?.lessons?.singleOrNull { it.lessonId == selectedLessonId }
    val bookUnits = remember(loadedPack, selectedBookId) {
        loadedPack?.units.orEmpty().filter { it.bookId == selectedBookId }.sortedBy { it.sequence }
    }
    val unitLessons = remember(loadedPack, selectedUnitId) {
        loadedPack?.lessons.orEmpty().filter { it.unitId == selectedUnitId }.sortedBy { it.sequence }
    }
    val lessonActivities = remember(activities, selectedLessonId) {
        activities.filter { it.lessonId == selectedLessonId }
    }

    fun navigateBack() {
        when {
            selectedActivityId != null -> selectedActivityId = null
            selectedLessonId != null -> selectedLessonId = null
            selectedUnitId != null -> selectedUnitId = null
            selectedBookId != null -> selectedBookId = null
            selectedDomain != null -> selectedDomain = null
            else -> onExit()
        }
    }

    BackHandler {
        navigateBack()
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
            onClick = { navigateBack() }
        ) {
            Text(
                stringResource(
                    when {
                        selectedActivityId != null -> R.string.back_to_activities
                        selectedLessonId != null -> R.string.back_to_lessons
                        selectedUnitId != null -> R.string.back_to_units
                        selectedBookId != null -> R.string.back_to_books
                        selectedDomain != null -> R.string.back_to_learning_areas
                        else -> R.string.back_to_learning_space
                    }
                )
            )
        }

        if (!isLoading && !loadError && loadedPack != null && selectedDomain == null && selectedBookId == null && selectedUnitId == null && selectedLessonId == null && selectedActivityId == null) {
            PersonalBookShelf(learnerPathway = learnerPathway)
        }

        when {
            isLoading -> {
                Text(stringResource(R.string.loading_content_pack), style = MaterialTheme.typography.bodyLarge)
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

            selectedLesson != null -> {
                Text(selectedLesson.title, style = MaterialTheme.typography.headlineSmall)
                lessonActivities.forEachIndexed { index, activity ->
                    ActivityCatalogueCard(
                        index = index + 1,
                        activity = activity,
                        isPractised = activity.activityId in practicedActivityIds,
                        onClick = { selectedActivityId = activity.activityId }
                    )
                }
                if (lessonActivities.isEmpty()) {
                    Text(stringResource(R.string.no_activities_available), style = MaterialTheme.typography.bodyLarge)
                }
            }

            selectedUnit != null -> {
                Text(stringResource(R.string.choose_lesson), style = MaterialTheme.typography.headlineSmall)
                unitLessons.forEach { lesson ->
                    val lessonActivitiesCount = activities.count { it.lessonId == lesson.lessonId }
                    val practicedCount = activities.count {
                        it.lessonId == lesson.lessonId && it.activityId in practicedActivityIds
                    }
                    LessonCard(
                        lesson = lesson,
                        practicedCount = practicedCount,
                        activityCount = lessonActivitiesCount,
                        onClick = { selectedLessonId = lesson.lessonId }
                    )
                }
            }

            selectedBook != null -> {
                Text(stringResource(R.string.choose_unit), style = MaterialTheme.typography.headlineSmall)
                bookUnits.forEach { unit ->
                    val unitActivityCount = activities.count { it.unitId == unit.unitId }
                    val practicedCount = activities.count {
                        it.unitId == unit.unitId && it.activityId in practicedActivityIds
                    }
                    UnitCard(
                        unit = unit,
                        practicedCount = practicedCount,
                        activityCount = unitActivityCount,
                        onClick = { selectedUnitId = unit.unitId }
                    )
                }
            }

            selectedDomain != null -> {
                Text(stringResource(R.string.choose_activity), style = MaterialTheme.typography.headlineSmall)
                val domainBooks = loadedPack.books.filter { book ->
                    ContentPackSelectionPolicy.subjectForDomain(book.subject, learnerPathway) == selectedDomain
                }
                domainBooks.forEach { book ->
                    val bookActivityCount = activities.count { it.bookId == book.bookId }
                    val practicedCount = activities.count {
                        it.bookId == book.bookId && it.activityId in practicedActivityIds
                    }
                    BookCard(
                        book = book,
                        practicedCount = practicedCount,
                        activityCount = bookActivityCount,
                        onClick = { selectedBookId = book.bookId }
                    )
                }
                val unassignedActivities = selectedDomainActivities.filter { it.bookId == null }
                if (unassignedActivities.isNotEmpty()) {
                    Text(
                        stringResource(R.string.additional_practice),
                        style = MaterialTheme.typography.titleMedium
                    )
                    unassignedActivities.forEachIndexed { index, activity ->
                        ActivityCatalogueCard(
                            index = index + 1,
                            activity = activity,
                            isPractised = activity.activityId in practicedActivityIds,
                            onClick = { selectedActivityId = activity.activityId }
                        )
                    }
                }
                if (domainBooks.isEmpty() && unassignedActivities.isEmpty()) {
                    Text(stringResource(R.string.no_activities_available), style = MaterialTheme.typography.bodyLarge)
                }
            }

            activities.isEmpty() -> {
                Text(stringResource(R.string.no_activities_available), style = MaterialTheme.typography.bodyLarge)
            }

            else -> {
                Text(stringResource(R.string.choose_learning_area), style = MaterialTheme.typography.headlineSmall)
                activitiesByDomain.forEach { (domain, domainActivities) ->
                    val practicedCount = domainActivities.count { it.activityId in practicedActivityIds }
                    LearningDomainCard(
                        domain = domain,
                        practicedCount = practicedCount,
                        activityCount = domainActivities.size,
                        onClick = {
                            selectedDomain = domain
                            selectedBookId = null
                            selectedUnitId = null
                            selectedLessonId = null
                        }
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
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
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
private fun BookCard(
    book: BookDefinition,
    practicedCount: Int,
    activityCount: Int,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(book.title, style = MaterialTheme.typography.titleLarge)
            Text(
                stringResource(R.string.learning_area_progress, practicedCount, activityCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun UnitCard(
    unit: UnitDefinition,
    practicedCount: Int,
    activityCount: Int,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(stringResource(R.string.unit_number_title, unit.sequence, unit.title), style = MaterialTheme.typography.titleLarge)
            Text(
                stringResource(R.string.learning_area_progress, practicedCount, activityCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LessonCard(
    lesson: LessonDefinition,
    practicedCount: Int,
    activityCount: Int,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(stringResource(R.string.lesson_number_title, lesson.sequence, lesson.title), style = MaterialTheme.typography.titleLarge)
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
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
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
