package org.example.currency_converter.presentation.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource


import headerColor
import org.example.currency_converter.domain.model.RateCondition
import org.example.currency_converter.getPlatform
import currencyconverter.composeapp.generated.resources.Res
import currencyconverter.composeapp.generated.resources.money_exchange
import currencyconverter.composeapp.generated.resources.refresh_ic
import org.example.currency_converter.utility.showCurrentDatetime
import staleColor


@Composable
fun HomePageHeader(status: RateCondition, onRatesRefresh: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp, 14.dp))
            .background(headerColor)
            .padding(top = if (getPlatform().name == "Android") 0.dp else 26.dp)
            .padding(26.dp)
    ) {
        Spacer(Modifier.height(26.dp))
        RatesCondition(status, onRatesRefresh)
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

    if (status == RateCondition.StaleCondition) IconButton(onRatesRefresh) {
        Icon(
            painterResource(Res.drawable.refresh_ic),
            "Refresh Icon",
            Modifier.size(24.dp),
            staleColor
        )
    }
}
