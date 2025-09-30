package ru.dzhaparidze.collegeapp.features.settings.views

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import ru.dzhaparidze.collegeapp.R

@Composable
fun AboutAppScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable(onClick = onBackClick)
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "О приложении",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 32.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "МойКЦТ для Android",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Версия 0.1",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "© 2021-2025 АНПОО \"Колледж Цифровых Технологий\"",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Учебное приложение для студентов колледжа цифровых технологий",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
            )
        }

        Text(
            text = "Действия",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF86868A),
            modifier = Modifier.padding(start = 32.dp, end = 16.dp, bottom = 4.dp, top = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            LinkButton(
                icon = Icons.Default.BugReport,
                text = "Сообщить о проблеме",
                url = "https://t.me/airsss993",
                showDivider = true
            )

            LinkButton(
                icon = Icons.Default.Code,
                text = "Исходный код",
                url = "https://github.com/airsss993/college-android-app",
                showDivider = true
            )

            LinkButton(
                icon = Icons.Default.Language,
                text = "Веб-сайт",
                url = "https://it-college.ru/",
                showDivider = false
            )
        }

        Text(
            text = "Разработчики",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF86868A),
            modifier = Modifier.padding(start = 32.dp, end = 16.dp, bottom = 4.dp, top = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            DeveloperButton(
                name = "Иван Коломацкий",
                role = "iOS разработчик",
                githubUrl = "https://github.com/anton1ks96",
                telegramUrl = "https://t.me/IKolomatskii",
                showDivider = true
            )

            DeveloperButton(
                name = "Артём Джапаридзе",
                role = "Android разработчик",
                githubUrl = "https://github.com/airsss993",
                telegramUrl = "https://t.me/airsss993",
                showDivider = false
            )
        }

        Text(
            text = "Маркетинг",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF86868A),
            modifier = Modifier.padding(start = 32.dp, end = 16.dp, bottom = 4.dp, top = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            DeveloperButton(
                name = "Илья Некрасов",
                role = "Маркетолог",
                githubUrl = "https://github.com/necrasov-ilya",
                telegramUrl = "https://t.me/NKSV_ILYA",
                showDivider = false
            )
        }
    }
}

@Composable
private fun DeveloperButton(
    name: String,
    role: String,
    githubUrl: String,
    telegramUrl: String,
    showDivider: Boolean
) {
    val uriHandler = LocalUriHandler.current

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp, 40.dp)
                        .shadow(2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFFDFDFD))
                        .clickable(
                            indication = ripple(),
                            interactionSource = remember { MutableInteractionSource() }
                        ) { uriHandler.openUri(githubUrl) },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEEEEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.github_icon),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(50.dp, 40.dp)
                        .shadow(2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFFDFDFD))
                        .clickable(
                            indication = ripple(),
                            interactionSource = remember { MutableInteractionSource() }
                        ) { uriHandler.openUri(telegramUrl) },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEEEEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.telegram_icon),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(18.dp)
                                .scale(scaleX = 1f, scaleY = -1f)
                        )
                    }
                }
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFFE3E2E9)
            )
        }
    }
}

@Composable
private fun LinkButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    url: String,
    showDivider: Boolean
) {
    val uriHandler = LocalUriHandler.current

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { uriHandler.openUri(url) }
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFFE3E2E9)
            )
        }
    }
}