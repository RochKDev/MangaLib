package roch.manga_library.screens.topScreens

import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import roch.manga_library.model.RequestTopFollowedMangaState
import roch.manga_library.model.TopMangaViewModel
import roch.manga_library.screens.MangaList
/**
 * The screen that represents the most followed mangas.
 * @param modifier The modifier of the composable.
 * @param navController The navController of the application.
 * @param mangaViewModel The viewModel to use in this screen.
 */
@Composable
fun TopFollowedScreen(
    mangaViewModel: TopMangaViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = Unit) {
        mangaViewModel.getMostFollowedManga()
    }

    when (mangaViewModel.requestFollowedState) {
        is RequestTopFollowedMangaState.Loading -> CircularProgressIndicator()
        is RequestTopFollowedMangaState.Success -> MangaList(
            mangas = (mangaViewModel.requestFollowedState as RequestTopFollowedMangaState.Success).result,
            navController = navController
        )

        is RequestTopFollowedMangaState.Error -> Text(text = "Error")
    }
}
