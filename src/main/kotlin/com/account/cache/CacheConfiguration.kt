package com.account.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfiguration {

    @Bean
    fun cacheManager(): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(wholeDayCacheConfig())
        return caffeineCacheManager
    }

    fun wholeDayCacheConfig(): Caffeine<Any, Any> = Caffeine.newBuilder()
        .expireAfterWrite(25, TimeUnit.HOURS)
        .initialCapacity(1)
}
