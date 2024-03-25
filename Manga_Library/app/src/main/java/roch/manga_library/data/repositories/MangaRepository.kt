package roch.manga_library.data.repositories

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import roch.manga_library.data.dto.MangaDB

/**
 * A class that interacts with the FireBase DB
 */
object MangaRepository {
    val dataBase =
        Firebase.database("https://manga-library-1fdd1-default-rtdb.europe-west1.firebasedatabase.app/")
            .setPersistenceEnabled(true)

    private val dataBaseRef =
        Firebase.database("https://manga-library-1fdd1-default-rtdb.europe-west1.firebasedatabase.app/").reference

    private val usersRef =
        Firebase.database("https://manga-library-1fdd1-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")

    init {
        usersRef.keepSynced(true)
    }

    /**
     * Adds the [id] and [name] of the specified manga to the likes in the DB, only if
     * it isn't there already.
     * @param id The id of the manga to add
     * @param name The name of the manga to add
     */
    fun addLike(id: String?, name: String?) {
        val userId by mutableStateOf(FirebaseAuth.getInstance().currentUser?.uid)
        val like = MangaDB(
            mangaTitle = name,
            mangaId = id
        )

        val userLikesRef = dataBaseRef.child("users").child(userId ?: "1").child("likes")
        userLikesRef.addListenerForSingleValueEvent(object :
            ValueEventListener { // to prevent repetitions
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val existingLike = childSnapshot.getValue(MangaDB::class.java)
                    if (existingLike?.mangaId == id) {
                        //Manga already in likes
                        return
                    }
                }
                // Manga doesn't exist so add it
                val newLikeRef = userLikesRef.child(id ?: "1")
                newLikeRef.setValue(like)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    /**
     * Adds the [id] and [name] of the specified manga to the library in the DB, only if
     * it isn't there already.
     * @param id The id of the manga to add
     * @param name The name of the manga to add
     */
    fun addToLibrary(id: String?, name: String?) {
        val userId by mutableStateOf(FirebaseAuth.getInstance().currentUser?.uid)
        val library = MangaDB(
            mangaTitle = name,
            mangaId = id
        )

        val userLibraryRef = dataBaseRef.child("users").child(userId ?: "1").child("library")
        userLibraryRef.addListenerForSingleValueEvent(object :
            ValueEventListener { // to prevent repetitions
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val existingLike = childSnapshot.getValue(MangaDB::class.java)
                    if (existingLike?.mangaId == id) {
                        //Manga already in likes
                        return
                    }
                }
                // Manga doesn't exist so add it
                val newLibraryRef = userLibraryRef.child(id ?: "1")
                newLibraryRef.setValue(library)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    /**
     * Checks if the manga [id] is in the likes of the DB.
     * @param id The id of the manga to check in the DB.
     */
    suspend fun isMangaInLikes(id: String?): Boolean {
        var result: Boolean
        withContext(Dispatchers.IO) {
            result = try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null && id != null) {
                    val userLikesRef = dataBaseRef.child("users").child(userId).child("likes")
                    // checks if ID is is likes
                    val query = userLikesRef.orderByKey().equalTo(id)
                    val snapshot = Tasks.await(query.get())
                    snapshot.exists()
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
        return result
    }

    /**
     * Checks if the manga [id] is in the library of the DB.
     * @param id The id of the manga to check in the DB.
     */
    suspend fun isMangaInLibrary(id: String?): Boolean {
        var result: Boolean
        withContext(Dispatchers.IO) {
            result = try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null && id != null) {
                    val userLibraryRef = dataBaseRef.child("users").child(userId).child("library")
                    // checks if ID is is likes
                    val query = userLibraryRef.orderByKey().equalTo(id)
                    val snapshot = Tasks.await(query.get())
                    snapshot.exists()
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
        return result
    }

    /**
     * Removes the manga with the given [id] from the likes of the DB.
     * @param id The id of the manga to remove from the likes of the DB.
     */
    suspend fun removeMangaFromLikes(id: String?) {
        withContext(Dispatchers.IO) {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null && id != null) {
                    val userLikesRef = dataBaseRef.child("users").child(userId).child("likes")

                    // checks if manga ID is in likes
                    val query = userLikesRef.orderByKey().equalTo(id)
                    val snapshot = Tasks.await(query.get())

                    if (snapshot.exists()) {
                        // Manga is in likes so we can delete it
                        snapshot.ref.child(id).removeValue()
                    }
                } else {
                    return@withContext
                }
            } catch (e: Exception) {
                Log.d("Remove", "${e.message}")
            }
        }
    }
    /**
     * Removes the manga with the given [id] from the library of the DB.
     * @param id The id of the manga to remove from the library of the DB.
     */
    suspend fun removeMangaFromLibrary(id: String?) {
        withContext(Dispatchers.IO) {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null && id != null) {
                    val userLibraryRef = dataBaseRef.child("users").child(userId).child("library")

                    // checks if manga ID is in likes
                    val query = userLibraryRef.orderByKey().equalTo(id)
                    val snapshot = Tasks.await(query.get())

                    if (snapshot.exists()) {
                        // Manga is in likes so we can delete it
                        snapshot.ref.child(id).removeValue()
                    }
                } else {
                    return@withContext
                }
            } catch (e: Exception) {
                Log.d("Remove", "${e.message}")
            }
        }
    }

    /**
     * Gets all the liked mangas of the user from the DB.
     */
    suspend fun getUserLikes() : List<String>{
        val likedMangas = mutableListOf<String>()
        withContext(Dispatchers.IO){
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val userLikesRef = dataBaseRef.child("users").child(userId).child("likes")

                    // Get all the mangas liked by the user
                    val snapshot = Tasks.await(userLikesRef.get())

                    for (childSnapshot in snapshot.children) {
                        val like = childSnapshot.getValue(MangaDB::class.java)
                        if (like != null) {
                            likedMangas.add(like.mangaId?:"1")
                        }
                    }
                }
            } catch (e:Exception){
                return@withContext
            }
        }
        return likedMangas
    }

    /**
     * Gets all the mangas in the library in the DB.
     */
    suspend fun getUserLibrary() : List<String>{
        val libraryMangas = mutableListOf<String>()
        withContext(Dispatchers.IO){
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val userLibraryRef = dataBaseRef.child("users").child(userId).child("library")

                    // Get all the mangas liked by the user
                    val snapshot = Tasks.await(userLibraryRef.get())

                    for (childSnapshot in snapshot.children) {
                        val library = childSnapshot.getValue(MangaDB::class.java)
                        if (library != null) {
                            libraryMangas.add(library.mangaId?:"1")
                        }
                    }
                }
            } catch (e:Exception){
                return@withContext
            }
        }
        return libraryMangas
    }
}

