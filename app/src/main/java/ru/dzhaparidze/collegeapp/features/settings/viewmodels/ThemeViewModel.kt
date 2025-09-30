package ru.dzhaparidze.collegeapp.features.settings.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.dzhaparidze.collegeapp.features.settings.data.ThemeMode

class ThemeViewModel(context: Context) : ViewModel() {
    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    var currentTheme by mutableStateOf(loadTheme())
        private set

    private fun loadTheme(): ThemeMode {
        val themeName = prefs.getString("theme_mode", ThemeMode.SYSTEM.name)
        return ThemeMode.valueOf(themeName ?: ThemeMode.SYSTEM.name)
    }

    fun setTheme(theme: ThemeMode) {
        currentTheme = theme
        prefs.edit().putString("theme_mode", theme.name).apply()
    }
}