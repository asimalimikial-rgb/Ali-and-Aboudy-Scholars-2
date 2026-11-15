package com.asimalimikial.aas2.ui

/**
 * Identity of one learner's view of one activity in one immutable content-pack version.
 *
 * This key is used by Compose to prevent transient UI state from leaking when the
 * selected learner or active activity changes.
 */
internal data class ActivitySessionKey(
    val learnerId: String,
    val contentPackId: String,
    val contentPackVersion: String,
    val activityId: String
)
