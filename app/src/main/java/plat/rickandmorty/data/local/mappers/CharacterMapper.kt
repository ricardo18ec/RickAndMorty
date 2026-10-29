package plat.rickandmorty.data.local.mappers

import plat.rickandmorty.data.Character
import plat.rickandmorty.data.local.entities.CharacterEntity

fun Character.toEntity(): CharacterEntity {

    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = isFavorite
    )
}

fun CharacterEntity.toModel(): Character {

    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = isFavorite
    )
}