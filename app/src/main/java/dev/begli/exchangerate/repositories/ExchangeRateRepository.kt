package dev.begli.exchangerate.repositories

import dev.begli.exchangerate.model.network.ExchangeRatesApi


class ExchangeRateRepository(private val api: ExchangeRatesApi) : BaseRepository() {

    suspend fun getExchangeRates(accessToken: String) = safeApiCall { api.getExchangeRates(accessToken) }

}