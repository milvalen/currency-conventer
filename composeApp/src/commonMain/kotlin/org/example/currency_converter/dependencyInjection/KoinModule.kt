package org.example.currency_converter.dependencyInjection

import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.dsl.module

import org.example.currency_converter.data.local.MongoDbImpl
import org.example.currency_converter.data.local.SharedPrefsImpl
import org.example.currency_converter.data.remote.thirdPartyAPI.CurrencyConverterApiServiceImpl
import org.example.currency_converter.domain.CurrencyConverterApiService
import org.example.currency_converter.domain.MongoDbRepo
import org.example.currency_converter.domain.SharedPrefsRepo
import org.example.currency_converter.presentation.screen.HomePageViewModel

fun initKoin() {
    startKoin {
        modules(module {
            single<CurrencyConverterApiService> { CurrencyConverterApiServiceImpl(get()) }
            single<SharedPrefsRepo> { SharedPrefsImpl(get()) }
            single<MongoDbRepo> { MongoDbImpl() }
            single { Settings() }

            factory { HomePageViewModel(get(), get(), get()) }
        })
    }
}
