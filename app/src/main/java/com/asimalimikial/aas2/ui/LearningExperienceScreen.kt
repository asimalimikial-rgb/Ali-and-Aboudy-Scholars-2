package com.asimalimikial.aas2.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.saveable.rememberSaveable
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
    val attemptsFlow = remember(learnerId) { learningAttemptDao.observeForLearner(learnerId) }
    val attempts by attemptsFlow.collectAsState(initial = emptyList())
    var contentPack by remember(packId) { mutableStateOf<ContentPack?>(null) }
    var isLoading by remember(packId) { mutableStateOf(true) }
    var loadError by remember(packId) { mutableStateOf(false) }
    var selectedDomain by rememberSaveable(packId) { mutableStateOf<String?>(null) }
    var selectedBookId by rememberSaveable(packId) { mutableStateOf<String?>(null) }
    var selectedUnitId by rememberSaveable(packId) { mutableStateOf<String?>(null) }
    var selectedLessonId by rememberSaveable(packId) { mutableStateOf<String?>(null) }
    var selectedActivityId by rememberSaveable(packId) { mutableStateOf<String?>(null) }

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
    val domainBooks = remember(loadedPack, selectedDomain, learnerPathway) {
        loadedPack?.books.orEmpty().filter { book ->
            ContentPackSelectionPolicy.subjectForDomain(book.subject, learnerPathway) == selectedDomain
        }
    }
    val selectedDomainUnassignedActivities = remember(selectedDomainActivities) {
        selectedDomainActivities.filter { it.bookId == null }
    }
    val lessonActivities = remember(activities, selectedLessonId) {
        activities.filter { it.lessonId == selectedLessonId }
    }
    val selectedBookActivities = remember(activities, selectedBookId) {
        activities.filter { it.bookId == selectedBookId }
    }
    val selectedActivitySequence = remember(
        lessonActivities,
        selectedDomainUnassignedActivities,
        selectedLessonId
    ) {
        if (selectedLessonId != null) lessonActivities
        else selectedDomainUnassignedActivities
    }
    val selectedActivityPosition = selectedActivitySequence.indexOfFirst {
        it.activityId == selectedActivityId
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
            .background(
                Brush.verticalGradient(
                    listOf(
                        if (learnerPathway == "KG2") Color(0xFFFFF4EC) else Color(0xFFEDF4FF),
                        Color(0xFFF8FAFD),
                        Color.White
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF263D5B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("🗺️  YOUR LEARNING WORLD", style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFD7E8F6))
                Text(
                    when {
                        selectedActivity != null -> "One question at a time"
                        selectedLesson != null -> selectedLesson.title?.takeIf { it.isNotBlank() } ?: "Lesson"
                        selectedUnit != null -> selectedUnit.title?.takeIf { it.isNotBlank() } ?: "Unit"
                        selectedBook != null -> selectedBook.title?.takeIf { it.isNotBlank() } ?: "Book"
                        selectedDomain != null -> selectedDomain?.takeIf { it.isNotBlank() } ?: "Learning"
                        else -> if (learnerPathway == "KG2") "Ali's adventure map" else "Aboudy's discovery map"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text("Explore • Practise • Discover", style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD7E8F6))
            }
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navigateBack() },
            shape = RoundedCornerShape(16.dp)
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
                if (selectedActivityPosition >= 0) {
                    Text(
                        stringResource(
                            R.string.activity_sequence_progress,
                            selectedActivityPosition + 1,
                            selectedActivitySequence.size
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                ActivitySession(
                    learnerId = learnerId,
                    contentPack = loadedPack,
                    activity = selectedActivity,
                    submissionService = submissionService,
                    hasNextActivity = selectedActivityPosition >= 0 &&
                        selectedActivityPosition < selectedActivitySequence.lastIndex,
                    onNextActivity = {
                        selectedActivitySequence.getOrNull(selectedActivityPosition + 1)
                            ?.let { next -> selectedActivityId = next.activityId }
                            ?: run { selectedActivityId = null }
                    },
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
                    Text(
                        stringResource(R.string.lesson_activities_in_preparation),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            selectedUnit != null -> {
                Text(stringResource(R.string.choose_lesson), style = MaterialTheme.typography.headlineSmall)
                unitLessons.forEach { lesson ->
                    val lessonItems = activities.filter { it.lessonId == lesson.lessonId }
                    val practicedCount = lessonItems.count { it.activityId in practicedActivityIds }
                    LessonCard(
                        lesson = lesson,
                        practicedCount = practicedCount,
                        activityCount = lessonItems.size,
                        onClick = { selectedLessonId = lesson.lessonId }
                    )
                }
            }

            selectedBook != null -> {
                Text(stringResource(R.string.choose_unit), style = MaterialTheme.typography.headlineSmall)
                bookUnits.forEach { unit ->
                    val unitItems = activities.filter { it.unitId == unit.unitId }
                    val practicedCount = unitItems.count { it.activityId in practicedActivityIds }
                    UnitCard(
                        unit = unit,
                        practicedCount = practicedCount,
                        activityCount = unitItems.size,
                        onClick = { selectedUnitId = unit.unitId }
                    )
                }
                if (bookUnits.isEmpty() || selectedBookActivities.isEmpty()) {
                    Text(
                        stringResource(R.string.book_activities_in_preparation),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (selectedDomainUnassignedActivities.isNotEmpty()) {
                    Text(
                        stringResource(R.string.additional_practice),
                        style = MaterialTheme.typography.titleMedium
                    )
                    selectedDomainUnassignedActivities.forEachIndexed { index, activity ->
                        ActivityCatalogueCard(
                            index = index + 1,
                            activity = activity,
                            isPractised = activity.activityId in practicedActivityIds,
                            onClick = { selectedActivityId = activity.activityId }
                        )
                    }
                }
            }

            selectedDomain != null -> {
                Text(stringResource(R.string.choose_activity), style = MaterialTheme.typography.headlineSmall)
                domainBooks.forEach { book ->
                    val bookItems = activities.filter { it.bookId == book.bookId }
                    val practicedCount = bookItems.count { it.activityId in practicedActivityIds }
                    BookCard(
                        book = book,
                        practicedCount = practicedCount,
                        activityCount = bookItems.size,
                        onClick = { selectedBookId = book.bookId }
                    )
                }
                val unassignedActivities = selectedDomainUnassignedActivities
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
                            val matchingBooks = loadedPack.books.filter { book ->
                                ContentPackSelectionPolicy.subjectForDomain(book.subject, learnerPathway) == domain
                            }
                            selectedDomain = domain
                            selectedBookId = matchingBooks.singleOrNull()?.bookId
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
    val icon = when {
        domain.contains("English", true) || domain.contains("Language", true) -> "📖"
        domain.contains("Math", true) || domain.contains("Numeracy", true) -> "🔢"
        domain.contains("Science", true) || domain.contains("Discovery", true) -> "🔬"
        else -> "🌍"
    }
    val tint = when (icon) {
        "📖" -> Color(0xFFFFF0EA)
        "🔢" -> Color(0xFFEAF6F2)
        "🔬" -> Color(0xFFEDF3FF)
        else -> Color(0xFFFFF7E5)
    }
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(17.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = tint)) {
                Text(icon, modifier = Modifier.padding(13.dp), style = MaterialTheme.typography.headlineMedium)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(domain, style = MaterialTheme.typography.titleLarge, color = Color(0xFF263D5B))
                Text(stringResource(R.string.learning_area_progress, practicedCount, activityCount),
                    style = MaterialTheme.typography.bodySmall, color = Color(0xFF8290A2))
            }
            Text("›", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF7891AC))
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
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)) {
            Text("📚", style = MaterialTheme.typography.headlineMedium)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium, color = Color(0xFF263D5B))
                Text(stringResource(R.string.learning_area_progress, practicedCount, activityCount),
                    style = MaterialTheme.typography.bodySmall, color = Color(0xFF8290A2))
            }
            Text("›", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF7891AC))
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
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)) {
            Text("🗺️", style = MaterialTheme.typography.headlineMedium)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(stringResource(R.string.unit_number_title, unit.sequence, unit.title),
                    style = MaterialTheme.typography.titleMedium, color = Color(0xFF263D5B))
                Text(stringResource(R.string.learning_area_progress, practicedCount, activityCount),
                    style = MaterialTheme.typography.bodySmall, color = Color(0xFF8290A2))
            }
            Text("›", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF7891AC))
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
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)) {
            Text("✨", style = MaterialTheme.typography.headlineMedium)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(stringResource(R.string.lesson_number_title, lesson.sequence, lesson.title),
                    style = MaterialTheme.typography.titleMedium, color = Color(0xFF263D5B))
                Text(stringResource(R.string.learning_area_progress, practicedCount, activityCount),
                    style = MaterialTheme.typography.bodySmall, color = Color(0xFF8290A2))
            }
            Text("›", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF7891AC))
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
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)) {
            Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D8))) {
                Text(index.toString(), modifier = Modifier.padding(horizontal = 15.dp, vertical = 11.dp),
                    style = MaterialTheme.typography.titleLarge, color = Color(0xFF946B22))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(activity.prompt, style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF263D5B), maxLines = 3, overflow = TextOverflow.Ellipsis)
                if (isPractised) {
                    Text(stringResource(R.string.activity_practised), style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF398A6A))
                } else {
                    Text("Tap to start", style = MaterialTheme.typography.labelMedium, color = Color(0xFF8290A2))
                }
            }
            Text("▶", style = MaterialTheme.typography.titleLarge, color = Color(0xFF7891AC))
        }
    }
}
