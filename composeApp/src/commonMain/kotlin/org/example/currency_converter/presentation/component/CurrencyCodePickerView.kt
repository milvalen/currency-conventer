package org.example.currency_converter.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import org.example.currency_converter.domain.model.CurrencyKey
import surfaceColor
import primaryColor
import textColor

@Composable
fun CurrencyCodePickerView(currencyKey: CurrencyKey, isSelected: Boolean, onSelect: (CurrencyKey) -> Unit) {
    val animationTarget = if (isSelected) 1f else 0f
    val saturation = remember { Animatable(animationTarget) }
    val colorMatrix = remember(saturation.value) { ColorMatrix().apply { setToSaturation(saturation.value) } }
    val animatedAlpha by animateFloatAsState(if (isSelected) 1f else 0.5f, tween(300))

    LaunchedEffect(isSelected) { saturation.animateTo(animationTarget) }

    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(9.dp)).clickable { onSelect(currencyKey) }.padding(9.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
            Row {
                Image(
                    painterResource(currencyKey.countryFlag),
                    "Currency Country Flag",
                    Modifier.size(24.dp),
                    colorFilter = ColorFilter.colorMatrix(colorMatrix)
                )

                Spacer(Modifier.width(8.dp))
                Text(currencyKey.name, Modifier.alpha(animatedAlpha), textColor, fontWeight = FontWeight.Bold)
            }

            CurrencyKeySelector(isSelected)
    }
}

@Composable
fun CurrencyKeySelector(isSelected: Boolean = false) {
    val animatedColor by animateColorAsState(
        if (isSelected) primaryColor else textColor.copy(0.1f),
        tween(300)
    )

    Box(Modifier.size(19.dp).clip(CircleShape).background(animatedColor), Alignment.Center) {
        if (isSelected)
            Icon(Icons.Default.Check, "User Selector Check Icon", Modifier.size(13.dp), surfaceColor)
    }
}
