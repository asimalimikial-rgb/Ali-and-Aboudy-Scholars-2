package com.asimalimikial.aas2.learning

/**
 * The child gets one retry after the first incorrect response.
 * A second incorrect response is final for this question session.
 */
object QuestionAttemptPolicy {
    const val MAX_ATTEMPTS = 2

    fun canRetry(completedAttempts: Int): Boolean =
        completedAttempts in 1 until MAX_ATTEMPTS

    fun needsReview(completedAttempts: Int, isCorrect: Boolean): Boolean =
        completedAttempts >= MAX_ATTEMPTS && !isCorrect
}
