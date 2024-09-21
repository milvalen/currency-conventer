package org.example.currency_converter

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.example.currency_converter.presentation.screen.HomePage
import org.example.currency_converter.dependencyInjection.initKoin

@Composable
@Preview
fun App() {
    initKoin()
    MaterialTheme { Navigator(HomePage()) }
}
