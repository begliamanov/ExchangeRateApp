package dev.begli.exchangerate.model.responses

data class ExchangeRateResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Float>,
    val success: Boolean,
    val timestamp: Int
)