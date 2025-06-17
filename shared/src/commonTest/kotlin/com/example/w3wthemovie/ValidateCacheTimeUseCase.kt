package com.example.w3wthemovie

import com.example.w3wthemovie.domain.ValidateCacheTimeUseCase
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateCacheTimeUseCaseTest {

    private val useCase = ValidateCacheTimeUseCase()

    @Test
    fun `Cache is valid when timestamp is recent`() {
        val twentyThreeHoursInMillis = Clock.System.now().minus(23, DateTimeUnit.HOUR).epochSeconds
        val isValid = useCase(twentyThreeHoursInMillis)
        assertTrue(isValid)
    }

    @Test
    fun `Cache is invalid when timestamp is exactly 24 hours old`() {
        val twentyThreeHoursInMillis = Clock.System.now().minus(24, DateTimeUnit.HOUR).epochSeconds
        val isValid = useCase(twentyThreeHoursInMillis)
        assertTrue(isValid)
    }

    @Test
    fun `Cache is invalid when timestamp is older than 24 hours`() {
        val twentyThreeHoursInMillis = Clock.System.now().minus(25, DateTimeUnit.HOUR).epochSeconds
        val isValid = useCase(twentyThreeHoursInMillis)
        assertFalse(isValid)
    }

    @Test
    fun `Cache is valid when timestamp is current time`() {
        val twentyThreeHoursInMillis = Clock.System.now().epochSeconds
        val isValid = useCase(twentyThreeHoursInMillis)
        assertTrue(isValid)
    }

    @Test
    fun `Cache is invalid for future timestamp`() {
        val twentyThreeHoursInMillis = Clock.System.now().plus(1, DateTimeUnit.HOUR).epochSeconds
        val isValid = useCase(twentyThreeHoursInMillis)
        assertTrue(isValid)
    }

    @Test
    fun `Cache check with timestamp slightly less than 24 hours ago`() {
        // Given a timestamp that is 23 hours and 59 minutes and 59 seconds old
        val twentyThreeHoursInMillis = Clock.System.now().minus(23 * 60 * 60 + 59 * 60 + 59, DateTimeUnit.SECOND).epochSeconds
        val isValid = useCase(twentyThreeHoursInMillis)
        assertTrue(isValid)
    }

    @Test
    fun `Cache check with timestamp slightly more than 24 hours ago`() {
        // Given a timestamp that is 24 hours and 1 second old
        val twentyThreeHoursInMillis = Clock.System.now().minus(24 * 60 * 60 + 1, DateTimeUnit.SECOND).epochSeconds
        val isValid = useCase(twentyThreeHoursInMillis)
        assertFalse(isValid)
    }
}
