package org.example.currency_converter.domain.model

import androidx.compose.ui.graphics.Color
import freshColor
import staleColor

enum class RateCondition(val rateTitle: String, val rateColor: Color) {
    IdleCondition("----", Color.White),
    FreshCondition("New Rates", freshColor),
    StaleCondition("Rates are not New", staleColor)
}
