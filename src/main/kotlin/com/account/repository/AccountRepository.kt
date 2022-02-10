package com.account.repository

import com.account.api.Account
import com.account.exception.AccountNotFoundException

interface AccountRepository {
    @Throws(AccountNotFoundException::class)
    fun findByUserId(userId: String): Account
}
