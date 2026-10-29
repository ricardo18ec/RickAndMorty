package plat.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import plat.rickandmorty.data.local.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    // Obtener todas las ubicaciones
    @Query("SELECT * FROM locations ORDER BY id ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    // Obtener una ubicación
    @Query("SELECT * FROM locations WHERE id = :id")
    fun getLocationById(id: Int): Flow<LocationEntity?>

    // Insertar varias ubicaciones
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(
        locations: List<LocationEntity>
    )

    // Contar ubicaciones guardadas
    @Query("SELECT COUNT(*) FROM locations")
    suspend fun countLocations(): Int

    // Eliminar todas las ubicaciones
    @Query("DELETE FROM locations")
    suspend fun deleteAllLocations()
}