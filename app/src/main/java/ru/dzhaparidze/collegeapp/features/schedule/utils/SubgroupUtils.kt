package ru.dzhaparidze.collegeapp.features.schedule.utils

fun getFullSubgroupName(shortName: String): String {
    return when (shortName.uppercase()) {
        "BE" -> "Backend"
        "GD" -> "Game Dev"
        "PM" -> "Project Management"
        "SA" -> "System Administration"
        "FE" -> "Frontend"
        "CD" -> "Computer Design"
        else -> shortName
    }
}