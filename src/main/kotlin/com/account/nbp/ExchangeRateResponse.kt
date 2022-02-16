package com.account.nbp

import java.math.BigDecimal
import java.time.LocalDate

data class ExchangeRateResponse(
    val code: String,
    val rates: List<Rate>
)

data class Rate(
    val no: String,
    val effectiveDate: LocalDate,
    val bid: BigDecimal,
    val ask: BigDecimal
)
