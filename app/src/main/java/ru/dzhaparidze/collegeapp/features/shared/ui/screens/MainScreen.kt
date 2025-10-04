package ru.dzhaparidze.collegeapp.features.shared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import ru.dzhaparidze.collegeapp.features.schedule.views.ScheduleScreen
import ru.dzhaparidze.collegeapp.features.settings.viewmodels.ThemeViewModel
import ru.dzhaparidze.collegeapp.features.settings.views.AboutAppScreen
import ru.dzhaparidze.collegeapp.features.settings.views.SettingsScreen
import ru.dzhaparidze.collegeapp.features.settings.views.ThemeSelectionScreen

enum class Screen { SETTINGS, SCHEDULE, THEME_SELECTION, ABOUT_APP }

@Composable
fun MainScreen(themeViewModel: ThemeViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.SCHEDULE) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (currentScreen) {
                Screen.SCHEDULE -> ScheduleScreen()

                Screen.SETTINGS -> SettingsScreen(
                    currentTheme = themeViewModel.currentTheme,
                    onThemeClick = {
                        currentScreen = Screen.THEME_SELECTION
                    },
                    onInfoClick = {
                        currentScreen = Screen.ABOUT_APP
                    }
                )

                Screen.ABOUT_APP -> AboutAppScreen(
                    onBackClick = { currentScreen = Screen.SETTINGS }
                )

                Screen.THEME_SELECTION -> ThemeSelectionScreen(
                    currentTheme = themeViewModel.currentTheme,
                    onThemeSelected = { theme ->
                        themeViewModel.setTheme(theme)
                    },
                    onBackClick = { currentScreen = Screen.SETTINGS }
                )
            }
        }

        if (currentScreen != Screen.THEME_SELECTION && currentScreen != Screen.ABOUT_APP) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                CustomNavBar(
                    currentScreen = currentScreen,
                    onScreenChange = { currentScreen = it }
                )
            }
        }
    }
}
