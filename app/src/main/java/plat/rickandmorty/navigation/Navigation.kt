package plat.rickandmorty.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import plat.rickandmorty.R
import plat.rickandmorty.screens.login.LoginScreen
import plat.rickandmorty.screens.profile.ProfileScreen
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination.Companion.hasRoute

@Serializable
object LoginDestination


@Serializable
object MainDestination


@Serializable
object ProfileDestination


@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {

        // LOGIN
        composable<LoginDestination> {

            LoginScreen(
                onEmpezarClick = {

                    navController.navigate(
                        MainDestination
                    ) {

                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }


        // APP PRINCIPAL
        composable<MainDestination> {

            MainScreen(
                onLogoutClick = {

                    navController.navigate(
                        LoginDestination
                    ) {

                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    onLogoutClick: () -> Unit
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected =
                        currentDestination?.hasRoute<CharactersGraph>() == true ||
                        currentDestination?.hasRoute<CharactersDestination>() == true ||
                        currentDestination?.hasRoute<CharacterDetailDestination>() == true,

                    onClick = {
                        navController.navigate(CharactersGraph) {
                            popUpTo(CharactersGraph) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(
                                R.drawable.ic_characters
                            ),
                            contentDescription = "Characters"
                        )
                    },
                    label = {
                        Text("Characters")
                    }
                )


                NavigationBarItem(
                    selected =
                        currentDestination?.hasRoute<LocationsGraph>() == true ||
                        currentDestination?.hasRoute<LocationsDestination>() == true ||
                        currentDestination?.hasRoute<LocationDetailDestination>() == true,

                    onClick = {
                        navController.navigate(LocationsGraph) {
                            popUpTo(CharactersGraph) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(
                                R.drawable.ic_locations
                            ),
                            contentDescription = "Locations"
                        )
                    },
                    label = {
                        Text("Locations")
                    }
                )


                NavigationBarItem(
                    selected =
                        currentDestination?.hasRoute<ProfileDestination>() == true,

                    onClick = {
                        navController.navigate(ProfileDestination) {
                            popUpTo(CharactersGraph) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(
                                R.drawable.ic_profile
                            ),
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text("Profile")
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(innerPadding)
        ) {

            charactersGraph(
                navController = navController
            )

            locationsGraph(
                navController = navController
            )

            composable<ProfileDestination> {

                ProfileScreen(
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}