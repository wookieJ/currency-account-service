package com.account.error

import java.time.LocalDateTime

class Error private constructor(
    val code: String?,
    val message: String?,
    val timestamp: LocalDateTime,
    val path: String?,
    val cause: String?,
    val exception: Class<out Exception>?
) {
    data class Builder(
        var code: String? = null,
        var message: String? = null,
        var path: String? = null,
        var cause: String? = null,
        var exception: Class<out Exception>? = null
    ) {
        fun code(code: String) = apply { this.code = code }
        fun message(message: String) = apply { this.message = message }
        fun path(path: String) = apply { this.path = path }
        fun cause(cause: String) = apply { this.cause = cause }
        fun exception(exception: Class<out Exception>) = apply { this.exception = exception }
        fun build() = Error(code, message, LocalDateTime.now(), path, cause, exception)
    }
}
