package ru.dzhaparidze.collegeapp.features.schedule.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ScheduleSubGroup(
    val id: String = UUID.randomUUID().toString(),

    @SerialName("SClID")
    val classId: String,

    @SerialName("SGrID")
    val groupId: String,

    @SerialName("SGCaID")
    val categoryId: String,

    @SerialName("STopic")
    val topic: String,

    @SerialName("STitle")
    val title: String
)