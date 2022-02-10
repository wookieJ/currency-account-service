package com.account.service

import com.account.api.Account
import com.account.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class InMemoryAccountService(
    val accountsCollection: AccountRepository
) : AccountService {

    override fun getAccount(userId: String): Account {
        return accountsCollection.findByUserId(userId)
    }
}
