package ru.dzhaparidze.collegeapp.features.schedule.views

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import ru.dzhaparidze.collegeapp.features.schedule.models.ScheduleEvent
import ru.dzhaparidze.collegeapp.features.schedule.utils.getFullSubgroupName

@Composable
fun EventCard(event: ScheduleEvent) {
    var isExpanded by remember { mutableStateOf(false) }
    val hasSubGroups = !event.subGroups.isNullOrEmpty()

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
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (event.room.isNotBlank()) {
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

                    // КНОПКА С ПОДГРУППАМИ
                    if (hasSubGroups) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFEBDFF4),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { isExpanded = !isExpanded }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Group,
                                    contentDescription = null,
                                    tint = Color(0xFFBB3DD8),
                                    modifier = Modifier.size(18.dp)
                                )

                                Text(
                                    text = "${event.subGroups.size} подгруппы",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFBB3DD8),
                                    fontWeight = FontWeight.Medium
                                )

                                Icon(
                                    if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color(0xFFBB3DD8),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (isExpanded && hasSubGroups) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFDCDCE0))
            )
        }

        AnimatedVisibility(
            visible = isExpanded && hasSubGroups,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF2F2F6)
                ),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    event.subGroups?.forEach { subGroup ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // НАЗВАНИЕ ПРОФИЛЯ
                                    Text(
                                        text = getFullSubgroupName(subGroup.groupId),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )

                                    // НАЗВАНИЕ ПРЕДМЕТА
                                    Text(
                                        text = subGroup.title,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )

                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFEFEFF1),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.NearMe,
                                            contentDescription = null,
                                            tint = Color(0xFF808085),
                                            modifier = Modifier.size(12.dp)
                                        )

                                        Text(
                                            text = subGroup.categoryId,
                                            style = MaterialTheme.typography.bodySmall,
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
    }
}