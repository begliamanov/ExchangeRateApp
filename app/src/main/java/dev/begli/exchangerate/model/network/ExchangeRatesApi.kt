package dev.begli.exchangerate.model.network

import dev.begli.exchangerate.model.responses.ExchangeRateResponse
import retrofit2.http.*

interface ExchangeRatesApi {

    @GET("/v1/latest?access_key=b9c5ba2a01634b1cab862029aa13087f&format=1")
    suspend fun getExchangeRates(): ExchangeRateResponse
}