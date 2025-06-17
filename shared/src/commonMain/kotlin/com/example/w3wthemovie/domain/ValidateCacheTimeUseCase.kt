package com.example.w3wthemovie.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

/**
 * return true if cache is still valid
 */
class ValidateCacheTimeUseCase {
    operator fun invoke(timestamp: Long): Boolean {
        val currentMoment = Clock.System.now()
        val oneDayAgo = currentMoment.minus(VALID_CACHE_HOURS, DateTimeUnit.HOUR).epochSeconds
        return oneDayAgo <= timestamp
    }

    companion object {
        /**
         * cache's time is 24 hours
         */
        private const val VALID_CACHE_HOURS = 24
    }
}