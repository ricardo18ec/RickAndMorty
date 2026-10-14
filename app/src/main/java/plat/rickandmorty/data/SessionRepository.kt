package plat.rickandmorty.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(
    name = "session_preferences"
)

class SessionRepository(
    private val context: Context
) {

    private val NAME_KEY = stringPreferencesKey("name")

    val nombreUsuario: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[NAME_KEY]
        }

    suspend fun guardarNombre(nombre: String) {

        context.dataStore.edit { preferences ->
            preferences[NAME_KEY] = nombre
        }
    }

    suspend fun eliminarNombre() {

        context.dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
        }
    }
}