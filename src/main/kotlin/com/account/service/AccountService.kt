package com.account.service

import com.account.api.Account

interface AccountService {
    fun getAccount(userId: String): Account
}
