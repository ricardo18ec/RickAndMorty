package plat.rickandmorty.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.rickandmorty.data.SessionRepository

data class SessionState(
    val isLoading: Boolean = true,
    val nombre: String? = null
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SessionRepository(application)

    private val _state = MutableStateFlow(SessionState())

    val state = _state.asStateFlow()

    init {
        observarSesion()
    }

    private fun observarSesion() {

        viewModelScope.launch {

            repository.nombreUsuario.collect { nombre ->

                _state.value = SessionState(
                    isLoading = false,
                    nombre = nombre
                )
            }
        }
    }

    fun iniciarSesion(nombre: String) {

        viewModelScope.launch {
            repository.guardarNombre(nombre.trim())
        }
    }

    fun cerrarSesion() {

        viewModelScope.launch {
            repository.eliminarNombre()
        }
    }
}