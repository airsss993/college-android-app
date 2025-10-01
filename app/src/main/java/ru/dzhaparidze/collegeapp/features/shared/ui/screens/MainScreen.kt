package ru.dzhaparidze.collegeapp.features.shared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.dzhaparidze.collegeapp.features.schedule.views.ScheduleScreen
import ru.dzhaparidze.collegeapp.features.settings.viewmodels.ThemeViewModel
import ru.dzhaparidze.collegeapp.features.settings.views.AboutAppScreen
import ru.dzhaparidze.collegeapp.features.settings.views.SettingsScreen
import ru.dzhaparidze.collegeapp.features.settings.views.ThemeSelectionScreen

enum class Screen { SETTINGS, SCHEDULE, THEME_SELECTION, ABOUT_APP }

@Composable
fun MainScreen(themeViewModel: ThemeViewModel) {
    val currentScreen = remember { mutableStateOf(Screen.SCHEDULE) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (currentScreen.value != Screen.THEME_SELECTION && currentScreen.value != Screen.ABOUT_APP) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp
                ) {
                    // РАСПИСАНИЕ
                    NavigationBarItem(
                        onClick = { currentScreen.value = Screen.SCHEDULE },
                        icon = {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null
                            )
                        },
                        label = { Text("Расписание") },
                        selected = currentScreen.value == Screen.SCHEDULE,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    // НАСТРОЙКИ
                    NavigationBarItem(
                        onClick = { currentScreen.value = Screen.SETTINGS },
                        icon = {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = null
                            )
                        },
                        label = { Text("Настройки") },
                        selected = currentScreen.value == Screen.SETTINGS,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
