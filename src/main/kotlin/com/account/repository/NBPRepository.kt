package com.account.repository

import com.account.nbp.NBPClient
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable

const val EXCHANGE_RATES_CACHE_NAME = "exchange_rates"

@CacheConfig(cacheNames = [EXCHANGE_RATES_CACHE_NAME])
open class NBPRepository(private val nbpClient: NBPClient) {
    @Cacheable
    open fun getTodayUSDExchangeRate() = nbpClient.getTodayUSDExchangeRate()

    @CacheEvict(value = [EXCHANGE_RATES_CACHE_NAME], allEntries = true)
    open fun evictCache() {
    }
}
