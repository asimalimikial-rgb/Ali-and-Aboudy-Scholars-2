package com.asimalimikial.aas2.learning

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivityRetryPolicyTest {
    @Test
    fun firstIncorrectAnswerOffersOneRetry() {
        assertTrue(ActivityRetryPolicy.canRetry(1))
    }

    @Test
    fun noIncorrectAnswersDoesNotConsumeRetry() {
        assertTrue(ActivityRetryPolicy.canRetry(0))
    }

    @Test
    fun secondIncorrectAnswerEndsRetryOpportunity() {
        assertFalse(ActivityRetryPolicy.canRetry(2))
    }

    @Test
    fun laterIncorrectAttemptsCannotReopenRetry() {
        assertFalse(ActivityRetryPolicy.canRetry(3))
    }
}
