package fr.corenting.traficparis.traffic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import fr.corenting.traficparis.models.api.ApiResponse
import fr.corenting.traficparis.models.RequestResult
import kotlinx.coroutines.Dispatchers


class TrafficViewModel : ViewModel() {
    private val repository: TrafficRepository = TrafficRepository()
    private val data = MutableLiveData<RequestResult<ApiResponse>>()

    fun getUpdatedTraffic() = liveData(Dispatchers.IO) {
        data.postValue(repository.getTraffic())
        emit(data)
    }

    fun getTraffic() = liveData(Dispatchers.IO) {
        emit(data)
    }
}