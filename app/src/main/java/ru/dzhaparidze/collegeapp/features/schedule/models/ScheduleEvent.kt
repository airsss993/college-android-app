package ru.dzhaparidze.collegeapp.features.schedule.models

import kotlinx.serialization.SerialName
import java.util.UUID

data class ScheduleEvent(
    val id: String = UUID.randomUUID().toString(),

    @SerialName("ClID")
    val clID: String,

    val type: String?,

    @SerialName("Day")
    val day: String,

    val group: String,
    val topic: String,
    val start: String,
    val end: String,
    val room: String,
    val color: String,
    val title: String,

    @SerialName("SubGroup")
    val subGroups: List<ScheduleSubGroup>?
)