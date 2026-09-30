package plat.rickandmorty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
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


        composable<LocationDetailDestination> { backStackEntry ->

            val destination =
                backStackEntry.toRoute<LocationDetailDestination>()

            LocationDetailScreen(
                locationId = destination.id,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
