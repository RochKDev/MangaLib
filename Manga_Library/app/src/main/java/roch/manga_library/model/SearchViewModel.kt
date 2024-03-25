package roch.manga_library.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import roch.manga_library.data.dto.Data
import roch.manga_library.data.dto.Mangas
import roch.manga_library.network.MangaApi
import java.util.LinkedList
import java.util.Queue

sealed interface  RequestSearchState{
    object Loading :RequestSearchState
    object Error : RequestSearchState
    object NoResults : RequestSearchState
    object Idle : RequestSearchState
    data class Success(val result: Mangas): RequestSearchState
}

/**
 * The viewModel of the Search screen.
 */
class SearchViewModel : ViewModel(){


    var requestSearchState: RequestSearchState by mutableStateOf(RequestSearchState.Idle)
        private set

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isActive = MutableStateFlow(false)
    val isActive = _isActive.asStateFlow()

    private val _history = MutableStateFlow<Queue<Data>>(LinkedList())
    val history = _history.asStateFlow()

    /**
     * Changes the searchText to [text]
     * @param text the text to set.
     */
    fun onSearchTextChange(text : String){
        _searchText.value = text
    }

    /**
     * Changes the state of [isActive] to te given state.
     * @param active The value to set [isActive] to.
     */
    fun onActiveChange(active : Boolean){
        _isActive.value = active
    }

    /**
     * Gets a list of mangas corresponding the given [name]
     * @param name the name of the manga to search.
     */
    fun getSearchMangas(name : String) {
        var listResult: Mangas
        requestSearchState = RequestSearchState.Loading
        viewModelScope.launch {
            try {
                listResult = MangaApi.retrofitService.getSearchManga(name)
                requestSearchState = RequestSearchState.Success(
                    result = listResult
                )
            } catch (e: HttpException) {
                requestSearchState = RequestSearchState.Error
            } catch (e: Throwable) {
                e.printStackTrace()
                requestSearchState = RequestSearchState.Error
            }
        }
    }

    /**
     * Adds to the history the [manga] researched.
     * @param manga the manga to add to the history.
     */
    fun addToHistory(manga : Data){
        if(_history.value.size == 20){
            _history.value.remove()
        }else{
            _history.value.offer(manga)
        }
        requestSearchState = RequestSearchState.Idle

    }

}