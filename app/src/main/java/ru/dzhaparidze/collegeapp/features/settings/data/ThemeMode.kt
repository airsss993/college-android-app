package ru.dzhaparidze.collegeapp.features.settings.data

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK;

    fun toDisplayString(): String {
        return when (this) {
            SYSTEM -> "Системная"
            LIGHT -> "Светлая"
            DARK -> "Тёмная"
        }
    }
}