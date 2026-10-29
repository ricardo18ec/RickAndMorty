package plat.rickandmorty.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.delay
import plat.rickandmorty.data.CharacterDb
import plat.rickandmorty.data.LocationDb
import plat.rickandmorty.data.local.AppDatabase
import plat.rickandmorty.data.local.mappers.toEntity

class SyncRepository(
    private val database: AppDatabase
) {

    suspend fun sincronizarDatos() {

        delay(4000)

        database.withTransaction {

            val characterDao = database.characterDao()
            val locationDao = database.locationDao()

            if (characterDao.countCharacters() == 0) {
                val characters = CharacterDb()
                    .getAllCharacters()
                    .map { it.toEntity() }
                characterDao.insertCharacters(characters)
            }

            if (locationDao.countLocations() == 0) {
                val locations = LocationDb()
                    .getAllLocations()
                    .map { it.toEntity() }
                locationDao.insertLocations(locations)
            }
        }
    }

    suspend fun borrarDatos() {

        database.withTransaction {
            database.characterDao().deleteAllCharacters()
            database.locationDao().deleteAllLocations()
        }
    }
}
