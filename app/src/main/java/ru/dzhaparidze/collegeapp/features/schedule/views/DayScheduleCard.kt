package ru.dzhaparidze.collegeapp.features.schedule.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.dzhaparidze.collegeapp.features.schedule.models.ScheduleEvent
import ru.dzhaparidze.collegeapp.features.schedule.utils.DateFormatters
import java.text.ParseException

@Composable
fun DayScheduleCard(day: String, events: List<ScheduleEvent>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = try {
                        val date = DateFormatters.request.parse(day)
                        if (date != null) DateFormatters.uiDate.format(date) else day
                    } catch (e: ParseException) { day },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "${events.size} ${getEventCountText(events.size)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            events.forEach { event ->
                EventCard(event = event)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private fun getEventCountText(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "занятие"
        count % 10 in 2..4 && (count % 100 < 10 || count % 100 >= 20) -> "занятия"
        else -> "занятий"
    }
}