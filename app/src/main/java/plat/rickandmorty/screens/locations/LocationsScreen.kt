package plat.rickandmorty.screens.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.rickandmorty.data.Location
import plat.rickandmorty.screens.components.ErrorScreen
import plat.rickandmorty.screens.components.LoadingScreen
import plat.rickandmorty.viewmodels.LocationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationsViewModel = viewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Locations")
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
                        mensaje = "Error al obtener listado de ubicaciones.\nIntenta de nuevo",
                        onRetry = {
                            viewModel.cargarLocations()
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

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = state.data,
                            key = { it.id }
                        ) { location ->

                            LocationItem(
                                location = location,
                                onClick = {
                                    onLocationClick(location.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LocationItem(
    location: Location,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = location.name,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
