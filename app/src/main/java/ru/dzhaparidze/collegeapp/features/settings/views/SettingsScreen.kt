package ru.dzhaparidze.collegeapp.features.schedule.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}