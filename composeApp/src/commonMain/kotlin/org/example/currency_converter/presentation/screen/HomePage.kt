package org.example.currency_converter.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen

import org.example.currency_converter.data.remote.thirdPartyAPI.CurrencyConverterApiServiceImpl

class HomePage: Screen {
    @Composable
    override fun Content() {
        LaunchedEffect(Unit) {
            CurrencyConverterApiServiceImpl().retreiveLatestCurrencyConverterRates()
        }
    }
}
