package plat.rickandmorty.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.rickandmorty.data.Character
import plat.rickandmorty.data.CharacterDb

data class CharactersState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel : ViewModel() {

    private val _state = MutableStateFlow(CharactersState())

    val state = _state.asStateFlow()

    private var loadingJob: kotlinx.coroutines.Job? = null

    init {
        cargarPersonajes()
    }

    fun cargarPersonajes() {

        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {

            _state.value = CharactersState(
                isLoading = true
            )

            delay(4000)

            try {
                val personajes = CharacterDb().getAllCharacters()

                _state.value = CharactersState(
                    isLoading = false,
                    data = personajes
                )

            } catch (e: Exception) {

                _state.value = CharactersState(
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