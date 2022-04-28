package dev.begli.exchangerate.model.network

import dev.begli.exchangerate.model.responses.ExchangeRateResponse
import retrofit2.http.*

interface ExchangeRatesApi {

    @GET("/v1/latest")
    suspend fun getExchangeRates(@Query("access_key") accessKey: String): ExchangeRateResponse
}