package plat.rickandmorty.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.rickandmorty.data.Location
import plat.rickandmorty.data.LocationDb

data class LocationsState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel : ViewModel() {

    private val _state = MutableStateFlow(LocationsState())

    val state = _state.asStateFlow()

    private var loadingJob: Job? = null

    init {
        cargarLocations()
    }

    fun cargarLocations() {

        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {

            _state.value = LocationsState(
                isLoading = true
            )

            // Simulamos una llamada a Internet
            delay(4000)

            try {

                val locations = LocationDb().getAllLocations()

                _state.value = LocationsState(
                    isLoading = false,
                    data = locations,
                    hasError = false
                )

            } catch (e: Exception) {

                _state.value = LocationsState(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun mostrarError() {

        loadingJob?.cancel()

        _state.value = _state.value.copy(
            isLoading = false,
            hasError = true
        )
    }
}