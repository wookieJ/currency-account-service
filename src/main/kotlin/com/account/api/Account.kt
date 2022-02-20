package com.account.api

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

data class Account(
    @field:Schema(description = "User id number")
    val userId: String,

    @field:Schema(description = "Account balance value")
    val balance: BigDecimal,

    @field:Schema(description = "Currency code by ISO 4217")
    val currencyCode: CurrencyCode,

    @field:Schema(description = "Inform if exchange rate using to convert PLN to USD values are up to date - from today")
    val isTodayExchange: Boolean = false,

    @field:Schema(description = "Exchange rate download date")
    val exchangeDate: LocalDate? = null
)
