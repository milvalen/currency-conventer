package org.example.currency_converter.utility

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun showCurrentDatetime(): String {
    val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    date.dayOfMonth.let {
        return "$it${mapOf(1 to "st", 2 to "nd", 3 to "rd")[it % 10] ?: "th"} " +
                "${date.month}".lowercase().replaceFirstChar {
                    char -> char.titlecase()
                } + ", ${date.year}"
    }
}
