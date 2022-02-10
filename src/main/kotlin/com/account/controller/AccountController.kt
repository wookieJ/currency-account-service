package com.account.controller

import com.account.api.Account
import com.account.service.InMemoryAccountService
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
    private val accountService: InMemoryAccountService
) {

    @GetMapping("/{userId}")
    fun getAccount(@PathVariable userId: String): ResponseEntity<Account> {
        val account = accountService.getAccount(userId)
        return ResponseEntity
            .ok()
            .body(account)
    }
}
