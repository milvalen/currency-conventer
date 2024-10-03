package org.example.currency_converter.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import org.example.currency_converter.domain.CurrencyType
import org.example.currency_converter.presentation.component.CurrencyPickerDialog

import org.example.currency_converter.presentation.component.HomePageHeader
import surfaceColor

class HomePage: Screen {
    @Composable
    override fun Content() {
        getScreenModel<HomePageViewModel>().let {
            val selectedCurrencyType by remember { mutableStateOf(CurrencyType.None) }
            val rateCondition by it.rateRefreshStatus
            val currencyFrom by it.sourceCurrency
            val currencyTo by it.targetCurrency

            var amountDigits by rememberSaveable { mutableStateOf(0.0) }
            var isDialogUnfolded by remember { mutableStateOf(true) }

            if (isDialogUnfolded)
                CurrencyPickerDialog(it.allCurrencies, selectedCurrencyType, { isDialogUnfolded = false }) {
                    isDialogUnfolded = true
                }

            Column(Modifier.fillMaxSize().background(surfaceColor)) {
                HomePageHeader(
                    rateCondition,
                    currencyFrom,
                    currencyTo,
                    amountDigits,
                    { number -> amountDigits = number },
                    { it.passEvent(HomePageUiEvent.RefreshRatesEvent) }
                ) { it.passEvent(HomePageUiEvent.CurrenciesSwitch) }
            }
        }
    }
}
