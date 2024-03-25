package roch.manga_library.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import roch.manga_library.R
import roch.manga_library.data.Screen
import roch.manga_library.data.dto.Data
import roch.manga_library.model.MangaDetailsViewModel
import roch.manga_library.model.RequestSearchState
import roch.manga_library.model.SearchViewModel
/**
 * The screen that represents the search screen.
 * @param navController The navController of the application.
 * @param searchViewModel The viewModel to use in this screen.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    searchViewModel: SearchViewModel,
    navController: NavHostController
) {
    var text = searchViewModel.searchText.collectAsState()
    var active = searchViewModel.isActive.collectAsState()

    Scaffold {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            query = text.value,
            onQueryChange = {
                searchViewModel.onSearchTextChange(it)
            },
            onSearch = {
                searchViewModel.getSearchMangas(text.value)
                searchViewModel.onSearchTextChange("")
            },
            active = active.value,
            onActiveChange = {
                searchViewModel.onActiveChange(it)
            },
            placeholder = {
                Text(text = stringResource(id = R.string.search))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (active.value) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.value.isNotEmpty()) {
                                searchViewModel.onSearchTextChange("")
                            } else {
                                searchViewModel.onActiveChange(false)
                            }
                        },
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon"
                    )
                }
            }
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            when (searchViewModel.requestSearchState) {
                is RequestSearchState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is RequestSearchState.Error -> {
                    Text(text = "Error")
                }
                is RequestSearchState.NoResults -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No results for this input :(",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is RequestSearchState.Success -> {
                    SearchResultsList(
                        mangas = (searchViewModel.requestSearchState as RequestSearchState.Success).result.data,
                        onItemClicked = {
                            searchViewModel.onActiveChange(false)
                            MangaDetailsViewModel.selectedManga(it)
                            navController.navigate(route = Screen.MangaDetails.name)
                            searchViewModel.addToHistory(it)
                        }
                    )
                }

                is RequestSearchState.Idle -> {
                    if (active.value) {
                        var history = searchViewModel.history.collectAsState()
                        SearchResultsList(
                            mangas = history.value.toList(),
                            onItemClicked = {
                                searchViewModel.onActiveChange(false)
                                MangaDetailsViewModel.selectedManga(it)
                                navController.navigate(route = Screen.MangaDetails.name)
                                searchViewModel.addToHistory(it)
                            }
                        )
                    }
                }
            }

        }

    }
}

/**
 * A composable that represents a list of mangas beneath the search bar.
 * @param mangas The mangas to display.
 * @param onItemClicked The action to perform when an item is clicked.
 */
@Composable
private fun SearchResultsList(mangas: List<Data>, onItemClicked: (Data) -> Unit) {
    LazyColumn {
        itemsIndexed(items = mangas) { index, manga ->
            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClicked.invoke(manga) }
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row {
                    Box(modifier = Modifier.width(100.dp)){
                        val coverArt = manga.relationships?.find { it.type == "cover_art" }
                        AsyncImage(
                            model = "https://uploads.mangadex.org/covers/${manga.id}/${coverArt?.attributes?.fileName}",
                            contentDescription = MangaDetailsViewModel.title
                                ?: "Image of an manga",
                            placeholder = painterResource(id = R.drawable.loading),
                            error = painterResource(id = R.drawable.error),
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                        )
                    }
                    Box{
                        val titleKey = manga.attributes.title.keys.firstOrNull()
                        Text(
                            text = manga.attributes.title[titleKey] ?: "No English or Japanese Title",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}