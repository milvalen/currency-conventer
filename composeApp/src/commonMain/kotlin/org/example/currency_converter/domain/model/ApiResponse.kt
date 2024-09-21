package org.example.currency_converter.domain.model

data class ApiResponse(val meta: ApiMetaData, val data: Map<String, CurrencyObject>)
data class ApiMetaData(val last_updated_at: String)
data class CurrencyObject(val code: String, val value: Double)
