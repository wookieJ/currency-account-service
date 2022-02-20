package com.account.background

import com.account.repository.EXCHANGE_RATES_CACHE_NAME
import com.account.repository.NBPRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class EvictCacheScheduler(
    private val nbpRepository: NBPRepository
) {
    @Scheduled(cron = EVERY_MIDNIGHT_CRON)
    fun evictExchangeRatesCache() {
        logger.info { "$EXCHANGE_RATES_CACHE_NAME cache cleaning" }
        nbpRepository.evictCache()
    }

    companion object {
        const val EVERY_MIDNIGHT_CRON = "0 0 0 * * *"
    }
}
