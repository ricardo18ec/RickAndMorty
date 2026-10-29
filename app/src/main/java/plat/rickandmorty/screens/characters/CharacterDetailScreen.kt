package plat.rickandmorty.screens.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import plat.rickandmorty.screens.components.ErrorScreen
import plat.rickandmorty.screens.components.LoadingScreen
import plat.rickandmorty.viewmodels.CharacterDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CharacterDetailViewModel = viewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Characters details")
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            painter = painterResource(
                                R.drawable.ic_arrow_back
                            ),
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when {

                // ERROR
                state.hasError -> {

                    ErrorScreen(
                        mensaje = "Error al obtener información del personaje.\nIntenta de nuevo",
                        onRetry = {
                            viewModel.cargarPersonaje()
                        }
                    )
                }

                // LOADING
                state.isLoading -> {

                    LoadingScreen(
                        onLoadingClick = {
                            viewModel.mostrarError()
                        }
                    )
                }

                // DATOS
                else -> {

                    state.data?.let { character ->

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            AsyncImage(
                                model = character.image,
                                contentDescription = character.name,
                                modifier = Modifier
                                    .size(250.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = character.name,
                                    style = MaterialTheme.typography.headlineLarge
                                )

                                IconButton(
                                    onClick = {
                                        viewModel.cambiarFavorito()
                                    }
                                ) {

                                    Text(
                                        text = if (character.isFavorite) {
                                            "★"
                                        } else {
                                            "☆"
                                        },
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text = "Especie",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = character.species
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text = "Status",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = character.status
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.padding(top = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "Género",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = character.gender
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
