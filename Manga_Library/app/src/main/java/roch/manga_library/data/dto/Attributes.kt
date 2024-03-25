package roch.manga_library.data.dto

import kotlinx.serialization.Serializable

/**
 * A data class representing the attributes of a manga.
 */
@Serializable
data class Attributes (
    val title : Map<String, String>,
    val altTitles : List<Titles>? = null,
    val description : Map<String, String>? = null,
    val publicationDemographic : String? = null,
    val status : String? = null,
    val year : Int? = null
)