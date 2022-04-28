package dev.begli.exchangerate.utils

class Exchange {

    private fun exchange(baseRate: Float, currencyRate: Float): Float {
        return currencyRate/baseRate
    }

    fun exchangeAllRates(baseRate: Float, rates: Map<String, Float>): Map<String, Float> {
        return rates.mapValues {
            exchange(baseRate, it.value)
        }
    }

}