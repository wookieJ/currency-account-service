package com.account.repository

import com.account.api.Account
import com.account.api.CurrencyCode
import com.account.exception.AccountNotFoundException
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
@Primary
class InMemoryAccountRepository : AccountRepository {

    private final val accountsCollection: Map<String, Account> = mapOf(
        "1" to Account("1", BigDecimal.valueOf(0), CurrencyCode.PLN),
        "2" to Account("2", BigDecimal.valueOf(-1.99), CurrencyCode.PLN),
        "3" to Account("3", BigDecimal.valueOf(9), CurrencyCode.PLN),
        "4" to Account("4", BigDecimal.valueOf(999), CurrencyCode.PLN),
        "5" to Account("5", BigDecimal.valueOf(12_000_000), CurrencyCode.PLN),
        "6" to Account("6", BigDecimal.valueOf(0.21), CurrencyCode.PLN),
        "7" to Account("7", BigDecimal.valueOf(1_000_000_000_000), CurrencyCode.PLN),
        "8" to Account("8", BigDecimal.valueOf(99.99), CurrencyCode.PLN),
        "9" to Account("9", BigDecimal.valueOf(132_678), CurrencyCode.PLN),
        "10" to Account("10", BigDecimal.valueOf(9.99), CurrencyCode.PLN)
    )

    override fun findByUserId(userId: String): Account {
        return accountsCollection.getOrElse(userId) { throw AccountNotFoundException("Account of user with id '$userId' not found") }
    }
}
