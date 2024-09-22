package org.example.currency_converter.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(val meta: ApiMetaData, val data: Map<String, CurrencyObject>)

@Serializable
data class ApiMetaData(val last_updated_at: String)

@Serializable
data class CurrencyObject(val code: String, val value: Double)
