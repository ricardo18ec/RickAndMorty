package plat.rickandmorty.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.rickandmorty.data.Character
import plat.rickandmorty.data.local.dao.CharacterDao
import plat.rickandmorty.data.local.mappers.toModel

class CharacterRepository(
    private val characterDao: CharacterDao
) {

    fun getAllCharacters(): Flow<List<Character>> {
        return characterDao.getAllCharacters().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getCharacterById(id: Int): Flow<Character?> {
        return characterDao.getCharacterById(id).map { entity ->
            entity?.toModel()
        }
    }

    fun getFavoriteCharacters(): Flow<List<Character>> {
        return characterDao.getFavoriteCharacters().map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        characterDao.updateFavorite(id, isFavorite)
    }
}