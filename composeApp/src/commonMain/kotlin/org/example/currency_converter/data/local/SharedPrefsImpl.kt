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
        proceededSettings.putLong(TIMESTAMP_KEY, Instant.parse(lastUpdated).toEpochMilliseconds())
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
