package org.example.currency_converter.domain.model

sealed class RequestCondition<out T> {
    data object IdleCondition: RequestCondition<Nothing>()
    data object LoadingCondition: RequestCondition<Nothing>()

    data class SuccessCondition<out T>(val data: T): RequestCondition<T>()
    data class ErrorCondition(val message: String): RequestCondition<Nothing>()

    fun isLoading() = this is LoadingCondition
    fun isError() = this is ErrorCondition
    fun isSuccess() = this is SuccessCondition
    fun getSuccessInfo() = (this as SuccessCondition).data
    fun getErrorMessageInfo() = (this as ErrorCondition).message
}
