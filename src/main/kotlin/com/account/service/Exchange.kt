package com.account.service

import java.math.BigDecimal
import java.time.LocalDate

data class Exchange(
    val value: BigDecimal,
    val isTodayExchange: Boolean,
    val effectiveDate: LocalDate
)
