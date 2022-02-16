package com.account.service

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualByComparingTo
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.account.nbp.ExchangeRateResponse
import com.account.nbp.NBPClient
import com.account.nbp.Rate
import com.account.repository.NBPRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal
import java.time.LocalDate

class NBPExchangeServiceTest {

    private val nbpClient = Mockito.mock(NBPClient::class.java)
    private val nbpRepository = NBPRepository(nbpClient)
    private val nbpExchangeService = NBPExchangeService(nbpRepository)

    @Test
    fun `should exchange PLN to USD`() {
        val today = LocalDate.now()
        Mockito.`when`(nbpClient.getTodayUSDExchangeRate())
            .thenReturn(
                ExchangeRateResponse(
                    code = "USD",
                    rates = listOf(
                        Rate(
                            no = "01/TEST",
                            effectiveDate = today,
                            bid = BigDecimal.valueOf(3),
                            ask = BigDecimal.valueOf(4)
                        )
                    )
                )
            )

        val usdExchangePositive = nbpExchangeService.exchangePlnToUsd(BigDecimal.valueOf(120))
        val usdExchangeFraction = nbpExchangeService.exchangePlnToUsd(BigDecimal.valueOf(9.67))
        val usdExchangeNegative = nbpExchangeService.exchangePlnToUsd(BigDecimal.valueOf(-40))
        val usdExchangeZero = nbpExchangeService.exchangePlnToUsd(BigDecimal.valueOf(0))

        assertThat(usdExchangePositive).all {
            prop("value in USD") { Exchange::value.call(it) }.isEqualByComparingTo(BigDecimal.valueOf(30.00))
            prop("is exchange from today") { Exchange::isTodayExchange.call(it) }.isEqualTo(true)
            prop("exchange date") { Exchange::effectiveDate.call(it) }.isEqualTo(today)
        }

        assertThat(usdExchangeFraction).all {
            prop("value in USD") { Exchange::value.call(it) }.isEqualByComparingTo(BigDecimal.valueOf(2.42))
            prop("is exchange from today") { Exchange::isTodayExchange.call(it) }.isEqualTo(true)
            prop("exchange date") { Exchange::effectiveDate.call(it) }.isEqualTo(today)
        }

        assertThat(usdExchangeNegative).all {
            prop("value in USD") { Exchange::value.call(it) }.isEqualByComparingTo(BigDecimal.valueOf(-10.00))
            prop("is exchange from today") { Exchange::isTodayExchange.call(it) }.isEqualTo(true)
            prop("exchange date") { Exchange::effectiveDate.call(it) }.isEqualTo(today)
        }

        assertThat(usdExchangeZero).all {
            prop("value in USD") { Exchange::value.call(it) }.isEqualByComparingTo(BigDecimal.valueOf(0.00))
            prop("is exchange from today") { Exchange::isTodayExchange.call(it) }.isEqualTo(true)
            prop("exchange date") { Exchange::effectiveDate.call(it) }.isEqualTo(today)
        }
    }
}
