package fr.corenting.traficparis.traffic

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import fr.corenting.traficparis.models.LineType
import fr.corenting.traficparis.models.api.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")

sealed interface TrafficUiState {
    data object Loading : TrafficUiState
    data class Success(val response: ApiResponse) : TrafficUiState
    data class Error(val message: String?) : TrafficUiState
}

private val defaultFilters = LineType.entries.associateWith { true }

class TrafficViewModel(
    private val repository: TrafficRepository,
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrafficUiState>(TrafficUiState.Loading)
    val uiState: StateFlow<TrafficUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val filters: StateFlow<Map<LineType, Boolean>> = dataStore.data
        .map { prefs ->
            LineType.entries.associateWith { lineType ->
                prefs[booleanPreferencesKey("filter_${lineType.apiName}")] ?: defaultFilters.getValue(lineType)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), defaultFilters)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            runCatching { repository.getTraffic() }
                .onSuccess { _uiState.value = TrafficUiState.Success(it) }
                .onFailure {
                    if (_uiState.value !is TrafficUiState.Success) {
                        _uiState.value = TrafficUiState.Error(it.message)
                    }
                }
            _isRefreshing.value = false
        }
    }

    fun toggleFilter(lineType: LineType) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                val key = booleanPreferencesKey("filter_${lineType.apiName}")
                prefs[key] = !(prefs[key] ?: true)
            }
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer { TrafficViewModel(TrafficRepository(), context.dataStore) }
        }
    }
}
