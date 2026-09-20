package plat.rickandmorty.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import plat.rickandmorty.R

@Composable
fun LoginScreen(
    onEmpezarClick: () -> Unit
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

        Image(
            painter = painterResource(
                R.drawable.rick_and_morty_logo
            ),
            contentDescription = "Logo de Rick and Morty",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.weight(0.5f)
        )

        Button(
            onClick = onEmpezarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Empezar")
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "Ricardo Escobar - 25891"
        )
    }
}