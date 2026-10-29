package plat.rickandmorty.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.rickandmorty.data.SessionRepository
import plat.rickandmorty.data.local.AppDatabase
import plat.rickandmorty.data.repository.SyncRepository

data class SessionState(
    val isLoading: Boolean = true,
    val nombre: String? = null,
    val isSyncing: Boolean = false,
    val error: String? = null
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sessionRepository =
        SessionRepository(application)

    private val database =
        AppDatabase.getDatabase(application)

    private val syncRepository =
        SyncRepository(database)

    private val _state = MutableStateFlow(SessionState())

    val state = _state.asStateFlow()

    init {
        observarSesion()
    }

    private fun observarSesion() {

        viewModelScope.launch {

            sessionRepository.nombreUsuario.collect { nombre ->

                _state.value = _state.value.copy(
                    isLoading = false,
                    nombre = nombre
                )
            }
        }
    }

    fun iniciarSesion(nombre: String) {

        val nombreLimpio = nombre.trim()

        if (nombreLimpio.isBlank()) return
        if (_state.value.isSyncing) return

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isSyncing = true,
                error = null
            )

            try {

                syncRepository.sincronizarDatos()
                sessionRepository.guardarNombre(nombreLimpio)
                sessionRepository.nombreUsuario.first {
                    it == nombreLimpio
                }

                _state.value = _state.value.copy(
                    isSyncing = false
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _state.value = _state.value.copy(
                    isSyncing = false,
                    error = "Error al iniciar sesión. Intenta de nuevo."
                )
            }
        }
    }

    fun cerrarSesion() {

        if (_state.value.isSyncing) return

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isSyncing = true,
                error = null
            )

            try {

                syncRepository.borrarDatos()
                sessionRepository.eliminarNombre()
                sessionRepository.nombreUsuario.first {
                    it == null
                }

                _state.value = _state.value.copy(
                    isSyncing = false
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _state.value = _state.value.copy(
                    isSyncing = false,
                    error = "Error al cerrar sesión."
                )
            }
        }
    }
}
