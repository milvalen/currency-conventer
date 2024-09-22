package org.example.currency_converter.data.remote.thirdPartyAPI

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

import org.example.currency_converter.domain.CurrencyConverterApiService
import org.example.currency_converter.domain.SharedPrefsRepo
import org.example.currency_converter.domain.model.ApiResponse
import org.example.currency_converter.domain.model.CurrencyKey
import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RequestCondition

class CurrencyConverterApiServiceImpl(
    private val prefs: SharedPrefsRepo
): CurrencyConverterApiService {
    companion object {
        const val API_KEY = "cur_live_M1DKPGLUqPgR1UstWDpVeKypv4BJsA35MaZOZ5UV"
        const val API_ENDPOINT = "https://api.currencyapi.com/v3/latest"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout) { requestTimeoutMillis = 16000 }
        install(DefaultRequest) { headers { append("apikey", API_KEY) } }
    }

    override suspend fun retreiveLatestCurrencyConverterRates()
    : RequestCondition<List<CurrencyObject>> {
        return try {
            val apiResponse = httpClient.get(API_ENDPOINT)

            if (apiResponse.status.value == 200)
                Json.decodeFromString<ApiResponse>(apiResponse.body()).let {
                    prefs.storeLastUpdatedTime(it.meta.last_updated_at)
                    println("API Response=$it")

                    RequestCondition.SuccessCondition(
                        it.data.values.filter {
                            currency -> it.data.keys.filter {
                                key -> CurrencyKey.entries.map {
                                    entry -> entry.name
                                }.toSet().contains(key)
                            }.contains(currency.code)
                        }
                    )
            } else RequestCondition.ErrorCondition("HTTP Error: ${apiResponse.status}")
        } catch (error: Exception) { RequestCondition.ErrorCondition("${error.message}") }
    }
}
