package org.example.currency_converter.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.CurrencyKey
import org.example.currency_converter.domain.CurrencyType
import primaryColor
import textColor

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

    AlertDialog(
        onDismiss,
        { TextButton({ onConfirmClick(chosenCurrencyKey) }) { Text("Confirm", color = primaryColor) } },
        dismissButton = { TextButton(onDismiss) { Text("Close", color =  MaterialTheme.colorScheme.outline) } },
        title = { Text("Search and Choose Currency", color = textColor) },
        containerColor = Color.White,
        text = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    currencyQuery,
                    { userInput ->
                        currencyQuery = userInput.uppercase()

                        if (userInput.isNotEmpty()) {
                            allCurrencies.filter { currency -> currency.code.contains(currencyQuery) }.let {
                                allCurrencies.clear()
                                allCurrencies.addAll(it)
                            }
                        } else {
                            allCurrencies.clear()
                            allCurrencies.addAll(currencies)
                        }
                    },
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(100.dp)),
                    textStyle = TextStyle(textColor, MaterialTheme.typography.bodySmall.fontSize),
                    singleLine = true,
                    placeholder = {
                        Text(
                            "write here",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = textColor.copy(0.39f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = textColor.copy(0.1f),
                        disabledContainerColor = textColor.copy(0.1f),
                        focusedContainerColor = textColor.copy(0.1f),
                        errorContainerColor = textColor.copy(0.1f),
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = textColor
                    )
                )

                Spacer(Modifier.height(22.dp))

                AnimatedContent(allCurrencies) { list ->
                    if (list.isNotEmpty())
                        LazyColumn(
                            Modifier.fillMaxWidth().height(251.dp),
                            verticalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            items(
                                list,
                                { currency -> currency.docID.toHexString() }
                            ) { option ->
                                CurrencyCodePickerView(
                                    CurrencyKey.valueOf(option.code),
                                    chosenCurrencyKey.name == option.code
                                ) { selected -> chosenCurrencyKey = selected }
                            }
                        }
                    else ErrorScreen(Modifier.height(251.dp))
                }
            }
        },
    )
}
