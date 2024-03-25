package roch.manga_library.data.dto

import kotlinx.serialization.Serializable
/**
 * A data class representing the data of a manga.
 */
    @Serializable
    data class Data (
        val id : String,
        val type : String,
        val attributes : Attributes,
        val relationships : List<RelationshipsData> ? = null
    )