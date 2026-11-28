package com.asimalimikial.aas2.content

import java.net.URI

object ContentPackValidator {
    // Schema 4 adds a book → unit → lesson hierarchy and reviewed activity metadata.
    // Schema 3 remains readable so existing learner packs and progress stay intact.
    private val supportedSchemaVersions = setOf(3, 4)
    private val supportedLearnerPaths = setOf("KG2", "GRADE_2")
    private val supportedActivityTypes = setOf("SINGLE_CHOICE")
    private val supportedLanguages = setOf("en", "ar")
    private val supportedAssessmentPhases = setOf(
        "DIAGNOSTIC", "GUIDED", "INDEPENDENT", "TRANSFER", "DELAYED_REVIEW"
    )
    private val supportedUsageRights = setOf(
        "REFERENCE_ONLY",
        "REDISTRIBUTION_ALLOWED",
        "ADAPTATION_ALLOWED",
        "PERMISSION_REQUIRED",
        "PROHIBITED"
    )

    fun validate(pack: ContentPack): ContentPackValidation {
        val errors = buildList {
            if (pack.packId.isBlank()) add("packId must not be blank")
            if (pack.version.isBlank()) add("version must not be blank")
            if (pack.schemaVersion !in supportedSchemaVersions) add("Unsupported schemaVersion: ${pack.schemaVersion}")
            if (pack.learnerPath !in supportedLearnerPaths) add("Unsupported learnerPath: ${pack.learnerPath}")
            if (pack.skills.isEmpty()) add("A content pack must contain at least one skill")
            if (pack.sourceReferences.isEmpty()) add("A content pack must contain at least one source reference")

            val sourceIds = pack.sourceReferences.map { it.sourceReferenceId }
            if (sourceIds.any(String::isBlank)) add("sourceReferenceId must not be blank")
            if (sourceIds.size != sourceIds.toSet().size) add("sourceReferenceId values must be unique")

            pack.sourceReferences.forEachIndexed { index, source ->
                if (source.title.isBlank()) add("Source at index $index has an empty title")
                if (source.publisher.isBlank()) add("Source at index $index has an empty publisher")
                if (!isValidHttpsUrl(source.url)) add("Source at index $index must have a valid HTTPS URL")
                if (source.license.isBlank()) add("Source at index $index has no license or rights statement")
                if (source.licenseUrl != null && !isValidHttpsUrl(source.licenseUrl)) {
                    add("Source at index $index has an invalid HTTPS license URL")
                }
                if (source.usageRights !in supportedUsageRights) add("Source at index $index has unsupported usageRights")
                if (source.usageRights in setOf("REDISTRIBUTION_ALLOWED", "ADAPTATION_ALLOWED") &&
                    source.attribution.isNullOrBlank()
                ) add("Source at index $index requires attribution metadata")
                if (source.verifiedAt != null && !isValidIsoDate(source.verifiedAt)) {
                    add("Source at index $index verifiedAt must be a valid YYYY-MM-DD date")
                }
            }

            val skillIds = pack.skills.map { it.skillId }
            if (skillIds.any(String::isBlank)) add("skillId must not be blank")
            if (skillIds.size != skillIds.toSet().size) add("skillId values must be unique")

            pack.skills.forEachIndexed { index, skill ->
                if (skill.learnerPath != pack.learnerPath) add("Skill at index $index belongs to a different learner path")
                if (skill.domain.isBlank()) add("Skill at index $index has an empty domain")
                if (skill.observableOutcome.isBlank()) add("Skill at index $index has an empty observable outcome")
                if (skill.sourceReferenceIds.isEmpty()) add("Skill at index $index must reference at least one sourceReferenceId")
                if (skill.sourceReferenceIds.any(String::isBlank)) add("Skill at index $index contains a blank source reference ID")
                if (skill.sourceReferenceIds.size != skill.sourceReferenceIds.toSet().size) {
                    add("Skill at index $index contains duplicate source reference IDs")
                }
                if (skill.sourceReferenceIds.any { it !in sourceIds }) {
                    add("Skill at index $index references an unknown sourceReferenceId")
                }
            }

            if (pack.schemaVersion == 4) {
                if (pack.books.isEmpty()) add("Schema 4 requires at least one book")
                if (pack.units.isEmpty()) add("Schema 4 requires at least one unit")
                if (pack.lessons.isEmpty()) add("Schema 4 requires at least one lesson")

                val bookIds = pack.books.map { it.bookId }
                val unitIds = pack.units.map { it.unitId }
                val lessonIds = pack.lessons.map { it.lessonId }
                if (bookIds.any(String::isBlank) || bookIds.size != bookIds.toSet().size) {
                    add("Schema 4 book IDs must be nonblank and unique")
                }
                if (unitIds.any(String::isBlank) || unitIds.size != unitIds.toSet().size) {
                    add("Schema 4 unit IDs must be nonblank and unique")
                }
                if (lessonIds.any(String::isBlank) || lessonIds.size != lessonIds.toSet().size) {
                    add("Schema 4 lesson IDs must be nonblank and unique")
                }

                pack.books.forEachIndexed { index, book ->
                    if (book.title.isBlank() || book.subject.isBlank()) add("Book at index $index has blank title or subject")
                    if (book.learnerPath != pack.learnerPath) add("Book at index $index belongs to a different learner path")
                    if (book.sourceReferenceIds.isEmpty() || book.sourceReferenceIds.any { it !in sourceIds }) {
                        add("Book at index $index must reference known source IDs")
                    }
                }

                pack.units.forEachIndexed { index, unit ->
                    if (unit.bookId !in bookIds) add("Unit at index $index references an unknown bookId")
                    if (unit.title.isBlank() || unit.sequence < 1) add("Unit at index $index has invalid title or sequence")
                    if (unit.lessonIds.isEmpty() || unit.lessonIds.size != unit.lessonIds.toSet().size) {
                        add("Unit at index $index must list unique lesson IDs")
                    }
                    if (unit.lessonIds.any { it !in lessonIds }) add("Unit at index $index references an unknown lessonId")
                }

                pack.lessons.forEachIndexed { index, lesson ->
                    if (lesson.unitId !in unitIds) add("Lesson at index $index references an unknown unitId")
                    if (lesson.title.isBlank() || lesson.sequence < 1) add("Lesson at index $index has invalid title or sequence")
                    if (lesson.skillIds.isEmpty() || lesson.skillIds.size != lesson.skillIds.toSet().size) {
                        add("Lesson at index $index must list unique skill IDs")
                    }
                    if (lesson.skillIds.any { it !in skillIds }) add("Lesson at index $index references an unknown skillId")
                }
                pack.units.forEachIndexed { index, unit ->
                    val lessonsForUnit = pack.lessons.filter { it.unitId == unit.unitId }.map { it.lessonId }.toSet()
                    if (unit.lessonIds.toSet() != lessonsForUnit) {
                        add("Unit at index $index lessonIds must match lessons assigned to the unit")
                    }
                }
            }

            val activityIds = pack.activities.map { it.activityId }
            if (activityIds.any(String::isBlank)) add("activityId must not be blank")
            if (activityIds.size != activityIds.toSet().size) add("activityId values must be unique")

            val knownSkillIds = skillIds.toSet()
            pack.activities.forEachIndexed { index, activity ->
                if (activity.skillId !in knownSkillIds) add("Activity at index $index references an unknown skillId")
                if (activity.activityType !in supportedActivityTypes) add("Activity at index $index has an unsupported activityType")
                if (activity.language !in supportedLanguages) add("Activity at index $index has an unsupported language")
                if (activity.language == "en") {
                    val learnerText = listOf(activity.prompt, activity.feedbackCorrect, activity.feedbackIncorrect) +
                        activity.options.map { it.text } + activity.optionRationales.values
                    if (learnerText.any(::containsArabicScript)) {
                        add("Activity at index $index contains Arabic script in English learner content")
                    }
                }
                if (activity.prompt.isBlank()) add("Activity at index $index has an empty prompt")
                if (activity.feedbackCorrect.isBlank()) add("Activity at index $index has empty correct feedback")
                if (activity.feedbackIncorrect.isBlank()) add("Activity at index $index has empty incorrect feedback")
                if (activity.difficulty !in 1..5) add("Activity at index $index difficulty must be between 1 and 5")

                val optionIds = activity.options.map { it.optionId }
                if (activity.options.size < 2) add("Activity at index $index must contain at least two options")
                if (optionIds.any(String::isBlank)) add("Activity at index $index contains a blank optionId")
                if (optionIds.size != optionIds.toSet().size) add("Activity at index $index optionId values must be unique")
                if (activity.options.any { it.text.isBlank() && it.imageAssetPath.isNullOrBlank() }) {
                    add("Activity at index $index contains an option without text or image")
                }
                if (activity.options.any { it.imageAssetPath != null && !isSafeImageAssetPath(it.imageAssetPath) }) {
                    add("Activity at index $index contains an unsafe image asset path")
                }
                if (activity.promptImageAssetPath != null && !isSafeImageAssetPath(activity.promptImageAssetPath)) {
                    add("Activity at index $index contains an unsafe prompt image asset path")
                }
                if (optionIds.count { it == activity.correctOptionId } != 1) {
                    add("Activity at index $index must identify exactly one correct option")
                }

                val hasBookMetadata = activity.bookId != null || activity.unitId != null || activity.lessonId != null
                if (hasBookMetadata) {
                    val knownBookIds = pack.books.map { it.bookId }.toSet()
                    if (activity.bookId == null || activity.bookId !in knownBookIds) {
                        add("Activity at index $index references an unknown bookId")
                    }
                    val unit = activity.unitId?.let { id -> pack.units.singleOrNull { it.unitId == id } }
                    val lesson = activity.lessonId?.let { id -> pack.lessons.singleOrNull { it.lessonId == id } }
                    if (unit == null) add("Activity at index $index references an unknown unitId")
                    if (lesson == null) add("Activity at index $index references an unknown lessonId")
                    if (unit != null && unit.bookId != activity.bookId) add("Activity at index $index bookId does not match its unit")
                    if (lesson != null && lesson.unitId != activity.unitId) add("Activity at index $index unitId does not match its lesson")
                    if (lesson != null && activity.skillId !in lesson.skillIds) {
                        add("Activity at index $index skillId is not assigned to its lesson")
                    }
                    if (activity.assessmentPhase == null || activity.assessmentPhase !in supportedAssessmentPhases) {
                        add("Activity at index $index has an unsupported or missing assessmentPhase")
                    }
                    if (activity.misconceptionCode.isNullOrBlank()) add("Activity at index $index has no misconceptionCode")
                    if (activity.promptImageAssetPath.isNullOrBlank()) add("Activity at index $index has no unique prompt image")
                    if (activity.promptAudioAssetPath.isNullOrBlank()) add("Activity at index $index has no prompt audio")
                    if (activity.feedbackCorrectAudioAssetPath.isNullOrBlank() ||
                        activity.feedbackIncorrectAudioAssetPath.isNullOrBlank()
                    ) add("Activity at index $index is missing feedback audio")
                    if (activity.optionAudioAssetPaths.keys != optionIds.toSet() ||
                        activity.optionAudioAssetPaths.values.any { !isSafeAssetPath(it) }
                    ) add("Activity at index $index must have one safe audio asset for each option")
                    if (activity.promptAudioAssetPath != null && !isSafeAssetPath(activity.promptAudioAssetPath)) {
                        add("Activity at index $index contains an unsafe prompt audio asset path")
                    }
                    if (activity.feedbackCorrectAudioAssetPath != null && !isSafeAssetPath(activity.feedbackCorrectAudioAssetPath)) {
                        add("Activity at index $index contains an unsafe correct-feedback audio path")
                    }
                    if (activity.feedbackIncorrectAudioAssetPath != null && !isSafeAssetPath(activity.feedbackIncorrectAudioAssetPath)) {
                        add("Activity at index $index contains an unsafe incorrect-feedback audio path")
                    }
                    if (activity.optionRationales.keys != optionIds.toSet() ||
                        activity.optionRationales.values.any(String::isBlank)
                    ) add("Activity at index $index must have one nonblank rationale for each option")
                }
            }
        }

        val guidedTaskIds = pack.guidedTasks.map { it.activityId }
        if (guidedTaskIds.any(String::isBlank)) add("guided task activityId must not be blank")
        if (guidedTaskIds.size != guidedTaskIds.toSet().size) add("guided task activityId values must be unique")
        if (guidedTaskIds.any { it in activityIds }) add("guided task IDs must not collide with scored activity IDs")
        val guidedBookIds = pack.books.map { it.bookId }.toSet()
        val guidedUnitIds = pack.units.map { it.unitId }.toSet()
        val guidedLessonIds = pack.lessons.map { it.lessonId }.toSet()
        pack.guidedTasks.forEachIndexed { index, task ->
            if (task.title.isBlank()) add("Guided task at index $index has an empty title")
            if (task.instructions.isEmpty() || task.instructions.any(String::isBlank)) {
                add("Guided task at index $index must contain nonblank instructions")
            }
            if (task.observableCriteria.isEmpty() || task.observableCriteria.any(String::isBlank)) {
                add("Guided task at index $index must contain observable criteria")
            }
            if (task.assessment.isBlank()) add("Guided task at index $index has no assessment guidance")
            if (task.skillId !in knownSkillIds) add("Guided task at index $index references an unknown skillId")
            if (task.bookId !in guidedBookIds) add("Guided task at index $index references an unknown bookId")
            val unit = pack.units.singleOrNull { it.unitId == task.unitId }
            val lesson = pack.lessons.singleOrNull { it.lessonId == task.lessonId }
            if (task.unitId !in guidedUnitIds || unit == null) add("Guided task at index $index references an unknown unitId")
            if (task.lessonId !in guidedLessonIds || lesson == null) add("Guided task at index $index references an unknown lessonId")
            if (unit != null && unit.bookId != task.bookId) add("Guided task at index $index bookId does not match its unit")
            if (lesson != null && lesson.unitId != task.unitId) add("Guided task at index $index unitId does not match its lesson")
            if (lesson != null && task.skillId !in lesson.skillIds) {
                add("Guided task at index $index skillId is not assigned to its lesson")
            }
        }
        return ContentPackValidation(isValid = errors.isEmpty(), errors = errors)
    }

    private fun isSafeImageAssetPath(path: String): Boolean =
        isSafeAssetPath(path) && (path.endsWith(".png", ignoreCase = true) || path.endsWith(".svg", ignoreCase = true))

    private fun containsArabicScript(value: String): Boolean {
        var index = 0
        while (index < value.length) {
            val codePoint = Character.codePointAt(value, index)
            if (
                codePoint in 0x0600..0x06FF ||
                codePoint in 0x0750..0x077F ||
                codePoint in 0x0870..0x089F ||
                codePoint in 0x08A0..0x08FF ||
                codePoint in 0xFB50..0xFDFF ||
                codePoint in 0xFE70..0xFEFC ||
                codePoint in 0x10EC0..0x10EFF ||
                codePoint in 0x1EE00..0x1EEFF
            ) return true
            index += Character.charCount(codePoint)
        }
        return false
    }

    private fun isSafeAssetPath(path: String): Boolean =
        path.isNotBlank() &&
            !path.startsWith("/") &&
            !path.contains('\\') &&
            !path.contains("://") &&
            path.split("/").none { it == ".." }

    private fun isValidHttpsUrl(value: String): Boolean {
        val uri = runCatching { URI(value) }.getOrNull() ?: return false
        return uri.scheme.equals("https", ignoreCase = true) &&
            !uri.host.isNullOrBlank() &&
            uri.rawUserInfo == null
    }

    private fun isValidIsoDate(value: String): Boolean {
        if (value.length != 10 || value[4] != '-' || value[7] != '-') return false
        for (index in value.indices) {
            if (index == 4 || index == 7) continue
            if (value[index] !in '0'..'9') return false
        }

        val year = value.substring(0, 4).toInt()
        val month = value.substring(5, 7).toInt()
        val day = value.substring(8, 10).toInt()
        if (year !in 1..9999 || month !in 1..12) return false

        val daysInMonth = when (month) {
            2 -> if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        return day in 1..daysInMonth
    }
}
