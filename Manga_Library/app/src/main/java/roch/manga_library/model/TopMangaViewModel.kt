package roch.manga_library.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import roch.manga_library.data.dto.Mangas
import roch.manga_library.network.MangaApi

sealed interface RequestTopMangaState {
    object Loading : RequestTopMangaState
    object Error : RequestTopMangaState
    data class Success(val result: Mangas) : RequestTopMangaState
}
sealed interface RequestTopFollowedMangaState {
    object Loading : RequestTopFollowedMangaState
    object Error : RequestTopFollowedMangaState
    data class Success(val result: Mangas) : RequestTopFollowedMangaState
}

/**
 * The viewModel of the TopManga screen.
 */
class TopMangaViewModel : ViewModel() {
    var requestRatedState: RequestTopMangaState by mutableStateOf(RequestTopMangaState.Loading)
        private set
    var requestFollowedState: RequestTopFollowedMangaState by mutableStateOf(RequestTopFollowedMangaState.Loading)
        private set

    /**
     * Gets the top rated mangas.
     */
    fun getTopRatedManga() {
        var listResult: Mangas
        requestRatedState = RequestTopMangaState.Loading
        viewModelScope.launch {
            try {
                val options : HashMap<String, String> = HashMap()
                options["order[rating]"] = "desc"
                options["includes[]"] = "cover_art"
                options["limit"] = "50"

                listResult = MangaApi.retrofitService.getTopRatedManga(options)
                requestRatedState = RequestTopMangaState.Success(
                    result = listResult
                )
            } catch (e: HttpException) {
                requestRatedState = RequestTopMangaState.Error
            } catch (e: Throwable) {
                e.printStackTrace()
                requestRatedState = RequestTopMangaState.Error
            }
        }
    }

    /**
     * Gets the most followed mangas.
     */
    fun getMostFollowedManga() {
        var listResult: Mangas
        requestFollowedState = RequestTopFollowedMangaState.Loading
        viewModelScope.launch {
            try {
                listResult = MangaApi.retrofitService.getMostFollowedManga()
                requestFollowedState = RequestTopFollowedMangaState.Success(
                    result = listResult
                )
            } catch (e: HttpException) {
                requestFollowedState = RequestTopFollowedMangaState.Error
            } catch (e: Throwable) {
                e.printStackTrace()
                requestFollowedState = RequestTopFollowedMangaState.Error
            }
        }
    }
}
