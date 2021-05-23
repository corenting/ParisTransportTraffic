package fr.corenting.traficparis.traffic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.corenting.traficparis.models.RequestResult
import fr.corenting.traficparis.models.api.ApiResponse
import kotlinx.coroutines.launch


class TrafficViewModel : ViewModel() {
    private val repository: TrafficRepository = TrafficRepository()
    private val data = MutableLiveData<RequestResult<ApiResponse>>()

    init {
        getLatestTraffic()
    }

    fun getLatestTraffic() {
        viewModelScope.launch {
            data.postValue(repository.getTraffic())
        }
    }

    fun getTraffic(): LiveData<RequestResult<ApiResponse>> {
        return data
    }
}