package com.account.service

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import com.account.api.Account
import com.account.api.CurrencyCode
import com.account.exception.AccountNotFoundException
import com.account.repository.AccountRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal

class InMemoryAccountServiceTest {

    private val accountRepository = Mockito.mock(AccountRepository::class.java)
    private val inMemoryAccountService = InMemoryAccountService(accountRepository)

    @Test
    fun `should return user account if exists`() {
        Mockito.`when`(accountRepository.findByUserId("1"))
            .thenReturn(Account("1", BigDecimal.valueOf(9.99), CurrencyCode.PLN))

        val account = inMemoryAccountService.getAccount("1")

        assertAll {
            assertThat(account.balance).isEqualTo(BigDecimal.valueOf(9.99))
            assertThat(account.currencyCode).isEqualTo(CurrencyCode.PLN)
        }
    }

    @Test
    fun `should throw exception when account not exist`() {
        Mockito.`when`(accountRepository.findByUserId("xxx"))
            .thenThrow(AccountNotFoundException("Account not found"))

        assertThat {
            inMemoryAccountService.getAccount("xxx")
        }.isFailure().hasMessage("Account not found")
    }
}
