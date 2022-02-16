package com.account.nbp

import feign.Headers
import feign.RequestLine

@Headers("Accept: application/json", "Content-Type: application/json")
interface NBPClient {

    @RequestLine("GET /api/exchangerates/rates/C/USD/today")
    fun getTodayUSDExchangeRate(): ExchangeRateResponse
}
