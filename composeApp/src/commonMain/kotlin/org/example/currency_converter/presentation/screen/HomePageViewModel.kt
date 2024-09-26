package org.example.currency_converter.presentation.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

import org.example.currency_converter.domain.CurrencyConverterApiService
import org.example.currency_converter.domain.MongoDbRepo
import org.example.currency_converter.domain.SharedPrefsRepo
import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RateCondition
import org.example.currency_converter.domain.model.RequestCondition

sealed class HomePageUiEvent { data object RefreshRatesEvent: HomePageUiEvent() }

class HomePageViewModel(
    private val prefs: SharedPrefsRepo,
    private val apiService: CurrencyConverterApiService,
    private val mongoDbRepo: MongoDbRepo
): ScreenModel {
    private var _rateRefreshStatus = mutableStateOf(RateCondition.IdleCondition)
    val rateRefreshStatus: State<RateCondition> = _rateRefreshStatus
    private var _currencyFrom = mutableStateOf(RequestCondition.IdleCondition)
    val sourceCurrency: State<RequestCondition<CurrencyObject>> = _currencyFrom
    private var _currencyTo = mutableStateOf(RequestCondition.IdleCondition)
    val targetCurrency: State<RequestCondition<CurrencyObject>> = _currencyTo
    private var _allCurrencies = mutableStateListOf<CurrencyObject>()
    val allCurrencies: List<CurrencyObject> = _allCurrencies

    init { screenModelScope.launch { readNewRates() } }

    private suspend fun readNewRates() {
        try {
            val localStorage = mongoDbRepo.retrieveCurrencyAmountData().first()

            if (localStorage.isSuccess()) {
                localStorage.getSuccessInfo().let {
                    if (it.isNotEmpty()) {
                        println("Test: DB Data is Available")
                        _allCurrencies.addAll(it)

                        if (prefs.isFreshFetched(Clock.System.now().toEpochMilliseconds()))
                            println("Test: Data is New")
                        else {
                            println("Test: Data is not New")
                            fetchDataToDB()
                        }
                    } else {
                        println("Test: DB is Empty")
                        fetchDataToDB()
                    }
                }
            } else if (localStorage.isError())
                println("Test: Local Storage Reading Error ${localStorage.getErrorMessageInfo()}")

            readRateCondition()
        } catch (error: Exception) { println(error.message) }
    }

    private suspend fun fetchDataToDB() {
        val newReadData = apiService.retrieveLatestCurrencyConverterRates()

        if (newReadData.isSuccess()) {
            mongoDbRepo.cleanDB()

            newReadData.getSuccessInfo().let {
                it.forEach { currency ->
                    mongoDbRepo.addCurrencyAmountData(currency)
                    println("Test: ${currency.code} Saved")
                }

                println("Test: _allCurrencies is Updated")
                _allCurrencies.addAll(it)
                return
            }
        }

        if (newReadData.isError())
            println("Test: Reading is Failed ${newReadData.getErrorMessageInfo()}")
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
