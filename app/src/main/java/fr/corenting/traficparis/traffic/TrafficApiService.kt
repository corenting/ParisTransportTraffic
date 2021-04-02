package fr.corenting.traficparis.traffic

import fr.corenting.traficparis.models.api.ApiResponse
import retrofit2.http.GET


interface TrafficApiService {

    @GET("traffic")
    suspend fun getTraffic(): ApiResponse
}