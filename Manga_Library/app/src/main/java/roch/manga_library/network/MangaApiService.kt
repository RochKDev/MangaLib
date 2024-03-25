package roch.manga_library.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import roch.manga_library.data.dto.Mangas

/**
 * The interface to use the API calls.
 */
interface MangaApiService{
    /**
     * Gets the top rated mangas with the options passed to [options]
     * @param options a query map of the options to the API call.
     */
    @GET("manga")
    suspend fun getTopRatedManga(
        @QueryMap options : Map<String, String>
    ) : Mangas

    /**
     * Gets the recently released chapters of mangas
     * @param order the order of the sort of the result.
     * @param cover includes the cover_art in the result.
     * @param limit the limit of results received.
     */
    @GET("manga")
    suspend fun getRecentlyReleasedChapters(
        @Query("order[latestUploadedChapter]") order: String = "desc",
        @Query("includes[]") cover : String = "cover_art",
        @Query("limit") limit : String = "50"
    ): Mangas
    /**
     * Gets the most followed mangas
     * @param order the order of the sort of the result.
     * @param cover includes the cover_art in the result.
     * @param limit the limit of results received.
     */
    @GET("manga")
    suspend fun getMostFollowedManga(
        @Query("order[followedCount]") order: String = "desc",
        @Query("includes[]") cover : String = "cover_art",
        @Query("limit") limit : String = "50"
    ): Mangas
    /**
     * Gets the different mangas corresponding to the [title]
     * @param title the title of the manga to search.
     * @param orderRelevance the order of the sort of the result.
     * @param orderRating the order of the sort of the result.
     * @param orderFollowedCount the order of the sort of the result.
     * @param cover includes the cover_art in the result.
     * @param limit the limit of results received.
     */
    @GET("manga")
    suspend fun getSearchManga(
        @Query("title") title: String = "",
        @Query("order[relevance]") orderRelevance: String = "desc",
        @Query("order[rating]") orderRating: String = "desc",
        @Query("order[followedCount]") orderFollowedCount: String = "desc",
        @Query("includes[]") cover : String = "cover_art",
        @Query("limit") limit : String = "30"
    ) :Mangas

    /**
     * Gets the mangas by the given list of [ids]
     * @param ids a list od mangas id to retrieve.
     * @param cover includes the cover_art in the result.
     */
    @GET("manga")
    suspend fun getMangasByIds(
        @Query("ids[]") ids : List<String>,
        @Query("includes[]") cover : String = "cover_art"
    ): Mangas
    
}

/**
 * Creates the object [MangaApi] to use the api calls.
 */
object MangaApi{
    private const val BASE_URL = "https://api.mangadex.org/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    val retrofitService : MangaApiService by lazy {
        retrofit.create(MangaApiService::class.java)
    }
}