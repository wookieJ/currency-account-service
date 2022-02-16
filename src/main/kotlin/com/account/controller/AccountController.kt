package com.account.controller

import com.account.api.Account
import com.account.api.CurrencyCode
import com.account.service.AccountServiceImpl
import com.account.service.NBPExchangeService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/api/v1/accounts"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class AccountController(
    private val accountService: AccountServiceImpl,
    private val nbpExchangeService: NBPExchangeService
) {

    @GetMapping("/{userId}")
    fun getAccount(@PathVariable userId: String): ResponseEntity<Account> {
        val plnAccount = accountService.getAccount(userId)
        val usdAccount = convertToUsd(plnAccount)
        return ResponseEntity
            .ok()
            .body(usdAccount)
    }

    private fun convertToUsd(plnAccount: Account): Account {
        val usdExchange = nbpExchangeService.exchangePlnToUsd(plnAccount.balance)
        return Account(
            userId = plnAccount.userId,
            balance = usdExchange.value,
            currencyCode = CurrencyCode.USD,
            isTodayExchange = usdExchange.isTodayExchange,
            exchangeDate = usdExchange.effectiveDate
        )
    }
}
