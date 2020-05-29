package fr.corenting.traficparis.traffic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers


class TrafficViewModel : ViewModel() {
    private val repository: TrafficRepository = TrafficRepository()

    fun getTraffic() = liveData(Dispatchers.IO) {
        emit(repository.getTraffic())
    }
}