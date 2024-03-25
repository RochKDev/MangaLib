package roch.manga_library.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import roch.manga_library.R
import roch.manga_library.model.MangaDetailsViewModel

/**
 * The screen that represents the manga details.
 * @param modifier The modifier of the composable.
 */
@Composable
fun MangaDetailsScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    MangaDetailsViewModel.checkIfMangaInLikes()
    MangaDetailsViewModel.checkIfMangaInLibrary()
    val isLiked by MangaDetailsViewModel.isLiked.collectAsState()
    val isLibrary by MangaDetailsViewModel.isLibrary.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {

            AsyncImage(
                model = MangaDetailsViewModel.imageUrl,
                contentDescription = MangaDetailsViewModel.title
                    ?: "Image of an manga",
                placeholder = painterResource(id = R.drawable.loading),
                error = painterResource(id = R.drawable.error),
                modifier = Modifier
                    .fillMaxSize()
                    .blur(5.dp)
                    .scale(
                        scaleX = 2f,
                        scaleY = 2f
                    )
            )
            AsyncImage(
                model = MangaDetailsViewModel.imageUrl,
                contentDescription = MangaDetailsViewModel.title
                    ?: "Image of an manga",
                placeholder = painterResource(id = R.drawable.loading),
                error = painterResource(id = R.drawable.error),
                modifier = Modifier
                    .background(Color.Transparent),
                alignment = Alignment.Center,
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        Text(
            text = MangaDetailsViewModel.title ?: "No English or Japanese title",
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            if (isLibrary.not()) {
                Button(
                    onClick = {
                        MangaDetailsViewModel.addToLibraryManga()
                        MangaDetailsViewModel.checkIfMangaInLibrary()
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = "Add to library")
                }
            }else{
                Button(
                    onClick = {
                        MangaDetailsViewModel.removeMangaFromLibrary()
                        MangaDetailsViewModel.checkIfMangaInLibrary()
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                    Text(text = "In your library")
                }
            }

            if (isLiked.not()) {
                Button(
                    onClick = {
                        MangaDetailsViewModel.addLikesManga()
                        MangaDetailsViewModel.checkIfMangaInLikes()
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = "Add to favorites")
                }
            } else {
                Button(
                    onClick = {
                        MangaDetailsViewModel.removeMangaFromLikes()
                        MangaDetailsViewModel.checkIfMangaInLikes()
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                    Text(text = "In your favorites")
                }
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            Text(
                text = "${stringResource(id = R.string.description_tag)} :",
                color = MaterialTheme.colorScheme.inversePrimary,
                textAlign = TextAlign.Start
            )
        }

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = if (MangaDetailsViewModel.description.isNullOrEmpty()) {
                "No English description"
            } else {
                "   " + MangaDetailsViewModel.description
            },
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(start = 16.dp, end = 16.dp)
        )
    }
}
