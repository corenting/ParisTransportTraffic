package fr.corenting.traficparis.traffic

import fr.corenting.traficparis.models.ApiResponse
import fr.corenting.traficparis.models.RequestResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TrafficRepository {

    private val apiService: TrafficApiService = Retrofit.Builder()
        .baseUrl("https://api-ratp.pierre-grimaud.fr/v4/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrafficApiService::class.java)

    suspend fun getTraffic(): RequestResult<ApiResponse> {
        return try {
            RequestResult(data = apiService.getTraffic(), error = null)
        } catch (t: Throwable) {
            RequestResult(data = null, error = t)
        }
    }
}