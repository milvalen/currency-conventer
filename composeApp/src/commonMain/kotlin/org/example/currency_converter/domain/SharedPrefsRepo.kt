package org.example.currency_converter.domain

interface SharedPrefsRepo {
    suspend fun storeLastUpdatedTime(lastUpdated: String)
    suspend fun isFreshFetched(currentTimestamp: Long): Boolean
}
