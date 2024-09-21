package org.example.currency_converter.domain

import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RequestCondition

interface CurrencyConverterApiService {
    suspend fun retreiveLatestCurrencyConverterRates(): RequestCondition<List<CurrencyObject>>
}
