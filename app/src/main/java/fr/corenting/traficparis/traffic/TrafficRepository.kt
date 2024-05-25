package fr.corenting.traficparis.traffic

import fr.corenting.traficparis.models.RequestResult
import fr.corenting.traficparis.models.api.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson

class TrafficRepository {
    suspend fun getTraffic(): RequestResult<ApiResponse> {
        return try {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    gson()
                }
            }
            val apiResponse: ApiResponse = client.get("https://tchoutchou.9cw.eu/traffic").body()
            RequestResult(data = apiResponse, error = null)
        } catch (t: Throwable) {
            RequestResult(data = null, error = t)
        }
    }
}