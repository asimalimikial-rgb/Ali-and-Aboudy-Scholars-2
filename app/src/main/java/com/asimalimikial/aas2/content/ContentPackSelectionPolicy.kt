package com.asimalimikial.aas2.content

import java.util.Locale

/** Shared learner-path and learning-language selection rules for bundled content. */
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

    /**
     * English is the learning language for the current child pathways.
     * Device locale controls app chrome only and must never select translated
     * learning content. Arabic child-activity support remains deferred.
     */
    fun englishActivities(pack: ContentPack): List<ActivityDefinition> =
        pack.activities.filter { it.language == "en" }
}
