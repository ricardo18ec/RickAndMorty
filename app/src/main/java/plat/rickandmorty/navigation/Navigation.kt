package plat.rickandmorty.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import plat.rickandmorty.R
import plat.rickandmorty.screens.login.LoginScreen
import plat.rickandmorty.screens.profile.ProfileScreen
import plat.rickandmorty.viewmodels.SessionViewModel

@Serializable
object SessionCheckDestination

@Serializable
object LoginDestination

@Serializable
object MainDestination

@Serializable
object ProfileDestination


@Composable
fun Navigation(
    sessionViewModel: SessionViewModel = viewModel()
) {

    val navController = rememberNavController()

    val sessionState by sessionViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = SessionCheckDestination
    ) {

        composable<SessionCheckDestination> {

            LaunchedEffect(sessionState.isLoading, sessionState.nombre) {

                if (!sessionState.isLoading) {

                    val destino = if (sessionState.nombre.isNullOrBlank()) {
                        LoginDestination
                    } else {
                        MainDestination
                    }

                    if (destino == LoginDestination) {

                        navController.navigate(LoginDestination) {
                            popUpTo<SessionCheckDestination> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }

                    } else {

                        navController.navigate(MainDestination) {
                            popUpTo<SessionCheckDestination> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }


        // LOGIN
        composable<LoginDestination> {

            LaunchedEffect(sessionState.nombre) {

                if (!sessionState.isLoading &&
                    !sessionState.nombre.isNullOrBlank()
                ) {

                    navController.navigate(MainDestination) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            LoginScreen(
                onEmpezarClick = { nombre ->
                    if (nombre.isNotBlank()) {
                        sessionViewModel.iniciarSesion(nombre)
                    }
                }
            )
        }


        // APP PRINCIPAL
        composable<MainDestination> {

            MainScreen(
                nombreUsuario = sessionState.nombre.orEmpty(),
                onLogoutClick = {
                    sessionViewModel.cerrarSesion()
                }
            )

            LaunchedEffect(sessionState.nombre, sessionState.isLoading) {

                if (!sessionState.isLoading &&
                    sessionState.nombre.isNullOrBlank()
                ) {

                    navController.navigate(LoginDestination) {
                        popUpTo<MainDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}


@Composable
fun MainScreen(
    nombreUsuario: String,
    onLogoutClick: () -> Unit
) {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {

            NavigationBar {

                // CHARACTERS
                NavigationBarItem(
                    selected =
                        currentDestination?.hasRoute<CharactersDestination>() == true ||
                                currentDestination?.hasRoute<CharacterDetailDestination>() == true,

                    onClick = {
                        navController.navigate(CharactersGraph) {
                            popUpTo(CharactersGraph) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_characters),
                            contentDescription = "Characters"
                        )
                    },

                    label = {
                        Text("Characters")
                    }
                )


                // LOCATIONS
                NavigationBarItem(
                    selected =
                        currentDestination?.hasRoute<LocationsDestination>() == true ||
                                currentDestination?.hasRoute<LocationDetailDestination>() == true,

                    onClick = {
                        navController.navigate(LocationsGraph) {
                            popUpTo(CharactersGraph) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_locations),
                            contentDescription = "Locations"
                        )
                    },

                    label = {
                        Text("Locations")
                    }
                )


                // PROFILE
                NavigationBarItem(
                    selected =
                        currentDestination?.hasRoute<ProfileDestination>() == true,

                    onClick = {
                        navController.navigate(ProfileDestination) {
                            popUpTo(CharactersGraph) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_profile),
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

            charactersGraph(navController)

            locationsGraph(navController)

            composable<ProfileDestination> {

                ProfileScreen(
                    nombreUsuario = nombreUsuario,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}