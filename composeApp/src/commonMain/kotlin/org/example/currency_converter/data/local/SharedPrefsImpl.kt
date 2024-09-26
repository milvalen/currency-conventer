package org.example.currency_converter.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

import org.example.currency_converter.domain.SharedPrefsRepo
import org.example.currency_converter.domain.model.CurrencyKey

@OptIn(ExperimentalSettingsApi::class)
class SharedPrefsImpl(private val settings: Settings): SharedPrefsRepo {
    companion object {
        const val SOURCE_CURRENCY_KEY = "sourceCurrency"
        const val TARGET_CURRENCY_KEY = "targetCurrency"
        const val TIMESTAMP_KEY = "lastUpdatedTime"

        val defaultSourceCurrencyKey = CurrencyKey.RUB.name
        val defaultTargetCurrencyKey = CurrencyKey.USD.name
    }

    private val proceededSettings = (settings as ObservableSettings).toFlowSettings()

    override suspend fun storeLastUpdatedTime(lastUpdated: String) {
        proceededSettings.putLong(TIMESTAMP_KEY, Instant.parse(lastUpdated).toEpochMilliseconds())
    }

    override fun readSourceCurrencyKey(): Flow<CurrencyKey> {
        return proceededSettings.getStringFlow(SOURCE_CURRENCY_KEY, defaultSourceCurrencyKey).map {
            key -> CurrencyKey.valueOf(key)
        }
    }

    override fun readTargetCurrencyKey(): Flow<CurrencyKey> {
        return proceededSettings.getStringFlow(TARGET_CURRENCY_KEY, defaultTargetCurrencyKey).map {
            key -> CurrencyKey.valueOf(key)
        }
    }

    override suspend fun storeSourceCurrencyKey(code: String) {
        proceededSettings.putString(SOURCE_CURRENCY_KEY, code)
    }

    override suspend fun storeTargetCurrencyKey(code: String) {
        proceededSettings.putString(TARGET_CURRENCY_KEY, code)
    }

    override suspend fun isFreshFetched(currentTimestamp: Long): Boolean {
        proceededSettings.getLong(TIMESTAMP_KEY, 0L).let {
            return it != 0L && Instant
                .fromEpochMilliseconds(currentTimestamp)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfYear -
                    Instant
                        .fromEpochMilliseconds(it)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfYear < 1
        }
    }
}
