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
import plat.rickandmorty.data.Character
import plat.rickandmorty.data.local.AppDatabase
import plat.rickandmorty.data.repository.CharacterRepository

data class CharactersState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = CharacterRepository(
        AppDatabase.getDatabase(application).characterDao()
    )

    private val _state = MutableStateFlow(CharactersState())

    val state = _state.asStateFlow()

    private var loadingJob: Job? = null

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
                repository.getAllCharacters().collectLatest { personajes ->

                    _state.value = CharactersState(
                        isLoading = false,
                        data = personajes,
                        hasError = false
                    )
                }

            } catch (e: CancellationException) {
                throw e

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

    fun cambiarFavorito(character: Character) {

        viewModelScope.launch {

            repository.updateFavorite(
                id = character.id,
                isFavorite = !character.isFavorite
            )
        }
    }
}