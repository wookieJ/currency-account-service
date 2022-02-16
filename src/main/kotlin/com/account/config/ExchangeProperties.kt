package com.account.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "exchange")
@ConstructorBinding
data class ExchangeProperties(
    val clients: Clients
) {
    @ConstructorBinding
    data class Clients(
        val nbp: NBP
    ) {
        @ConstructorBinding
        data class NBP(
            val url: String
        )
    }
}
