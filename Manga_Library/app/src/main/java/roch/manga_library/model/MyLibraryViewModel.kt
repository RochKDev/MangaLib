package roch.manga_library.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import roch.manga_library.data.dto.Mangas
import roch.manga_library.data.repositories.MangaRepository
import roch.manga_library.network.MangaApi

/**
 * The viewModel of the MyLibrary screen.
 */
class MyLibraryViewModel : ViewModel() {

    sealed interface RequestUserLikedMangas {
        object Loading : RequestUserLikedMangas
        object Error : RequestUserLikedMangas
        data class Success(val result: Mangas) : RequestUserLikedMangas
    }

    sealed interface RequestUserLibraryMangas {
        object Loading : RequestUserLibraryMangas
        object Error : RequestUserLibraryMangas
        data class Success(val result: Mangas) : RequestUserLibraryMangas
    }

    var requestLikesState: RequestUserLikedMangas by mutableStateOf(RequestUserLikedMangas.Loading)
        private set
    var requestLibraryState: RequestUserLibraryMangas by mutableStateOf(RequestUserLibraryMangas.Loading)
        private set

    /**
     * Gets all the liked manga from the DB.
     */
    fun getLikedMangas() {
        var listResult: Mangas
        requestLikesState = RequestUserLikedMangas.Loading
        viewModelScope.launch {
            try {
                val ids = withContext(Dispatchers.IO) {
                    MangaRepository.getUserLikes()
                }
                if (ids.size <= 100) {
                    listResult = MangaApi.retrofitService.getMangasByIds(ids)
                    requestLikesState = RequestUserLikedMangas.Success(
                        result = listResult
                    )
                } else {
                    ids.take(100)
                    listResult = MangaApi.retrofitService.getMangasByIds(ids)
                    requestLikesState = RequestUserLikedMangas.Success(
                        result = listResult
                    )
                }
            } catch (e: HttpException) {
                requestLikesState = RequestUserLikedMangas.Error
            } catch (e: Throwable) {
                requestLikesState = RequestUserLikedMangas.Error
            }
        }
    }
    /**
     * Gets all the library manga from the DB.
     */
    fun getLibraryMangas() {
        var listResult: Mangas
        requestLibraryState = RequestUserLibraryMangas.Loading
        viewModelScope.launch {
            try {
                val ids = withContext(Dispatchers.IO) {
                    MangaRepository.getUserLibrary()
                }
                if (ids.size <= 100) {
                    listResult = MangaApi.retrofitService.getMangasByIds(ids)
                    requestLibraryState = RequestUserLibraryMangas.Success(
                        result = listResult
                    )
                } else {
                    ids.take(100)
                    listResult = MangaApi.retrofitService.getMangasByIds(ids)
                    requestLibraryState = RequestUserLibraryMangas.Success(
                        result = listResult
                    )
                }
            } catch (e: HttpException) {
                requestLibraryState = RequestUserLibraryMangas.Error
            } catch (e: Throwable) {
                requestLibraryState = RequestUserLibraryMangas.Error
            }
        }
    }
}