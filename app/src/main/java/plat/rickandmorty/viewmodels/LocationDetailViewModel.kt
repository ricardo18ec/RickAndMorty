package plat.rickandmorty.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import plat.rickandmorty.data.Location
import plat.rickandmorty.data.local.AppDatabase
import plat.rickandmorty.data.repository.LocationRepository
import plat.rickandmorty.navigation.LocationDetailDestination

data class LocationDetailState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val locationId =
        savedStateHandle.toRoute<LocationDetailDestination>().id

    private val repository = LocationRepository(
        AppDatabase.getDatabase(application).locationDao()
    )

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

            // Conservamos el Loading de 2 segundos
            delay(2000)

            try {

                repository.getLocationById(locationId)
                    .collectLatest { location ->

                        _state.value = LocationDetailState(
                            isLoading = false,
                            data = location,
                            hasError = location == null
                        )
                    }

            } catch (e: CancellationException) {
                throw e

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
