package com.asimalimikial.aas2.content

import java.util.Locale

/** Shared learner-path and language selection rules for bundled learning content. */
object ContentPackSelectionPolicy {
    fun canonicalLearnerPath(pathway: String): String? =
        when (pathway.trim().uppercase(Locale.ROOT)) {
            "KG2" -> "KG2"
            "GRADE 2", "GRADE_2", "GRADE2" -> "GRADE_2"
            else -> null
        }

    fun packIdForPathway(pathway: String): String? =
        when (canonicalLearnerPath(pathway)) {
            "KG2" -> "aas2-kg2-foundation"
            "GRADE_2" -> "aas2-grade2-foundation"
            else -> null
        }

    fun activitiesForLanguage(
        pack: ContentPack,
        preferredLanguage: String
    ): List<ActivityDefinition> {
        val language = if (preferredLanguage.trim().lowercase(Locale.ROOT).startsWith("ar")) "ar" else "en"
        val preferred = pack.activities.filter { it.language == language }
        return if (preferred.isNotEmpty()) preferred else pack.activities.filter { it.language == "en" }
    }
}
