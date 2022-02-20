package com.account

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.assertions.prop
import com.account.api.Account
import com.account.api.CurrencyCode
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

class GetAccountBalanceTest : IntegrationTest() {

    @Test
    fun `should return account balance with today exchange`() {
        val today = LocalDate.now()
        stubGetUSDExchangeRate(
            effectiveDate = today,
            bid = BigDecimal.valueOf(3.9291),
            ask = BigDecimal.valueOf(4.0085)
        )

        val response = restTemplate.getForEntity("$serverUrl/api/v1/accounts/1", Account::class.java)

        assertAll {
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body).all {
                prop("user id") { Account::userId.call(it) }.isEqualTo("1")
                prop("balance value in USD") { Account::balance.call(it) }.isEqualTo(BigDecimal.valueOf(191.05))
                prop("currency code") { Account::currencyCode.call(it) }.isEqualTo(CurrencyCode.USD)
                prop("balance was exchange today") { Account::isTodayExchange.call(it) }.isTrue()
                prop("exchange day is today") { Account::exchangeDate.call(it) }.isEqualTo(today)
            }
        }
    }

    @Test
    fun `should return account balance after retry`() {
        val today = LocalDate.now()
        stubGetUSDExchangeRateFlappingResponse(
            effectiveDate = today,
            bid = BigDecimal.valueOf(3.9291),
            ask = BigDecimal.valueOf(4.0085)
        )

        val response = restTemplate.getForEntity("$serverUrl/api/v1/accounts/1", Account::class.java)

        assertAll {
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body).all {
                prop("user id") { Account::userId.call(it) }.isEqualTo("1")
                prop("balance value in USD") { Account::balance.call(it) }.isEqualTo(BigDecimal.valueOf(191.05))
                prop("currency code") { Account::currencyCode.call(it) }.isEqualTo(CurrencyCode.USD)
                prop("balance was exchange today") { Account::isTodayExchange.call(it) }.isTrue()
                prop("exchange day is today") { Account::exchangeDate.call(it) }.isEqualTo(today)
            }
        }
    }

    @Test
    fun `should return account balance with yestarday exchange`() {
        val yesterday = LocalDate.now().minusDays(1)
        stubGetUSDExchangeRate(
            effectiveDate = yesterday,
            bid = BigDecimal.valueOf(3.9291),
            ask = BigDecimal.valueOf(4.0085)
        )

        val response = restTemplate.getForEntity("$serverUrl/api/v1/accounts/1", Account::class.java)

        assertAll {
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body).all {
                prop("user id") { Account::userId.call(it) }.isEqualTo("1")
                prop("balance value in USD") { Account::balance.call(it) }.isEqualTo(BigDecimal.valueOf(191.05))
                prop("currency code") { Account::currencyCode.call(it) }.isEqualTo(CurrencyCode.USD)
                prop("balance was not exchange today") { Account::isTodayExchange.call(it) }.isFalse()
                prop("exchange day was yesterday") { Account::exchangeDate.call(it) }.isEqualTo(yesterday)
            }
        }
    }

    @Test
    fun `should return account balance with today exchange from cache`() {
        // TODO: Not implemented yet
    }

    @Test
    fun `should return 404 NOT_FOUND if user not exists`() {
        assertThat { restTemplate.getForEntity("$serverUrl/api/v1/accounts/xxx", Error::class.java) }
            .isFailure()
    }
}
