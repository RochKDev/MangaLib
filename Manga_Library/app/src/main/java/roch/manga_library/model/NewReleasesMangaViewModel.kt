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

sealed interface  RequestNewMangaState{
    object Loading : RequestNewMangaState
    object Error : RequestNewMangaState
    data class Success(val result: Mangas): RequestNewMangaState
}

/**
 * The viewModel of the NewReleases screen.
 */
class NewReleasesMangaView : ViewModel(){
    var requestState : RequestNewMangaState by mutableStateOf(RequestNewMangaState.Loading)
        private set

    /**
     * Gets the recently released mangas.
     */
    fun getRecentlyReleasedManga(){
        var listResult : Mangas
        requestState = RequestNewMangaState.Loading
        viewModelScope.launch {
            try {
                listResult = MangaApi.retrofitService.getRecentlyReleasedChapters()
                requestState = RequestNewMangaState.Success(
                    result = listResult
                )
            } catch (e: HttpException) {
                requestState = RequestNewMangaState.Error
            } catch (e: Throwable) {
                e.printStackTrace()
                requestState = RequestNewMangaState.Error
            }
        }
    }
}
