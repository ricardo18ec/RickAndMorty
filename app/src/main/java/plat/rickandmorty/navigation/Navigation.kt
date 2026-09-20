package plat.rickandmorty.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.rickandmorty.screens.CharacterDetailScreen
import plat.rickandmorty.screens.CharactersScreen
import plat.rickandmorty.screens.LoginScreen
import kotlinx.serialization.Serializable


@Serializable
object LoginDestination


@Serializable
object CharactersDestination


@Serializable
data class CharacterDetailDestination(
    val id: Int
)

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {

        composable<LoginDestination> {

            LoginScreen(
                onEmpezarClick = {

                    navController.navigate(
                        CharactersDestination
                    ) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }


        composable<CharactersDestination> {

            CharactersScreen(
                onCharacterClick = { characterId ->

                    navController.navigate(
                        CharacterDetailDestination(
                            id = characterId
                        )
                    )
                }
            )
        }


        composable<CharacterDetailDestination> { backStackEntry ->

            val destination =
                backStackEntry.toRoute<CharacterDetailDestination>()

            CharacterDetailScreen(
                characterId = destination.id,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}