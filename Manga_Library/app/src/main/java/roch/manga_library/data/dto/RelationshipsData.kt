package roch.manga_library.data.dto

import kotlinx.serialization.Serializable
/**
 * A data class representing the relationships of a manga.
 */
@Serializable
data class RelationshipsData (
    val id : String? = null,
    val type : String? = null,
    val attributes: CoverAttributes? = null,
    val related : String? =null
)