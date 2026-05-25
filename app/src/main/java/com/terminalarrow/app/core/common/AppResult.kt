package com.terminalarrow.app.core.common

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val exception: AppException) : AppResult<Nothing>()
    data object Loading : AppResult<Nothing>()
}

sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkException(cause: Throwable) : AppException("No internet connection", cause)
    class ServerException(val code: Int, message: String) : AppException(message)
    class AuthException(message: String) : AppException(message)
    class UnknownException(cause: Throwable) : AppException("Unexpected error", cause)
}
