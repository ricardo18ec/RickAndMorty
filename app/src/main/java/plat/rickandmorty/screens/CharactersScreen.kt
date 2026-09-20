package plat.rickandmorty.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import plat.rickandmorty.data.Character
import plat.rickandmorty.data.CharacterDb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit
) {

    val characters = CharacterDb().getAllCharacters()

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Characters")
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(characters) { character ->

                CharacterItem(
                    character = character,
                    onClick = {
                        onCharacterClick(character.id)
                    }
                )
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit
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
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {

                Text(
                    text = character.name
                )

                Text(
                    text = character.species
                )

                Text(
                    text = character.status
                )

                Text(
                    text = character.gender
                )
            }
        }
    }
}