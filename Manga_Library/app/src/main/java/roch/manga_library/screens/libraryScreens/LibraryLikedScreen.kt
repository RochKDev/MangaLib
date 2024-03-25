package roch.manga_library.screens.libraryScreens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import roch.manga_library.model.MyLibraryViewModel
import roch.manga_library.screens.MangaList

/**
 * The screen that represents the user's liked mangas.
 * @param modifier The modifier of the composable.
 * @param navController The navController of the application.
 * @param viewModel The viewModel to use in this screen.
 */
@Composable
fun LibraryLikedScreen(
    modifier: Modifier = Modifier,
    navController : NavHostController,
    viewModel: MyLibraryViewModel
) {
    LaunchedEffect(key1 = Unit){
        viewModel.getLikedMangas()
    }
    when(viewModel.requestLikesState){
        is MyLibraryViewModel.RequestUserLikedMangas.Loading -> CircularProgressIndicator()
        is MyLibraryViewModel.RequestUserLikedMangas.Success -> MangaList(
            mangas = (viewModel.requestLikesState as MyLibraryViewModel.RequestUserLikedMangas.Success).result,
            navController= navController
        )
        is MyLibraryViewModel.RequestUserLikedMangas.Error -> Text(text = "Error")
    }
}