package roch.manga_library.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/**
 * A data class representing the different titles of a manga.
 */
@Serializable
data class Titles(
    val en : String? = null,
    @SerialName(value = "ja-ro")
    val jaRo : String? = null,
    @SerialName(value = "ja_jp")
    val ja : String? = null,
)