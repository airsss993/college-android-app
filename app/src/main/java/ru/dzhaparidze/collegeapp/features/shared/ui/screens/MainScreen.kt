package ru.dzhaparidze.collegeapp.features.shared.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.dzhaparidze.collegeapp.features.schedule.views.ScheduleScreen
import ru.dzhaparidze.collegeapp.features.settings.views.SettingsScreen

enum class Screen { SETTINGS, SCHEDULE }

@Composable
fun MainScreen() {
    val currentScreen = remember { mutableStateOf(Screen.SCHEDULE) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
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
                .padding(paddings)
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.Center

        ) {
            when (currentScreen.value) {
                Screen.SETTINGS -> SettingsScreen()
                Screen.SCHEDULE -> ScheduleScreen()
            }
        }
    }
}
