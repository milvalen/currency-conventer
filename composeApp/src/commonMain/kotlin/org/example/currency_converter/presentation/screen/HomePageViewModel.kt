package org.example.currency_converter.presentation.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

import org.example.currency_converter.domain.CurrencyConverterApiService
import org.example.currency_converter.domain.SharedPrefsRepo
import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RateCondition
import org.example.currency_converter.domain.model.RequestCondition

sealed class HomePageUiEvent { data object RefreshRatesEvent: HomePageUiEvent() }

class HomePageViewModel(
    private val prefs: SharedPrefsRepo,
    private val apiService: CurrencyConverterApiService
): ScreenModel {
    private var _rateRefreshStatus = mutableStateOf(RateCondition.IdleCondition)
    val rateRefreshStatus: State<RateCondition> = _rateRefreshStatus
    private var _currencyFrom = mutableStateOf(RequestCondition.IdleCondition)
    val sourceCurrency: State<RequestCondition<CurrencyObject>> = _currencyFrom
    private var _currencyTo = mutableStateOf(RequestCondition.IdleCondition)
    val targetCurrency: State<RequestCondition<CurrencyObject>> = _currencyTo

    init { screenModelScope.launch { readNewRates() } }

    private suspend fun readNewRates() {
        try {
            apiService.retrieveLatestCurrencyConverterRates()
            readRateCondition()
        } catch (error: Exception) { println(error.message) }
    }

    private suspend fun readRateCondition() {
        _rateRefreshStatus.value =
            if (prefs.isFreshFetched(Clock.System.now().toEpochMilliseconds()))
                RateCondition.FreshCondition
            else RateCondition.StaleCondition
    }

    fun passEvent(event: HomePageUiEvent) {
        if (event is HomePageUiEvent.RefreshRatesEvent) screenModelScope.launch { readNewRates() }
    }
}
