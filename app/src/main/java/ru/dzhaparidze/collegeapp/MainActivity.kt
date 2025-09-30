package ru.dzhaparidze.collegeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.*
import ru.dzhaparidze.collegeapp.features.settings.data.ThemeMode
import ru.dzhaparidze.collegeapp.features.settings.viewmodels.ThemeViewModel
import ru.dzhaparidze.collegeapp.features.shared.ui.screens.MainScreen
import ru.dzhaparidze.collegeapp.features.shared.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val themeViewModel = remember { ThemeViewModel(context) }
            val isSystemDark = isSystemInDarkTheme()

            val darkTheme = when (themeViewModel.currentTheme) {
                ThemeMode.SYSTEM -> isSystemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            AppTheme(darkTheme = darkTheme) {
                MainScreen(themeViewModel = themeViewModel)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    val context = LocalContext.current
    val themeViewModel = remember { ThemeViewModel(context) }

    AppTheme {
        MainScreen(themeViewModel = themeViewModel)
    }
}