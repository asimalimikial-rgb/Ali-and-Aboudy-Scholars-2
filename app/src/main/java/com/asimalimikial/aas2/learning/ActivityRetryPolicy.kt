package com.asimalimikial.aas2.learning

/**
 * A learner gets one retry after the first incorrect answer.
 * A second incorrect answer ends this activity and offers the next one.
 */
object ActivityRetryPolicy {
    const val MAX_INCORRECT_ATTEMPTS = 2

    fun canRetry(incorrectAttempts: Int): Boolean =
        incorrectAttempts in 0 until MAX_INCORRECT_ATTEMPTS
}
