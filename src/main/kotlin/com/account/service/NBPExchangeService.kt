package com.account.service

import com.account.nbp.ExchangeRateResponse
import com.account.nbp.Rate
import com.account.repository.NBPRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class NBPExchangeService(
    private val nbpRepository: NBPRepository
) : ExchangeService {
    override fun exchangePlnToUsd(value: BigDecimal): Exchange {
        val todayUSDExchangeRate = cacheableExchangeRate()
        val exchangeRate = todayUSDExchangeRate.rates.first()
        return Exchange(
            value = value.divide(exchangeRate.ask, 2, RoundingMode.HALF_UP),
            isTodayExchange = isTodayDate(exchangeRate),
            effectiveDate = exchangeRate.effectiveDate
        )
    }

    fun cacheableExchangeRate(): ExchangeRateResponse {
        return nbpRepository.getTodayUSDExchangeRate()
    }

    private fun isTodayDate(exchangeRate: Rate) = exchangeRate.effectiveDate.isEqual(LocalDate.now())
}
