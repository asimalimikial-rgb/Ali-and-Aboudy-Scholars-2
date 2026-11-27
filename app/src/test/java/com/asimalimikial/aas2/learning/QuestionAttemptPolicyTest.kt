package com.asimalimikial.aas2.learning

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionAttemptPolicyTest {
    @Test
    fun firstIncorrectAnswerAllowsExactlyOneRetry() {
        assertTrue(QuestionAttemptPolicy.canRetry(1))
        assertFalse(QuestionAttemptPolicy.canRetry(0))
    }

    @Test
    fun secondIncorrectAnswerDoesNotAllowAnotherRetry() {
        assertFalse(QuestionAttemptPolicy.canRetry(2))
        assertFalse(QuestionAttemptPolicy.canRetry(3))
    }

    @Test
    fun onlyFinalIncorrectAnswerNeedsReview() {
        assertFalse(QuestionAttemptPolicy.needsReview(1, false))
        assertTrue(QuestionAttemptPolicy.needsReview(2, false))
        assertFalse(QuestionAttemptPolicy.needsReview(2, true))
    }
}
