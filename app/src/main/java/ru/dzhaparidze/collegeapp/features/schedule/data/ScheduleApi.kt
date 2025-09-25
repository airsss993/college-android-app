package ru.dzhaparidze.collegeapp.features.schedule.data

import io.ktor.http.HttpMethod
import ru.dzhaparidze.collegeapp.core.networking.Endpoint
import ru.dzhaparidze.collegeapp.core.networking.HttpClient.send
import ru.dzhaparidze.collegeapp.features.schedule.models.ScheduleResponse
import ru.dzhaparidze.collegeapp.features.schedule.utils.DateFormatters

interface ScheduleAPIInterface {
    suspend fun fetchSchedule(
        group: String,
        subgroup: String,
        start: String,
        end: String
    ): ScheduleResponse
}

class ScheduleAPI : ScheduleAPIInterface {
    override suspend fun fetchSchedule(
        group: String,
        subgroup: String,
        start: String,
        end: String
    ): ScheduleResponse {
        val endpoint = Endpoint(
            path = "/api/v1/schedule",
            method = HttpMethod.Get,
            queryParams = mapOf(
                "group" to group,
                "subgroup" to subgroup,
                "start" to DateFormatters.request.format(start),
                "end" to DateFormatters.request.format(end)
            ),
        )

        return send(endpoint, "http://localhost:8500")
    }
}

