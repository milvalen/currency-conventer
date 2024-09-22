package org.example.currency_converter.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

        Column(Modifier.fillMaxSize().background(surfaceColor)) {
            HomePageHeader(rateCondition) { viewModel.passEvent(HomePageUiEvent.RefreshRatesEvent) }
        }
    }
}
