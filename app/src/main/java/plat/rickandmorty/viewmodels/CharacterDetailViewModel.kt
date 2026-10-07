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
import plat.rickandmorty.data.Character
import plat.rickandmorty.data.CharacterDb
import plat.rickandmorty.navigation.CharacterDetailDestination

data class CharacterDetailState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId =
        savedStateHandle.toRoute<CharacterDetailDestination>().id

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
                val personaje = CharacterDb()
                    .getCharacterById(characterId)

                _state.value = CharacterDetailState(
                    isLoading = false,
                    data = personaje,
                    hasError = false
                )

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
}