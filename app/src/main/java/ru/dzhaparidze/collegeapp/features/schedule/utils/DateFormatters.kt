package ru.dzhaparidze.collegeapp.features.schedule.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatters {
    val request: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    val uiDate: SimpleDateFormat by lazy {
        SimpleDateFormat("d MMM, EEE", Locale.Builder().setLanguage("ru").setRegion("RU").build())
    }
}