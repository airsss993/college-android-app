package ru.dzhaparidze.collegeapp.features.schedule.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import ru.dzhaparidze.collegeapp.features.schedule.models.ScheduleEvent

@Composable
fun EventCard(event: ScheduleEvent) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF2F2F6)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ВРЕМЯ ПАРЫ
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                val timeStart = event.start
                val timeEnd = event.end

                Text(
                    text = timeStart,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = timeEnd,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.DarkGray
                )
            }

            // ВЕРТИКАЛЬНАЯ ПОЛОСКА
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(52.dp)
                    .background(Color(0xFFBB3ED8))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // ИНФО О ПРЕДМЕТЕ
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (event.topic.isNotBlank()) {
                    Text(
                        text = event.topic,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (event.room.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFE3E2E9),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.NearMe,
                                    contentDescription = null,
                                    tint = Color(0xFF808085),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = event.room,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF808085),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}