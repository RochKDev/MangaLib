package roch.manga_library.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import roch.manga_library.data.Screen
import roch.manga_library.data.dto.Mangas
import roch.manga_library.model.MangaDetailsViewModel

/**
 * A composable that represents a list of mangas.
 * @param mangas the list of mangas to display.
 * @param navController The navController of the application.
 * @param modifier The modifier of the composable.
*/
@Composable
fun MangaList(
    mangas : Mangas,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    Column {
        LazyColumn {

            items(mangas.data){manga ->
                val coverArt = manga.relationships?.find { it.type == "cover_art" }
                val url =
                    "https://uploads.mangadex.org/covers/${manga.id}/${coverArt?.attributes?.fileName}"
                MangaItem(
                    data = manga,
                    imageUrl = url,
                    onItemClick = {
                        MangaDetailsViewModel.selectedManga(manga)
                        navController.navigate(route = Screen.MangaDetails.name)
                    }
                )
            }
        }
    }
}