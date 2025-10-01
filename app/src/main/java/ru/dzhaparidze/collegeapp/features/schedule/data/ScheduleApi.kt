package ru.dzhaparidze.collegeapp.features.schedule.data

import android.util.Log
import io.ktor.http.HttpMethod
import ru.dzhaparidze.collegeapp.core.networking.Endpoint
import ru.dzhaparidze.collegeapp.core.networking.HttpClient.send
import ru.dzhaparidze.collegeapp.features.schedule.models.ScheduleResponse

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
        Log.d("ScheduleAPI", "Send request to get schedule")
        Log.d("ScheduleAPI", "Group: $group, Subgroup: $subgroup")
        Log.d("ScheduleAPI", "Range: $start - $end")

        val endpoint = Endpoint(
            path = "/api/v1/schedule",
            method = HttpMethod.Get,
            queryParams = mapOf(
                "group" to group,
                "subgroup" to subgroup,
                "start" to start,
                "end" to end
            ),
        )

        val url = "http://79.174.86.248:8500${endpoint.path}"
        Log.d("ScheduleAPI", "Request URL: $url")
        Log.d("ScheduleAPI", "Params: ${endpoint.queryParams}")

        return try {
            val baseUrl = "http://79.174.86.248:8500"
            Log.d("ScheduleAPI", "Using base URL: $baseUrl")

            val response = send<ScheduleResponse>(endpoint, baseUrl)
            Log.d("ScheduleAPI", "Request success, get events: ${response.event.size}")
            response
        } catch (e: Exception) {
            Log.e("ScheduleAPI", "Error request: ${e.message}", e)
            throw e
        }
    }
}

