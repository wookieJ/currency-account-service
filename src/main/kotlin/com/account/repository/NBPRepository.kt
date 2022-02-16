package com.account.repository

import com.account.nbp.NBPClient

class NBPRepository(private val nbpClient: NBPClient) {
    fun getTodayUSDExchangeRate() = nbpClient.getTodayUSDExchangeRate()
}
