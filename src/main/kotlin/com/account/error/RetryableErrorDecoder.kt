package com.account.error

import feign.FeignException
import feign.Response
import feign.RetryableException
import feign.codec.ErrorDecoder
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class RetryableErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String?, response: Response): Exception {
        val exception = FeignException.errorStatus(methodKey, response)
        val status: Int = response.status()
        val requestUrl = response.request().url()
        val requestMethod = response.request().method()
        return if (status >= 500) {
            logger.info { "5xx error on $requestMethod $requestUrl found, retrying request" }
            RetryableException(exception.message, exception, null)
        } else exception
    }
}
