package org.example.currency_converter.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import org.example.currency_converter.domain.model.RateCondition
import org.example.currency_converter.getPlatform
import currencyconverter.composeapp.generated.resources.Res
import currencyconverter.composeapp.generated.resources.money_exchange
import currencyconverter.composeapp.generated.resources.refresh
import currencyconverter.composeapp.generated.resources.switch_currency
import org.example.currency_converter.domain.model.CurrencyKey
import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RequestCondition
import org.example.currency_converter.utility.showCurrentDatetime
import staleColor

@Composable
fun HomePageHeader(
    status: RateCondition,
    source: RequestCondition<CurrencyObject>,
    target: RequestCondition<CurrencyObject>,
    amountNumber: Double,
    onAmountNumberChange: (Double) -> Unit,
    onRatesRefresh: () -> Unit,
    onSwitchClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp, 14.dp))
            .background(Color.DarkGray)
            .padding(top = if (getPlatform().name == "Android") 0.dp else 26.dp)
            .padding(26.dp)
    ) {
        Spacer(Modifier.height(10.dp))
        RatesCondition(status, onRatesRefresh)
        Spacer(Modifier.height(26.dp))
        CurrencyInputs(source, target, onSwitchClick)
        Spacer(Modifier.height(26.dp))
        InputAmountNumber(amountNumber, onAmountNumberChange)
    }
}

@Composable
fun RatesCondition(status: RateCondition, onRatesRefresh: () -> Unit) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Row {
            Image(
                painterResource(Res.drawable.money_exchange),
                "Exchange Rate Illustration",
                Modifier.size(80.dp)
            )

            Spacer(Modifier.width(20.dp))
            Column {
                Text(
                    "Today is",
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )

                Text(
                    showCurrentDatetime(),
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )

                Text(
                    status.rateTitle,
                    color = status.rateColor,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            }
        }
    }

    if (status == RateCondition.StaleCondition)
        IconButton(onRatesRefresh) {
            Icon(
                painterResource(Res.drawable.refresh),
                "Refresh Icon",
                Modifier.size(24.dp),
                staleColor
            )
        }
}

@Composable
fun RowScope.ViewCurrency(
    titleHolder: String,
    currencyName: RequestCondition<CurrencyObject>,
    onClick: () -> Unit
) {
    Column(Modifier.weight(1f)) {
        Text(
            titleHolder,
            Modifier.padding(start = 14.dp),
            Color.White,
            MaterialTheme.typography.bodySmall.fontSize
        )

        Spacer(Modifier.height(5.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(size = 10.dp))
                .background(Color.White.copy(0.05f))
                .height(54.dp)
                .clickable { onClick() },
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            if (currencyName.isSuccess()) {
                CurrencyKey.valueOf(currencyName.getSuccessInfo().code).let {
                    Icon(
                        painterResource(it.countryFlag),
                        "Country Flag",
                        Modifier.size(24.dp),
                        Color.Unspecified
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        it.name,
                        color = Color.White,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CurrencyInputs(
    source: RequestCondition<CurrencyObject>,
    target: RequestCondition<CurrencyObject>,
    onSwitchClick: () -> Unit,
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        ViewCurrency("from Currency", source, { if (source.isSuccess()) {} })
        Spacer(Modifier.height(14.dp))

        IconButton(onSwitchClick, Modifier.padding(top = 24.dp)) {
            Icon(
                painterResource(Res.drawable.switch_currency),
                "Switch Currency Icon",
                tint = Color.White
            )
        }

        Spacer(Modifier.height(16.dp))
        ViewCurrency("to", target, { if (source.isSuccess()) {} })
    }
}

@Composable
fun InputAmountNumber(amountNumber: Double, onAmountNumberChange: (Double) -> Unit) {
    Color.White.copy(0.05f).let {
        TextField(
            "$amountNumber",
            { value -> onAmountNumberChange(value.toDouble()) },
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(9.dp))
                .animateContentSize()
                .height(54.dp),
            textStyle = TextStyle(
                Color.White,
                MaterialTheme.typography.titleLarge.fontSize,
                FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = it,
                disabledContainerColor = it,
                focusedContainerColor = it,
                cursorColor = Color.White
            ),
        )
    }
}
