package com.account.config

import com.account.error.RetryableErrorDecoder
import com.account.nbp.NBPClient
import com.account.repository.NBPRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import feign.Feign
import feign.Logger
import feign.Retryer
import feign.jackson.JacksonDecoder
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@Configuration
@EnableConfigurationProperties(ExchangeProperties::class)
class NBPConfiguration(
    private val properties: ExchangeProperties
) {
    @Bean
    fun nbpRepository() = NBPRepository(nbpClient())

    fun nbpClient(): NBPClient {
        val objectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val jacksonModules = listOf(JavaTimeModule(), KotlinModule())
        objectMapper.registerModules(jacksonModules)
        return Feign.builder()
            .decoder(JacksonDecoder(objectMapper))
            .errorDecoder(RetryableErrorDecoder())
            .logger(feignLogger())
            .retryer(Retryer.Default(
                TimeUnit.SECONDS.toMillis(properties.retries.minWaitSeconds),
                TimeUnit.SECONDS.toMillis(properties.retries.maxWaitSeconds),
                properties.retries.retryCount
            ))
            .logLevel(Logger.Level.FULL)
            .target(NBPClient::class.java, properties.clients.nbp.url)
    }

    private fun feignLogger() = object : Logger() {
        private val logger = getLogger(NBPClient::class.java)

        override fun log(configKey: String, format: String, vararg args: Any) {
            logger.debug("[$configKey] ${String.format(format, *args)}")
        }
    }
}
