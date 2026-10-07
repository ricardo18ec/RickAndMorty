package plat.rickandmorty.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.rickandmorty.data.Location
import plat.rickandmorty.data.LocationDb
import plat.rickandmorty.navigation.LocationDetailDestination

data class LocationDetailState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val locationId =
        savedStateHandle.toRoute<LocationDetailDestination>().id

    private val _state = MutableStateFlow(LocationDetailState())

    val state = _state.asStateFlow()

    private var loadingJob: Job? = null

    init {
        cargarLocation()
    }

    fun cargarLocation() {

        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {

            _state.value = LocationDetailState(
                isLoading = true
            )

            delay(2000)

            try {

                val location = LocationDb()
                    .getLocationById(locationId)

                _state.value = LocationDetailState(
                    isLoading = false,
                    data = location,
                    hasError = false
                )

            } catch (e: Exception) {

                _state.value = LocationDetailState(
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