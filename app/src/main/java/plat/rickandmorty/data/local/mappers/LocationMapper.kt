package plat.rickandmorty.data.local.mappers

import plat.rickandmorty.data.Location
import plat.rickandmorty.data.local.entities.LocationEntity

fun Location.toEntity(): LocationEntity {

    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}

fun LocationEntity.toModel(): Location {

    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}