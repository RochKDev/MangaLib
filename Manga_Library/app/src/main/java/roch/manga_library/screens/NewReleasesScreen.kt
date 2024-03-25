package roch.manga_library.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import roch.manga_library.model.NewReleasesMangaView
import roch.manga_library.model.RequestNewMangaState
/**
 * The screen that represents the new releases screen.
 * @param modifier The modifier of the composable.
 * @param navController The navController of the application.
 * @param mangaViewModel The viewModel to use in this screen.
 */
@Composable
fun NewReleasesScreen(
    mangaViewModel : NewReleasesMangaView,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    LaunchedEffect(key1 = Unit){
        mangaViewModel.getRecentlyReleasedManga()
    }
    when(mangaViewModel.requestState){
        is RequestNewMangaState.Loading -> CircularProgressIndicator()
        is RequestNewMangaState.Success -> MangaList(
            mangas = (mangaViewModel.requestState as RequestNewMangaState.Success).result,
            navController= navController
        )
        is RequestNewMangaState.Error -> Text(text = "Error")
    }
}

