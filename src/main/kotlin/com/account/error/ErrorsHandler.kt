package com.account.error

import com.account.exception.AccountNotFoundException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class ErrorsHandler {

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleNoSuchElementException(ex: AccountNotFoundException, webRequest: ServletWebRequest): ResponseEntity<Error> {
        val message = ex.message ?: "Account not found"
        val path = webRequest.request.servletPath
        logError(ACCOUNT_NOT_FOUND_CODE, ex, message, path)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Error.Builder()
                .code(ACCOUNT_NOT_FOUND_CODE)
                .message(message)
                .path(path)
                .cause(ex.cause.toString())
                .exception(ex::class.java)
                .build()
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception, webRequest: ServletWebRequest): ResponseEntity<Error> {
        val message = ex.message ?: "Internal error: ${ex.printStackTrace()}"
        val path = webRequest.request.servletPath
        logError(INTERNAL_ERROR_CODE, ex, message, path)
        return ResponseEntity.internalServerError().body(
            Error.Builder()
                .code(INTERNAL_ERROR_CODE)
                .message(message)
                .cause(ex.cause.toString())
                .path(path)
                .exception(ex::class.java)
                .build()
        )
    }

    private fun logError(code: String, ex: Exception, message: String, path: String) {
        logger.error { "[$code - ${ex::class.java}] $message, path: $path" }
    }

    companion object {
        const val INTERNAL_ERROR_CODE = "INTERNAL_ERROR"
        const val ACCOUNT_NOT_FOUND_CODE = "ACCOUNT_NOT_FOUND"
    }
}
