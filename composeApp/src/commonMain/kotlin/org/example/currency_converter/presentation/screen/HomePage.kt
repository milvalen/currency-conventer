package org.example.currency_converter.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

import org.example.currency_converter.presentation.component.HomePageHeader
import surfaceColor

// TODO: switch animation

class HomePage: Screen {
    @Composable
    override fun Content() {
        getScreenModel<HomePageViewModel>().let {
            val rateCondition by it.rateRefreshStatus
            val currencyFrom by it.sourceCurrency
            val currencyTo by it.targetCurrency

            var amountDigits by rememberSaveable { mutableStateOf(0.0) }

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
