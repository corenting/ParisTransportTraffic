package fr.corenting.traficparis.traffic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.corenting.traficparis.models.LineType
import fr.corenting.traficparis.models.RequestResult
import fr.corenting.traficparis.models.api.ApiResponse
import fr.corenting.traficparis.utils.PersistenceUtils
import kotlinx.coroutines.launch
import kotlin.collections.mutableMapOf
import kotlin.collections.set


class TrafficViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TrafficRepository = TrafficRepository()

    private val trafficData = MutableLiveData<RequestResult<ApiResponse>>()
    private val displayFilters = mutableMapOf<LineType, Boolean>()

    init {
        initDisplayFilterValue()
        refreshTrafficData()
    }

    private fun initDisplayFilterValue() {
        for (lineType in LineType.values()) {
            displayFilters[lineType] = PersistenceUtils.getDisplayCategoryValue(getApplication(), lineType)
        }
    }

    fun updateDisplayFilterValue(lineType: LineType, newValue: Boolean) {
        PersistenceUtils.setValue(getApplication(), lineType, newValue)
        displayFilters[lineType] = newValue
    }

    fun refreshTrafficData() {
        viewModelScope.launch {
            trafficData.postValue(repository.getTraffic())
        }
    }

    fun getTrafficData(): LiveData<RequestResult<ApiResponse>> {
        return trafficData
    }

    fun getDisplayFilters(): Map<LineType, Boolean> {
        return displayFilters
    }
}