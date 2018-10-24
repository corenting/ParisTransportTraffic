package fr.corenting.traficparis.traffic

import androidx.lifecycle.*
import fr.corenting.traficparis.models.ApiResponseResults


class TrafficViewModel : ViewModel() {
    private lateinit var results: LiveData<ApiResponseResults>


    fun getTraffic(): LiveData<ApiResponseResults> {
        results = TrafficRepository.getTraffic()
        return this.results
    }
}