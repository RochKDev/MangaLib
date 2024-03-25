package roch.manga_library.data

import androidx.annotation.StringRes
import roch.manga_library.R

/**
 * Data class that represents the routes to different screens.
 */
enum class Screen (@StringRes val title : Int ) {
    NewReleases(title = R.string.new_releases_screen),
    Recommendations(title = R.string.recommendations),
    MyLibrary(title = R.string.library),
    Search(title = R.string.search),
    Top(title = R.string.top),
    Account(title = R.string.account),
    TopRatedManga(title = R.string.top_rated_manga),
    TopFollowedManga(title = R.string.top_followed_manga),
    MangaDetails(title = R.string.manga_details),
    SignIn(title = R.string.sign_in)
}