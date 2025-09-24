package ru.dzhaparidze.collegeapp.features.schedule.utils

object GroupSubgroupCompatibility {
    private const val ALL_SUBGROUP = "*"
    private val firstYearSubgroups = listOf("Подгр1", "Подгр2", "Подгр3", "Подгр4")
    private val profiles = listOf("BE", "FE", "GD", "PM", "SA", "CD")
    private val englishByYear = mapOf(
        "25" to listOf("A0.11", "A0.12", "A1.11", "A1.12", "A2.11", "A2.12", "B1.11", "B1.12"),
        "24" to listOf("A0.21", "A1.21", "A1.22", "A1.23", "A2.21", "A2.22", "B1.21", "B1.22"),
        "23" to listOf("A1.31", "A2.31", "B1.31"),
        "22" to listOf("A1.41", "A2.41", "B1.41")
    )

    fun getYear(group: String): String? {
        val components = group.split(Regex("[^0-9]")).filter { it.isNotEmpty() }
        return components.firstOrNull { it.length == 2 }
    }

    fun availableSubgroups(group: String): List<String> {
        val year = getYear(group) ?: return listOf(ALL_SUBGROUP)

        val subgroups = mutableListOf(ALL_SUBGROUP)

        when (year) {
            "25" -> {
                subgroups.addAll(firstYearSubgroups)
                subgroups.addAll(englishByYear["25"] ?: emptyList())
            }
            "24", "23", "22" -> {
                subgroups.addAll(profiles)
                subgroups.addAll(englishByYear[year] ?: emptyList())
            }
        }

        return subgroups
    }

    fun isValidSubgroup(subgroup: String, group: String): Boolean {
        val availableSubgroups = availableSubgroups(group)
        return availableSubgroups.contains(subgroup)
    }

    fun validatedSubgroup(current: String, group: String): String {
        return if (isValidSubgroup(current, group)) {
            current
        } else {
            ALL_SUBGROUP
        }
    }

}
