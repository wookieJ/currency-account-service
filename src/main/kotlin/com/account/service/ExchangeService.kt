package com.account.service

import java.math.BigDecimal

interface ExchangeService {
    fun exchangePlnToUsd(value: BigDecimal): Exchange
}
