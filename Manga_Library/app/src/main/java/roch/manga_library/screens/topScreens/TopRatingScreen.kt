package roch.manga_library.screens.topScreens

import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import roch.manga_library.model.RequestTopMangaState
import roch.manga_library.model.TopMangaViewModel
import roch.manga_library.screens.MangaList
/**
 * The screen that represents the top rated mangas.
 * @param modifier The modifier of the composable.
 * @param navController The navController of the application.
 * @param mangaViewModel The viewModel to use in this screen.
 */
@Composable
fun TopRatingScreen(
    mangaViewModel: TopMangaViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = Unit) {
        mangaViewModel.getTopRatedManga()
    }

    when (mangaViewModel.requestRatedState) {
        is RequestTopMangaState.Loading -> CircularProgressIndicator()
        is RequestTopMangaState.Success -> MangaList(
            mangas = (mangaViewModel.requestRatedState as RequestTopMangaState.Success).result,
            navController = navController
        )
        is RequestTopMangaState.Error -> Text(text = "Error")
    }
}
