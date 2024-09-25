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

class HomePage: Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomePageViewModel>()
        val rateCondition by viewModel.rateRefreshStatus
        val currencyFrom by viewModel.sourceCurrency
        val currencyTo by viewModel.targetCurrency
        var amountDigits by rememberSaveable { mutableStateOf(0.0) }

        Column(Modifier.fillMaxSize().background(surfaceColor)) {
            HomePageHeader(
                rateCondition,
                currencyFrom,
                currencyTo,
                amountDigits,
                { amountDigits = it },
                { viewModel.passEvent(HomePageUiEvent.RefreshRatesEvent) }
            ) {}
        }
    }
}
