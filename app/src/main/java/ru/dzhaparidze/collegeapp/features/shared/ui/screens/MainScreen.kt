package ru.dzhaparidze.collegeapp.features.shared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import ru.dzhaparidze.collegeapp.features.schedule.views.ScheduleScreen
import ru.dzhaparidze.collegeapp.features.settings.views.SettingsScreen

enum class Screen { SETTINGS, SCHEDULE }

@Composable
fun MainScreen() {
    val currentScreen = remember { mutableStateOf(Screen.SCHEDULE) }
    val whiteThemeColor = 0xFFF2F2F6

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = androidx.compose.ui.graphics.Color(whiteThemeColor),
        bottomBar = {
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
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            contentAlignment = Alignment.Center

        ) {
            when (currentScreen.value) {
                Screen.SETTINGS -> SettingsScreen()
                Screen.SCHEDULE -> ScheduleScreen()
            }
        }
    }
}
