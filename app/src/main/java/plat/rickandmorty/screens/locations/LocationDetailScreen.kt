package plat.rickandmorty.screens.locations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.rickandmorty.R
import plat.rickandmorty.screens.components.ErrorScreen
import plat.rickandmorty.screens.components.LoadingScreen
import plat.rickandmorty.viewmodels.LocationDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    onBackClick: () -> Unit,
    viewModel: LocationDetailViewModel = viewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Location details")
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
                        mensaje = "Error al obtener información de la ubicación.\nIntenta de nuevo",
                        onRetry = {
                            viewModel.cargarLocation()
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

                    state.data?.let { location ->

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) {

                            Text(
                                text = location.name,
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(
                                    vertical = 24.dp
                                )
                            )

                            LocationInfo(
                                titulo = "ID:",
                                valor = location.id.toString()
                            )

                            LocationInfo(
                                titulo = "Type:",
                                valor = location.type
                            )

                            LocationInfo(
                                titulo = "Dimension:",
                                valor = location.dimension
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LocationInfo(
    titulo: String,
    valor: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {

        Text(
            text = titulo,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = valor,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}