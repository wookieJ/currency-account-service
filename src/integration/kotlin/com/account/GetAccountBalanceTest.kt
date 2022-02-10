package com.account

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.prop
import com.account.api.Account
import com.account.api.CurrencyCode
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal

class GetAccountBalanceTest : IntegrationTest() {

    @Test
    fun `should return existing user account balance`() {
        val response = restTemplate.getForEntity("$serverUrl/api/v1/accounts/1", Account::class.java)

        assertAll {
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body).all {
                prop("user id") { Account::userId.call(it) }.isEqualTo("1")
                prop("balance") { Account::balance.call(it) }.isEqualTo(BigDecimal.valueOf(0))
                prop("currency code") { Account::currencyCode.call(it) }.isEqualTo(CurrencyCode.PLN)
            }
        }
    }

    @Test
    fun `should return 404 NOT_FOUND if user not exists`() {

        // TODO: check status code

        assertThat { restTemplate.getForEntity("$serverUrl/api/v1/accounts/xxx", Error::class.java) }
            .isFailure()
    }
}
