package com.asimalimikial.aas2.data

/** Keeps the persisted learner selection aligned with the active local profiles. */
object LearnerSelectionPolicy {
    fun activeProfiles(profiles: List<LearnerProfileEntity>): List<LearnerProfileEntity> =
        profiles.filter { it.isActive }

    fun selectedProfile(
        selectedLearnerId: String?,
        profiles: List<LearnerProfileEntity>
    ): LearnerProfileEntity? {
        if (selectedLearnerId.isNullOrBlank()) return null
        return profiles.firstOrNull { it.learnerId == selectedLearnerId && it.isActive }
    }

    /**
     * Do not clear a selection while Room is still loading. Once profiles are
     * available, clear stale, blank, or inactive selections.
     */
    fun shouldClearSelection(
        selectedLearnerId: String?,
        profiles: List<LearnerProfileEntity>
    ): Boolean =
        selectedLearnerId != null &&
            profiles.isNotEmpty() &&
            selectedProfile(selectedLearnerId, profiles) == null
}
