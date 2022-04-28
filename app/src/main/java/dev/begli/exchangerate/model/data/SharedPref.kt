package dev.begli.exchangerate.model.data

import android.content.Context
import android.content.SharedPreferences
import dev.begli.exchangerate.utils.Constants.Companion.FIVE_SECONDS

class SharedPref(private val context: Context) {
    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    // Shared pref mode
    private val MODE_PRIVATE = 0

    init {
        pref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        editor = pref.edit()
    }

    companion object {
        // LogCat tag
        private val TAG: String = SharedPref::class.java.simpleName
        // Shared preferences file name
        private const val PREF_NAME = "ExchangeRate.App"

        private const val DEFAULT_RATE = "EUR"
        private const val REFRESH_INTERVAL: Long = FIVE_SECONDS
    }

    fun setDefaultRate(rate: String) {
        editor.putString(DEFAULT_RATE, rate)
        editor.commit()
    }

    fun defaultRate(): String? {
        return pref.getString(DEFAULT_RATE, "EUR")
    }

    fun setRefreshInterval(interval: Long) {
        editor.putLong(REFRESH_INTERVAL.toString(), interval)
        editor.commit()
    }

    fun refreshInterval(): Long {
        return pref.getLong(REFRESH_INTERVAL.toString(), FIVE_SECONDS)
    }

}