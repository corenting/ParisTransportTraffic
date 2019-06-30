package fr.corenting.traficparis.traffic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import fr.corenting.traficparis.models.ApiResponse
import fr.corenting.traficparis.models.ApiResponseResults


object TrafficRepository {

    private val apiService: TrafficApiService = Retrofit.Builder()
        .baseUrl("https://api-ratp.pierre-grimaud.fr/v4/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrafficApiService::class.java)

    fun getTraffic(): LiveData<ApiResponseResults> {
        val data = MutableLiveData<ApiResponseResults>()
        apiService.getTraffic()
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()!!.result
                        data.value = res
                    } else {
                        data.value = null
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    data.value = null
                }
            })
        return data
    }
}