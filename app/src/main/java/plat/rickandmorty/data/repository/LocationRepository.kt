package plat.rickandmorty.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.rickandmorty.data.Location
import plat.rickandmorty.data.local.dao.LocationDao
import plat.rickandmorty.data.local.mappers.toModel

class LocationRepository(
    private val locationDao: LocationDao
) {

    fun getAllLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getLocationById(id: Int): Flow<Location?> {
        return locationDao.getLocationById(id).map { entity ->
            entity?.toModel()
        }
    }
}