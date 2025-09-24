package ru.dzhaparidze.collegeapp.features.schedule.utils

import java.util.Date

data class DateRange(var start: Date, var end: Date) {
    init {
        if (end.before(start)) {
            val temp = start
            start = end
            end = temp
        }
    }
}