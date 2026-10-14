package plat.rickandmorty.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import plat.rickandmorty.R

@Composable
fun ProfileScreen(
    nombreUsuario: String,
    onLogoutClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(R.drawable.ic_profile),
            contentDescription = "Imagen de perfil",
            modifier = Modifier.size(150.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.weight(0.3f)
        )

        Text(
            text = "Nombre: $nombreUsuario",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Carné: 25891",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onLogoutClick
        ) {
            Text("Cerrar sesión")
        }
    }
}