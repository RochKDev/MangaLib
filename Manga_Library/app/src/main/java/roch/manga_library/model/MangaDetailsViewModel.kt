package roch.manga_library.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import roch.manga_library.data.dto.Data
import roch.manga_library.data.dto.RelationshipsData
import roch.manga_library.data.repositories.MangaRepository

/**
 * The viewModel of the MangaDetails screen.
 */
object MangaDetailsViewModel : ViewModel() {
    private val _selectedManga = MutableStateFlow<Data?>(null)
    val selectedManga = _selectedManga.asStateFlow()

    var coverArt by mutableStateOf<RelationshipsData?>(null)
        private set

    var imageUrl by mutableStateOf<String?>(null)
        private set

    var titleKey by mutableStateOf<String?>(null)
        private set

    var title by mutableStateOf<String?>(null)
        private set

    var description by mutableStateOf<String?>(null)
        private set

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()

    private val _isLibrary = MutableStateFlow(false)
    val isLibrary: StateFlow<Boolean> = _isLibrary.asStateFlow()

    /**
     * Sets the current manga to the one specified by [manga]
     * @param manga The manga to be set as the current manga.
     */
    fun selectedManga(manga: Data) {
        _selectedManga.value = manga
        updateMangaInfo()
    }

    /**
     * Updates all the info of the current manga.
     */
    private fun updateMangaInfo() {
        coverArt = selectedManga.value?.relationships?.find { it.type == "cover_art" }
        imageUrl = coverArt?.let {
            "https://uploads.mangadex.org/covers/${selectedManga.value?.id}/${it.attributes?.fileName}"
        }
        titleKey = selectedManga.value?.attributes?.title?.keys?.firstOrNull()
        title = selectedManga.value?.attributes?.title?.get(titleKey)
        description = selectedManga.value?.attributes?.description?.get("en")
    }

    /**
     * Adds the current manga to the likes in the DB.
     */
    fun addLikesManga() {
        viewModelScope.launch {
            MangaRepository.addLike(_selectedManga.value?.id, title)
        }
    }
    /**
     * Adds the current manga to the library in the DB.
     */
    fun addToLibraryManga() {
        viewModelScope.launch {
            MangaRepository.addToLibrary(_selectedManga.value?.id, title)
        }
    }

    /**
     * Checks if the current manga is in the likes of the DB.
     */
    fun checkIfMangaInLikes() {
        viewModelScope.launch {
            _isLiked.value = MangaRepository.isMangaInLikes(_selectedManga.value?.id)
        }
    }
    /**
     * Checks if the current manga is in the library of the DB.
     */
    fun checkIfMangaInLibrary() {
        viewModelScope.launch {
            _isLibrary.value = MangaRepository.isMangaInLibrary(_selectedManga.value?.id)
        }
    }

    /**
     * Removes the current manga from the likes of the DB.
     */
    fun removeMangaFromLikes() {
        viewModelScope.launch {
            MangaRepository.removeMangaFromLikes(_selectedManga.value?.id)
        }
    }
    /**
     * Removes the current manga from the library of the DB.
     */
    fun removeMangaFromLibrary() {
        viewModelScope.launch {
            MangaRepository.removeMangaFromLibrary(_selectedManga.value?.id)
        }
    }

}