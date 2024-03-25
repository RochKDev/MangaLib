package roch.manga_library.data.dto

import kotlinx.serialization.Serializable
/**
 * A data class representing the cover attributes of a manga.
 */
@Serializable
data class CoverAttributes(
    val description: String? =null,
    val volume: String? =null,
    val fileName: String? =null,
    val locale: String? =null,
    val createdAt: String? =null,
    val updatedAt: String? =null,
    val version: Int? =null
)
