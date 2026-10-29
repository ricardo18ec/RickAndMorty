package plat.rickandmorty.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

data class LocationsState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = LocationRepository(
        AppDatabase.getDatabase(application).locationDao()
    )

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

            // Conservamos el Loading del Lab 9
            delay(4000)

            try {

                repository.getAllLocations()
                    .collectLatest { locations ->

                        _state.value = LocationsState(
                            isLoading = false,
                            data = locations,
                            hasError = false
                        )
                    }

            } catch (e: CancellationException) {
                throw e

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
