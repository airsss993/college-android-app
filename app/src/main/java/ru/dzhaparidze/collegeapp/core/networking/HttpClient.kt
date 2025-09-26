package ru.dzhaparidze.collegeapp.core.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClient {
    val instance = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
        }
    }

    suspend inline fun <reified T> send(
        endpoint: Endpoint,
        baseUrl: String
    ): T {
        val response: HttpResponse = instance.get(baseUrl + endpoint.path) {
            url {
                endpoint.queryParams.forEach { (key, value) ->
                    parameters.append(key, value)
                }
            }
        }

        val jsonString = response.bodyAsText()
        return Json.decodeFromString<T>(jsonString)
    }
}

data class Endpoint(
    val path: String,
    val method: HttpMethod,
    val queryParams: Map<String, String>,
)
