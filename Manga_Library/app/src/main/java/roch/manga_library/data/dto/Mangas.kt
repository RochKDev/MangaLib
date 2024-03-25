package roch.manga_library.data.dto

import kotlinx.serialization.Serializable
/**
 * A data class representing a list od manga data.
 */
@Serializable
data class Mangas(
    val data : List<Data>
)


