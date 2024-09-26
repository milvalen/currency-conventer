package org.example.currency_converter.domain

import kotlinx.coroutines.flow.Flow
import org.example.currency_converter.domain.model.CurrencyKey

interface SharedPrefsRepo {
    suspend fun isFreshFetched(currentTimestamp: Long): Boolean
    suspend fun storeLastUpdatedTime(lastUpdated: String)
    suspend fun storeSourceCurrencyKey(code: String)
    suspend fun storeTargetCurrencyKey(code: String)
    fun readSourceCurrencyKey(): Flow<CurrencyKey>
    fun readTargetCurrencyKey(): Flow<CurrencyKey>
}
