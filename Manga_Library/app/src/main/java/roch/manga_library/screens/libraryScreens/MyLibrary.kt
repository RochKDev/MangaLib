package roch.manga_library.screens.libraryScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import roch.manga_library.R
import roch.manga_library.model.MyLibraryViewModel

/**
 * The screen that holds the library screen and likes screen.
 * @param modifier The modifier of the composable.
 * @param navController The navController of the application.
 * @param viewModel The viewModel to use in this screen.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyLibrary(
    viewModel: MyLibraryViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val tabItems = listOf(
        LibraryItem(stringResource(id = R.string.library)),
        LibraryItem(stringResource(id = R.string.favorites))
    )

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val fontSize = 12.sp

    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(selectedTabIndex){
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress){
        if (!pagerState.isScrollInProgress){
            selectedTabIndex = pagerState.currentPage
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    onClick = { selectedTabIndex = index  },
                    text = {
                        if (selectedTabIndex == index){
                            Text(
                                text = item.title,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = fontSize
                            )
                        } else{
                            Text(
                                text = item.title,
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = fontSize
                            )
                        }
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {index ->
            when (index) {
                0 -> LibraryScreen(
                    viewModel = viewModel,
                    navController = navController
                )
                1 -> LibraryLikedScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}
/**
 * Data class that represents the title of the TabRow.
 */
data class LibraryItem(
    val title: String
)


