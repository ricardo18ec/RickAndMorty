package plat.rickandmorty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import plat.rickandmorty.screens.characters.CharacterDetailScreen
import plat.rickandmorty.screens.characters.CharactersScreen

@Serializable
object CharactersGraph

@Serializable
object CharactersDestination

@Serializable
data class CharacterDetailDestination(
    val id: Int
)

fun NavGraphBuilder.charactersGraph(
    navController: NavHostController
) {

    navigation<CharactersGraph>(
        startDestination = CharactersDestination
    ) {

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

        composable<CharacterDetailDestination> {

            CharacterDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}