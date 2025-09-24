package ru.dzhaparidze.collegeapp.features.shared.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                // TODO: добавить логику выбранной вкладки
                NavigationBarItem(
                    onClick = { },
                    icon = {
                        Icon(
                            Icons.Default.Settings,
                            "SettingIcon"
                        )
                    },
                    label = { Text("Настройки") },
                    selected = false
                )

                NavigationBarItem(
                    onClick = { },
                    icon = {
                        Icon(
                            Icons.Default.DateRange,
                            "DateRangeIcon"
                        )
                    },
                    label = { Text("Расписание") },
                    selected = true
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
            Text(
                text = "Главная",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}