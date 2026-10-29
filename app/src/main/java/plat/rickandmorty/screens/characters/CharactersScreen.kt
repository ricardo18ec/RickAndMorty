package plat.rickandmorty.screens.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import plat.rickandmorty.R
import plat.rickandmorty.data.Character
import plat.rickandmorty.screens.components.ErrorScreen
import plat.rickandmorty.screens.components.LoadingScreen
import plat.rickandmorty.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersViewModel = viewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var soloFavoritos by rememberSaveable {
        mutableStateOf(false)
    }

    val personajesVisibles = if (soloFavoritos) {
        state.data.filter { it.isFavorite }
    } else {
        state.data
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Characters")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        soloFavoritos = !soloFavoritos
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = soloFavoritos,
                    onCheckedChange = {
                        soloFavoritos = it
                    }
                )

                Text("Mostrar solo favoritos")
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                when {

                    state.hasError -> {

                        ErrorScreen(
                            mensaje = "Error al obtener personajes.\nIntenta de nuevo",
                            onRetry = {
                                viewModel.cargarPersonajes()
                            }
                        )
                    }

                    state.isLoading -> {

                        LoadingScreen(
                            onLoadingClick = {
                                viewModel.mostrarError()
                            }
                        )
                    }

                    personajesVisibles.isEmpty() -> {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = if (soloFavoritos) {
                                    "No hay personajes favoritos para mostrar"
                                } else {
                                    "No hay elementos para mostrar"
                                }
                            )
                        }
                    }

                    else -> {

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            items(
                                items = personajesVisibles,
                                key = { it.id }
                            ) { character ->

                                CharacterItem(
                                    character = character,
                                    onClick = {
                                        onCharacterClick(character.id)
                                    },
                                    onFavoriteClick = {
                                        viewModel.cambiarFavorito(character)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {

                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(character.species)
                Text(character.status)
                Text(character.gender)
            }

            IconButton(
                onClick = onFavoriteClick
            ) {

                IconButton(
                    onClick = onFavoriteClick
                ) {
                    Text(
                        text = if (character.isFavorite) "★" else "☆",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}