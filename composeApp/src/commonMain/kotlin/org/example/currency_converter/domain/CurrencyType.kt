package org.example.currency_converter.domain

import org.example.currency_converter.domain.model.CurrencyKey

sealed class CurrencyType(val key: CurrencyKey) {
    data object None: CurrencyType(CurrencyKey.USD)

    data class Source(val currencyCode: CurrencyKey): CurrencyType(currencyCode)
    data class Target(val currencyCode: CurrencyKey): CurrencyType(currencyCode)
}
