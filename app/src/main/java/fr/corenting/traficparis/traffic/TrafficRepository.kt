package fr.corenting.traficparis.traffic

import fr.corenting.traficparis.models.api.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class TrafficRepository {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getTraffic(): ApiResponse {
        return client.get("https://tchoutchou.9cw.eu/traffic").body<ApiResponse>()
    }
}
