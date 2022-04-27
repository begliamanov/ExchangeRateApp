package dev.begli.exchangerate.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    fun format(date: Date? = null, format: String? = null): String? {
        val simpleDateFormat = if (format.isNullOrEmpty()) {
            SimpleDateFormat("yyyyMMdd")
        } else {
            SimpleDateFormat(format)
        }

        return if (date == null) {
            simpleDateFormat.format(Date())
        } else {
            simpleDateFormat.format(date)
        }
    }
}