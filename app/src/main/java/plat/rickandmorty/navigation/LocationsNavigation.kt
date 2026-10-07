package plat.rickandmorty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import plat.rickandmorty.screens.locations.LocationDetailScreen
import plat.rickandmorty.screens.locations.LocationsScreen

@Serializable
object LocationsGraph

@Serializable
object LocationsDestination

@Serializable
data class LocationDetailDestination(
    val id: Int
)

fun NavGraphBuilder.locationsGraph(
    navController: NavHostController
) {

    navigation<LocationsGraph>(
        startDestination = LocationsDestination
    ) {

        // LISTADO DE LOCATIONS
        composable<LocationsDestination> {

            LocationsScreen(
                onLocationClick = { locationId ->

                    navController.navigate(
                        LocationDetailDestination(
                            id = locationId
                        )
                    )
                }
            )
        }

        composable<LocationDetailDestination> {

            LocationDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
