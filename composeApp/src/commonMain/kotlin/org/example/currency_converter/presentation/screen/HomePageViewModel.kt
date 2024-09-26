package org.example.currency_converter.presentation.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

import org.example.currency_converter.domain.CurrencyConverterApiService
import org.example.currency_converter.domain.MongoDbRepo
import org.example.currency_converter.domain.SharedPrefsRepo
import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RateCondition
import org.example.currency_converter.domain.model.RequestCondition

sealed class HomePageUiEvent {
    data object RefreshRatesEvent: HomePageUiEvent()
    data object CurrenciesSwitch: HomePageUiEvent()
}

class HomePageViewModel(
    private val prefs: SharedPrefsRepo,
    private val apiService: CurrencyConverterApiService,
    private val mongoDbRepo: MongoDbRepo
): ScreenModel {
    private var _rateRefreshStatus: MutableState<RateCondition> =
        mutableStateOf(RateCondition.IdleCondition)

    val rateRefreshStatus: State<RateCondition> = _rateRefreshStatus

    private var _sourceCurrency: MutableState<RequestCondition<CurrencyObject>> =
        mutableStateOf(RequestCondition.IdleCondition)

    val sourceCurrency: State<RequestCondition<CurrencyObject>> = _sourceCurrency

    private var _targetCurrency: MutableState<RequestCondition<CurrencyObject>> =
        mutableStateOf(RequestCondition.IdleCondition)

    val targetCurrency: State<RequestCondition<CurrencyObject>> = _targetCurrency
    private var _allCurrencies = mutableStateListOf<CurrencyObject>()
    val allCurrencies: List<CurrencyObject> = _allCurrencies

    init {
        screenModelScope.launch {
            readNewRates()
            readSourceCurrency()
            readTargetCurrency()
        }
    }

    private suspend fun readNewRates() {
        try {
            val localStorage = mongoDbRepo.readCurrencyAmountData().first()

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
        when(event) {
            is HomePageUiEvent.RefreshRatesEvent -> screenModelScope.launch { readNewRates() }
            is HomePageUiEvent.CurrenciesSwitch -> switchCurrencies()
        }
    }

    private fun readSourceCurrency() {
        screenModelScope.launch(Dispatchers.Main) {
            prefs.readSourceCurrencyKey().collectLatest {
                currencyKey ->
                _allCurrencies.find { currency -> currency.code == currencyKey.name }.let {
                    _sourceCurrency.value =
                        if (it != null) RequestCondition.SuccessCondition(it)
                        else RequestCondition.ErrorCondition("Selected Currency not Found")
                }
            }
        }
    }

    private fun readTargetCurrency() {
        screenModelScope.launch(Dispatchers.Main) {
            prefs.readTargetCurrencyKey().collectLatest {
                currencyKey ->
                _allCurrencies.find { currency -> currency.code == currencyKey.name }.let {
                    _targetCurrency.value =
                        if (it != null) RequestCondition.SuccessCondition(it)
                        else RequestCondition.ErrorCondition("Selected Currency not Found")
                }
            }
        }
    }

    private fun switchCurrencies() {
        listOf(_sourceCurrency.value, _targetCurrency.value).let {
            _sourceCurrency.value = it[1]
            _targetCurrency.value = it[0]
        }
    }
}
