package com.account.api

import java.math.BigDecimal
import java.time.LocalDate

data class Account(
    val userId: String,
    val balance: BigDecimal,
    val currencyCode: CurrencyCode,
    val isTodayExchange: Boolean = false,
    val exchangeDate: LocalDate? = null
)
