package ru.dzhaparidze.collegeapp.features.schedule.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ScheduleEvent(
    val id: String? = null,

    @SerialName("ClID")
    val clID: String = "",

    val type: String? = null,

    @SerialName("Day")
    val day: String = "",

    val group: String = "",
    val topic: String = "",
    val start: String = "",
    val end: String = "",
    val room: String = "",
    val color: String = "",
    val title: String = "",

    @SerialName("SubGroup")
    val subGroups: List<ScheduleSubGroup>? = null
)