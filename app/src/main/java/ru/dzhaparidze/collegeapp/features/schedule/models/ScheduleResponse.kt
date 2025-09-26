package ru.dzhaparidze.collegeapp.features.schedule.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    @SerialName("events")
    val event: List<ScheduleEvent>
)