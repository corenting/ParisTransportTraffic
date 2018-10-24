package fr.corenting.traficparis.traffic

import retrofit2.Call
import retrofit2.http.GET
import fr.corenting.traficparis.models.ApiResponse


interface TrafficApiService {

    @GET("traffic")
    fun getTraffic(): Call<ApiResponse>
}