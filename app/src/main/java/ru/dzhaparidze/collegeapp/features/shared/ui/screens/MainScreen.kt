package ru.dzhaparidze.collegeapp.features.shared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import ru.dzhaparidze.collegeapp.features.schedule.views.ScheduleScreen
import ru.dzhaparidze.collegeapp.features.settings.viewmodels.ThemeViewModel
import ru.dzhaparidze.collegeapp.features.settings.views.AboutAppScreen
import ru.dzhaparidze.collegeapp.features.settings.views.SettingsScreen
import ru.dzhaparidze.collegeapp.features.settings.views.ThemeSelectionScreen

enum class Screen { SETTINGS, SCHEDULE, THEME_SELECTION, ABOUT_APP }

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val themeViewModel = remember { ThemeViewModel(context) }
    val currentScreen = remember { mutableStateOf(Screen.SCHEDULE) }
    val whiteThemeColor = 0xFFF2F2F6

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = androidx.compose.ui.graphics.Color(whiteThemeColor),
        bottomBar = {
            if (currentScreen.value != Screen.THEME_SELECTION && currentScreen.value != Screen.ABOUT_APP) {
                NavigationBar(
                    containerColor = androidx.compose.ui.graphics.Color(whiteThemeColor)
                ) {
                    // РАСПИСАНИЕ
                    NavigationBarItem(
                        onClick = { currentScreen.value = Screen.SCHEDULE },
                        icon = {
                            Icon(
                                Icons.Default.DateRange,
                                "DateRangeIcon"
                            )
                        },
                        label = { Text("Расписание") },
                        selected = currentScreen.value == Screen.SCHEDULE,
                    )

                    // НАСТРОЙКИ
                    NavigationBarItem(
                        onClick = { currentScreen.value = Screen.SETTINGS },
                        icon = {
                            Icon(
                                Icons.Default.Settings,
                                "SettingIcon"
                            )
                        },
                        label = { Text("Настройки") },
                        selected = currentScreen.value == Screen.SETTINGS,
                    )
                }
            }
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
        ) {
            when (currentScreen.value) {
                Screen.SETTINGS -> SettingsScreen(
                    currentTheme = themeViewModel.currentTheme,
                    onThemeClick = {
                        currentScreen.value = Screen.THEME_SELECTION
                    },
                    onInfoClick = {
                        currentScreen.value = Screen.ABOUT_APP
                    }
                )

                Screen.SCHEDULE -> ScheduleScreen()

                Screen.THEME_SELECTION -> ThemeSelectionScreen(
                    currentTheme = themeViewModel.currentTheme,
                    onThemeSelected = { theme ->
                        themeViewModel.setTheme(theme)
                    },
                    onBackClick = { currentScreen.value = Screen.SETTINGS }
                )

                Screen.ABOUT_APP -> AboutAppScreen(
                    onBackClick = { currentScreen.value = Screen.SETTINGS }
                )
            }
        }
    }
}
