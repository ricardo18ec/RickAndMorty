package plat.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import plat.rickandmorty.data.local.entities.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    // Obtener todos los personajes
    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    // Obtener un personaje
    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: Int): Flow<CharacterEntity?>

    // Insertar varios personajes
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacters(
        characters: List<CharacterEntity>
    )

    // Actualizar el estado de favorito
    @Query(
        "UPDATE characters SET isFavorite = :isFavorite WHERE id = :id"
    )
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )

    // Obtener solo los favoritos
    @Query(
        "SELECT * FROM characters WHERE isFavorite = 1 ORDER BY id ASC"
    )
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>

    // Contar personajes guardados
    @Query("SELECT COUNT(*) FROM characters")
    suspend fun countCharacters(): Int

    // Eliminar todos los personajes
    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()
}