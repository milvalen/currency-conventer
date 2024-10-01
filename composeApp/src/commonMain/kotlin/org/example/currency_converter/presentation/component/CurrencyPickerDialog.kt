package org.example.currency_converter.presentation.component

import androidx.compose.runtime.*

import org.example.currency_converter.domain.CurrencyType
import org.example.currency_converter.domain.model.CurrencyKey
import org.example.currency_converter.domain.model.CurrencyObject

// TODO: currency pick dialog

@Composable
fun CurrencyPickerDialog(
    currencies: List<CurrencyObject>,
    currencyType: CurrencyType,
    onConfirmClick: (CurrencyKey) -> Unit,
    onDismiss: () -> Unit
) {
    val allCurrencies = remember(currencies) { mutableStateListOf<CurrencyObject>().apply { addAll(currencies) } }

    var chosenCurrencyKey by remember(currencyType) { mutableStateOf(currencyType.key) }
    var currencyQuery by remember { mutableStateOf("") }
}
