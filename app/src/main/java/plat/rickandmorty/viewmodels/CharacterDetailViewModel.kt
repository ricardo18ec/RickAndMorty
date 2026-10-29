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
import plat.rickandmorty.data.Character
import plat.rickandmorty.data.local.AppDatabase
import plat.rickandmorty.data.repository.CharacterRepository
import plat.rickandmorty.navigation.CharacterDetailDestination

data class CharacterDetailState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val characterId =
        savedStateHandle.toRoute<CharacterDetailDestination>().id

    private val repository = CharacterRepository(
        AppDatabase.getDatabase(application).characterDao()
    )

    private val _state = MutableStateFlow(CharacterDetailState())

    val state = _state.asStateFlow()

    private var loadingJob: Job? = null

    init {
        cargarPersonaje()
    }

    fun cargarPersonaje() {

        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {

            _state.value = CharacterDetailState(
                isLoading = true
            )

            delay(2000)

            try {

                repository.getCharacterById(characterId)
                    .collectLatest { personaje ->

                        _state.value = CharacterDetailState(
                            isLoading = false,
                            data = personaje,
                            hasError = personaje == null
                        )
                    }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _state.value = CharacterDetailState(
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

    fun cambiarFavorito() {

        val personaje = _state.value.data ?: return

        viewModelScope.launch {

            repository.updateFavorite(
                id = personaje.id,
                isFavorite = !personaje.isFavorite
            )
        }
    }
}
