package roch.manga_library.navGraphs

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import roch.manga_library.data.Screen
import roch.manga_library.model.MyLibraryViewModel
import roch.manga_library.model.NewReleasesMangaView
import roch.manga_library.model.SearchViewModel
import roch.manga_library.model.SignInViewModel
import roch.manga_library.model.TopMangaViewModel
import roch.manga_library.screens.Account
import roch.manga_library.screens.MangaDetailsScreen
import roch.manga_library.screens.NewReleasesScreen
import roch.manga_library.screens.Search
import roch.manga_library.screens.SignInScreen
import roch.manga_library.screens.libraryScreens.MyLibrary
import roch.manga_library.screens.topScreens.TopMangaScreen
import roch.manga_library.sign_in.GoogleAuthUiClient

/**
 * The root navigation graph of the application.
 */
@Composable
fun RootNavGraph(
    navController: NavHostController,
    lifeCycle: LifecycleCoroutineScope,
    googleAuthClient: GoogleAuthUiClient,
    applicationContext: Context,
    padding: PaddingValues
) {
    val searchViewModel: SearchViewModel = viewModel()
    val topViewModel: TopMangaViewModel = viewModel()
    val newViewModel: NewReleasesMangaView = viewModel()
    val libraryViewModel : MyLibraryViewModel = viewModel()
    val viewModel : SignInViewModel = viewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.NewReleases.name
    )
    NavHost(
        navController = navController,
        startDestination = Screen.NewReleases.name,
        modifier = Modifier.padding(padding)
    ) {

        composable(route = Screen.Top.name) {
            TopMangaScreen(navController = navController, mangaViewModel = topViewModel)
        }
        composable(route = Screen.Search.name) {

            Search(navController = navController, searchViewModel = searchViewModel)
        }
        composable(route = Screen.NewReleases.name) {
            NewReleasesScreen(navController = navController, mangaViewModel = newViewModel)
        }
        composable(route = Screen.Account.name) {
            if (googleAuthClient.getSignedInUser() != null && currentScreen == Screen.Account) {
                Account(
                    userData = googleAuthClient.getSignedInUser(),
                    onSignOut = {
                        lifeCycle.launch {
                            googleAuthClient.signOut()
                            Toast.makeText(
                                applicationContext,
                                "Signed out",
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.resetState()
                            navController.navigate(Screen.NewReleases.name)
                        }
                    }
                )
            } else
                if (googleAuthClient.getSignedInUser() == null && currentScreen == Screen.Account) {
                    navController.navigate(Screen.SignIn.name)
                }
        }
        composable(route = Screen.MyLibrary.name) {
            if (googleAuthClient.getSignedInUser() != null && currentScreen == Screen.MyLibrary) {
                MyLibrary(
                    viewModel = libraryViewModel,
                    navController = navController
                )
            } else if (googleAuthClient.getSignedInUser() == null && currentScreen == Screen.MyLibrary) {
                navController.navigate(Screen.SignIn.name)
            }
        }
        composable(route = Screen.MangaDetails.name) {
            MangaDetailsScreen()
        }
        composable(route = Screen.SignIn.name) {

            val state by viewModel.state.collectAsStateWithLifecycle()
            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        lifeCycle.launch {
                            val signInResult = googleAuthClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            SignInScreen(
                state = state,
                onSignInClick = {
                    lifeCycle.launch {
                        val signInIntentSender = googleAuthClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
    }
}