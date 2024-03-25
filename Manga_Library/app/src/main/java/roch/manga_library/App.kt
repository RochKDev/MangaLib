package roch.manga_library

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import roch.manga_library.data.Screen
import roch.manga_library.navGraphs.RootNavGraph
import roch.manga_library.sign_in.GoogleAuthUiClient

/**
 * The main app composable.
 * @param modifier The modifier of the composable.
 * @param lifeCycle The lifecycle of the application.
 * @param googleAuthClient The google authentication client.
 * @param applicationContext The context of the application.
 * @param navController The navController of the application.
 */
@Composable
fun App(
    modifier: Modifier = Modifier,
    lifeCycle : LifecycleCoroutineScope,
    googleAuthClient : GoogleAuthUiClient,
    applicationContext : Context,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.NewReleases.name
    )
    Scaffold(
        topBar = { TopAppBar(showTopBar = currentScreen != Screen.MangaDetails) },
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        RootNavGraph(
            navController = navController,
            lifeCycle = lifeCycle,
            applicationContext = applicationContext,
            googleAuthClient = googleAuthClient,
            padding = innerPadding
        )
    }
}

/**
 * A composable that represents the bottom bar of the application.
 * @param navController The navController of the application.
 * @param modifier The modifier of the composable.
 */
@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(1)
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.NewReleases.name
    )
    selectedTabIndex = when(currentScreen){
        Screen.Search -> 0
        Screen.NewReleases -> 1
        Screen.MyLibrary -> 2
        Screen.Top -> 3
        Screen.Account -> 4
        else -> selectedTabIndex
    }
    val fontSize = 12.sp

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.height(60.dp)
    ) {
        BottomNavigationItem(
            selected = selectedTabIndex == 0,
            onClick = {
                selectedTabIndex = 0
                navController.navigate(route = Screen.Search.name)
            },
            icon = {
                if (selectedTabIndex == 0) {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.search),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.search),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

            },
            label = {
                if (selectedTabIndex == 0) {
                    Text(
                        stringResource(R.string.search),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        fontSize = fontSize,
                    )
                } else {
                    Text(
                        stringResource(R.string.search),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        fontSize = fontSize,
                    )
                }
            })
        BottomNavigationItem(
            selected = selectedTabIndex == 1,
            onClick = {
                selectedTabIndex = 1
                navController.navigate(route = Screen.NewReleases.name)
            },
            icon = {
                if (selectedTabIndex == 1) {
                    Icon(
                        painter = painterResource(R.drawable.hourglass),
                        contentDescription = stringResource(R.string.new_releases_screen),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.hourglass),
                        contentDescription = stringResource(R.string.new_releases_screen),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

            },
            label = {
                if (selectedTabIndex == 1) {
                    Text(
                        stringResource(R.string.new_releases_screen),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        fontSize = fontSize,
                    )
                } else {
                    Text(
                        stringResource(R.string.new_releases_screen),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        fontSize = fontSize,
                    )
                }
            })
        BottomNavigationItem(
            selected = selectedTabIndex == 2,
            onClick = {
                selectedTabIndex = 2
                navController.navigate(route = Screen.MyLibrary.name)
            },
            icon = {
                if (selectedTabIndex == 2) {
                    Icon(
                        painterResource(R.drawable.book_stack),
                        contentDescription = stringResource(R.string.library),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.book_stack),
                        contentDescription = stringResource(R.string.library),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            label = {
                if (selectedTabIndex == 2) {
                    Text(
                        stringResource(R.string.library),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        fontSize = fontSize
                    )
                } else {
                    Text(
                        stringResource(R.string.library),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        fontSize = fontSize
                    )
                }
            })
        BottomNavigationItem(
            selected = selectedTabIndex == 3,
            onClick = {
                selectedTabIndex = 3
                navController.navigate(route = Screen.Top.name)
            },
            icon = {
                if (selectedTabIndex == 3) {
                    Icon(
                        painterResource(R.drawable.fire_flame),
                        contentDescription = stringResource(R.string.top),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.fire_flame),
                        contentDescription = stringResource(R.string.top),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            label = {
                if (selectedTabIndex == 3) {
                    Text(
                        stringResource(R.string.top),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        fontSize = fontSize
                    )
                } else {
                    Text(
                        stringResource(R.string.top),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        fontSize = fontSize
                    )
                }
            })
        BottomNavigationItem(
            selected = selectedTabIndex == 4,
            onClick = {
                selectedTabIndex = 4
                navController.navigate(route = Screen.Account.name)
            },
            icon = {
                if (selectedTabIndex == 4) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(R.string.account),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(R.string.account),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

            },
            label = {
                if (selectedTabIndex == 4) {
                    Text(
                        stringResource(R.string.account),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        fontSize = fontSize
                    )
                } else {
                    Text(
                        stringResource(R.string.account),
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        fontSize = fontSize
                    )
                }
            })
    }
}
/**
 * A composable that represents the top app bar of the application.
 * @param modifier The modifier of the composable.
 * @param showTopBar Shows the top app bar if true, doesn't show it otherwise.
 */
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    showTopBar: Boolean = true,
) {
    if (showTopBar) {
        Column {
            androidx.compose.material.TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.background,
                title = {
                    Text(
                        text = stringResource(id = R.string.app_title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp
                    )
                },
                actions = actions,
                elevation = AppBarDefaults.TopAppBarElevation,
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
