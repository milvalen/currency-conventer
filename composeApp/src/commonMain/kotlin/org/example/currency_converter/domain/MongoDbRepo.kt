package org.example.currency_converter.domain

import kotlinx.coroutines.flow.Flow

import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RequestCondition

interface MongoDbRepo {
    fun retrieveCurrencyAmountData(): Flow<RequestCondition<List<CurrencyObject>>>
    suspend fun addCurrencyAmountData(currency: CurrencyObject)
    suspend fun cleanDB()
    fun setRealm()
}
