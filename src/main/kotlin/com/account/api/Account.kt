package com.account.api

import java.math.BigDecimal
import java.time.LocalDateTime

data class Account(
    val userId: String,
    val balance: BigDecimal,
    val currencyCode: CurrencyCode,
    val isExchangeDateActual: Boolean = false,
    val exchangeDate: LocalDateTime? = null
)
