package ru.dzhaparidze.collegeapp.features.schedule.utils

import java.util.*

object GroupsCatalog {
    val allGroups: List<String> = listOf(
        "ИТ25-11", "ИТ25-12", "ИТ25-13", "ИТ25-14",
        "ИТ24-11", "ИТ24-12", "ИТ24-13", "ИТ24-14",
        "ИТ23-11", "ИТ23-12", "ИТ23-13",
        "ИТ22-11", "ИТ22-12"
    )

    fun courseYear(group: String): Int? {
        val year = GroupSubgroupCompatibility.getYear(group) ?: return null
        val yearInt = year.toIntOrNull() ?: return null

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - (2000 + yearInt)
    }
}
