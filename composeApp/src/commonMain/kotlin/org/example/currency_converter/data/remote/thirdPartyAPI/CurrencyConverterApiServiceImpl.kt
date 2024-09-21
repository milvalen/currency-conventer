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
import org.example.currency_converter.domain.model.ApiResponse
import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RequestCondition

class CurrencyConverterApiServiceImpl: CurrencyConverterApiService {
    companion object {
        const val API_KEY = ""
        const val API_ENDPOINT = ""
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

            if (apiResponse.status.value == 200) {
                println("API Response=${apiResponse.body<String>()}")

                RequestCondition.SuccessCondition(
                    Json.decodeFromString<ApiResponse>(apiResponse.body()).data.values.toList()
                )
            } else {
                RequestCondition.ErrorCondition("HTTP Error: ${apiResponse.status}")
            }
        } catch (error: Exception) {
            RequestCondition.ErrorCondition("${error.message}")
        }
    }
}
