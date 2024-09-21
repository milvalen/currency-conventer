package org.example.currency_converter.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.currency_converter.domain.SharedPrefsRepo

@OptIn(ExperimentalSettingsApi::class)
class SharedPrefsImpl(private val settings: Settings): SharedPrefsRepo {
    companion object { const val TIMESTAMP_KEY = "lastUpdatedTime" }
    private val proceededSettings = (settings as ObservableSettings).toFlowSettings()

    override suspend fun storeLastUpdatedTime(lastUpdated: String) {
        proceededSettings.putLong(TIMESTAMP_KEY, 0L)
    }

    override suspend fun isFreshFetched(currentTimestamp: Long): Boolean {
        val storedTimestamp = proceededSettings.getLong(TIMESTAMP_KEY, 0L)

        return if (storedTimestamp != 0L) TimeZone.currentSystemDefault().let {
            Instant.fromEpochMilliseconds(currentTimestamp).toLocalDateTime(it).date.dayOfYear
            - Instant.fromEpochMilliseconds(storedTimestamp).toLocalDateTime(it).date.dayOfYear < 1
        } else false
    }
}
