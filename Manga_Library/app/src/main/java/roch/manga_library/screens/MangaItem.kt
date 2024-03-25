package roch.manga_library.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import roch.manga_library.R
import roch.manga_library.data.dto.Data

/**
 * A composable that represent a certain manga.
 * @param data The data of the manga.
 * @param imageUrl The url of the image to display.
 * @param onItemClick The action to perform when the [MangaItem] is clicked.
 * @param modifier The modifier of the composable.
 */
@Composable
fun MangaItem(
    data: Data,
    imageUrl: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .padding(5.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable(onClick = onItemClick)
            .then(modifier)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
            ){
                MangaIcon(
                    data,
                    imageUrl
                )
            }
            Box {
                MangaInfo(data)
            }
        }
    }
}

/**
 * A composable that represents the icon of the manga.
 * @param data The data of the manga.
 * @param imageUrl The url of the image to display.
 * @param modifier The modifier of the composable.
 */
@Composable
fun MangaIcon(
    data: Data,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val firstKey: String? = data.attributes.title.keys.firstOrNull()
    AsyncImage(
        model = imageUrl,
        contentDescription = data.attributes.title[firstKey]
            ?: "No title found in English or Japanese",
        placeholder = painterResource(id = R.drawable.loading),
        error = painterResource(id = R.drawable.error),
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
    )
}
/**
 * A composable that represents the info of the manga.
 * @param data The data of the manga.
 * @param modifier The modifier of the composable.
 */
@Composable
fun MangaInfo(
    data: Data,
    modifier: Modifier = Modifier
        .padding(dimensionResource(id = R.dimen.padding_small))
) {
    Column(modifier = modifier) {
        val firstKey: String? = data.attributes.title.keys.firstOrNull()
        Text(
            text = data.attributes.title[firstKey]
                ?: "No title found in English or Japanese",
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.padding_small)),
            color = MaterialTheme.colorScheme.inversePrimary
        )
        Text(
            text = "${
                data.attributes.description?.get("en")?.take(80) ?: "No English description"
            }...",
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
            color = MaterialTheme.colorScheme.inversePrimary
        )
    }
}