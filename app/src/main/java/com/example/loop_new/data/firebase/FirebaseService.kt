package com.example.loop_new.data.firebase

import android.util.Log
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.Category
import com.example.loop_new.domain.model.firebase.FavoriteStory
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import com.example.loop_new.domain.model.firebase.Statistics
import com.example.loop_new.domain.model.firebase.StatsSummary
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.model.firebase.TextContent
import com.example.loop_new.domain.model.firebase.User
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.ui.theme.categoryOfStory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicInteger

class FirebaseService(private val firestore: FirebaseFirestore) :
    FirebaseService {

    companion object {
        const val BOX = "box"
        const val FLASHCARD = "flashcard"
        const val USERS = "users"
        const val STORY = "story"
        const val FAVORITE_STORIES = "favoriteStories"
    }

    private val auth = Firebase.auth
    private val currentUser = auth.currentUser?.uid

    private fun logSuccess(message: String) {
        Log.d(LogTags.FIREBASE_SERVICES, message)
    }

    private fun logError(message: String) {
        Log.e(LogTags.FIREBASE_SERVICES, message)
    }

    /**
     * Creates a new user record in the Firestore database using Google user data.
     *
     * This function checks if a Google user is currently signed in and, if so,
     * adds their information to the Firestore database under the "users" collection.
     * It handles success and failure scenarios for the database operation.
     */
    override fun createNewGoogleUser() {
        // Fetch the currently signed-in Google user
        val signedInUser = getSignedInUser()

        if (signedInUser != null) {
            try {
                // Attempt to add the Google user's data to the Firestore database
                firestore.collection(USERS).document(signedInUser.uid ?: "")
                    .set(signedInUser)
                    .addOnSuccessListener {
                        logSuccess("createNewGoogleUser: Successful, created new google user!")
                    }
                    .addOnFailureListener { e ->
                        logError("createNewGoogleUser: ${e.printStackTrace()}")
                    }
            } catch (e: Exception) {
                logError("createNewGoogleUser: ${e.printStackTrace()}")
            }
        }
    }

    /**
     * Fetch the currently signed-in Google user's data.
     *
     * This function checks the Firebase Authentication's current user and, if a user is signed in,
     * creates and returns a User object containing the user's data
     *
     * @return A User object with the signed-in user's data, or null if no user is signed in.
     */
    override fun getSignedInUser(): User? = auth.currentUser?.run {
        User(
            email = email,
            uid = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    /**
     * Fetches a list of public boxes, supporting pagination.
     *
     * This function queries the Firestore database to retrieve public boxes, either starting
     * from the beginning of the collection or continuing from a specified last document snapshot.
     * The results are sent back as a flow of pairs containing the list of boxes and the last
     * visible document snapshot, which can be used for further pagination. Detailed logging is
     * provided throughout the process to track the operation's progress and any potential errors.
     *
     * @param lastDocSnapshot The last document snapshot from the previous query, used for pagination.
     * @return A flow emitting pairs of a list of boxes and the last document snapshot.
     */
    override fun fetchListOfPublicBox(lastDocSnapshot: DocumentSnapshot?): Flow<Pair<List<Box>, DocumentSnapshot?>> {
        return callbackFlow {
            logSuccess("fetchListOfPublicBox: Fetching list of public boxes")

            // The last document in the list activates the addition of further boxes
            var lastVisibleDocument: DocumentSnapshot? = lastDocSnapshot
            val fetchedBoxes = mutableListOf<Box>()

            // Construct the query based on whether we are paginating or not
            val query = if (lastVisibleDocument == null) {
                logSuccess("fetchListOfPublicBox: Querying first batch of public boxes")
                firestore.collection(BOX).limit(10)

            } else {
                logError("fetchListOfPublicBox: Querying next batch of public boxes starting after document: ${lastVisibleDocument.id}")
                firestore.collection(BOX).startAfter(lastVisibleDocument).limit(4)
            }

            try {
                val querySnapshot = query.get().await()
                val documents = querySnapshot.documents

                // Process the query results
                documents.forEach { document ->
                    document.toObject(Box::class.java)?.let {
                        fetchedBoxes.add(it)
                        logSuccess("Added box: ${it.name} to the list")
                    }
                    lastVisibleDocument = document
                }

                // Send the list of fetched boxes along with the last document for pagination
                logSuccess("fetchListOfPublicBox: Sending list of fetched boxes")
                trySend(Pair(fetchedBoxes, lastVisibleDocument)).isSuccess

                // Check if there are no more boxes to fetch
                if (documents.isEmpty()) {
                    logSuccess("fetchListOfPublicBox: No more public boxes to fetch")
                    close()
                }
            } catch (e: Exception) {
                logError("fetchListOfPublicBox: Error fetching public boxes: ${e.message}")
                close(e)
            }
            // Handle the closure of the flow
            awaitClose { logSuccess("fetchListOfPublicBox: Flow of public boxes has been closed") }
        }
    }

    /**
     * Fetches a list of flashcards from a specified public box in Firestore.
     *
     * This function sets up a real-time snapshot listener on the flashcard collection of
     * a given public box identified by 'boxUid'. It emits an updated list of flashcards
     * through a Flow whenever there are changes in the collection. The function also handles
     * and logs errors that may occur during the listening process.
     *
     * @param boxUid The unique identifier of the public box whose flashcards are to be fetched.
     * @return A Flow emitting a list of flashcards.
     */
    override fun fetchListOfFlashcardInPublicBox(boxUid: String): Flow<List<Flashcard>> {
        return callbackFlow {
            val listenerRegistration = firestore.collection(BOX).document(boxUid)
                .collection(FLASHCARD)
                .addSnapshotListener { flashcardsSnapshot, error ->
                    if (error != null) {
                        close(error)
                        logError("fetchListOfFlashcardInPublicBox: Error: $error")
                        return@addSnapshotListener
                    }

                    if (flashcardsSnapshot == null) {
                        logError("fetchListOfFlashcardInPublicBox: Snapshot is null")
                        trySend(emptyList<Flashcard>()).isSuccess
                        return@addSnapshotListener
                    }

                    val flashcards = flashcardsSnapshot.documents.mapNotNull { document ->
                        document.toObject(Flashcard::class.java)
                    }

                    trySend(flashcards).isSuccess
                    logSuccess("fetchListOfFlashcardInPublicBox: Success! Flashcards: ${flashcards.size}")
                }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override fun fetchListOfStory(): Flow<Category> {
        return callbackFlow {
            val categories = categoryOfStory
            val currentIndex = AtomicInteger(0)

            val listenerRegistration = fetchNextCategory(categories, currentIndex, this)

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    private fun fetchNextCategory(
        categories: List<String>,
        currentIndex: AtomicInteger,
        flow: SendChannel<Category>,
    ): ListenerRegistration {
        val currentCategory = categories[currentIndex.get()]

        return firestore.collection(STORY).document("yWhYIeotoTamzjd60yf9")
            .collection(currentCategory)
            .limit(5)
            .addSnapshotListener { querySnapshot, error ->

                if (error != null) {
                    flow.close(error)
                    return@addSnapshotListener
                }

                val stories = querySnapshot?.documents?.mapNotNull { document ->
                    val story = document.toObject(Story::class.java)
                    if (story != null) {
                        val isFavorite =
                            story.favoriteStories?.any { it.uid == currentUser } ?: false
                        story.copy(favorite = isFavorite)
                    } else {
                        null
                    }
                } ?: mutableListOf()

                val category = Category(currentCategory, stories)
                flow.trySend(category).isSuccess

                val nextIndex = currentIndex.incrementAndGet()

                if (nextIndex < categories.size) {
                    fetchNextCategory(categories, currentIndex, flow)
                }
            }
    }

    override suspend fun fetchAllStoriesFromOneCategory(category: String): List<Story> {
        val documents = mutableListOf<Story>()
        try {
            val querySnapshot = firestore.collection(STORY).document("yWhYIeotoTamzjd60yf9")
                .collection(category)
                .get()
                .await()
            for (document in querySnapshot.documents) {
                val story = document.toObject(Story::class.java)
                if (story != null) {
                    documents.add(story)
                }
            }
        } catch (e: Exception) {
            // Obsłuż błędy, np. brak połączenia z internetem itp.
            e.printStackTrace()
        }
        return documents
    }

    //TODO: add checking whether the logged in user has a given story in his favorites!!
    private fun isUserFavoriteStory(story: Story): Boolean {
        // Sprawdź, czy UID zalogowanego użytkownika znajduje się w liście ulubionych dla danej historii
        return story.favoriteStories?.any { it.uid == currentUser } ?: false
    }

    override suspend fun fetchStory(storyUid: String): Story? {
        return try {
            val document = firestore.collection(STORY).document("yWhYIeotoTamzjd60yf9")
                .collection("criminal").document(storyUid)
                .get()
                .await()

            val storyData = document.data
            Log.d("Firestore", "fetchStory: Success! $storyData")

            // Sprawdź, czy dane są niepuste
            if (storyData != null) {
                val title = storyData["title"].toString() as String?
                val author = storyData["author"].toString() as String?
                val entry = storyData["entry"].toString() as String?
                val level = storyData["level"].toString() as String?
                val category = storyData["category"].toString() as String?
                val image = storyData["image"].toString() as String?
                val chaptersList = storyData["text"] as List<Map<String, Any>>?
                val favoriteStoriesList = storyData["favoriteStories"] as List<Map<String, Any>>?
                val viewList = storyData["viewList"] as List<String>?

                // Sprawdź, czy masz listę rozdziałów
                val chapters = chaptersList?.map { chapterMap ->
                    TextContent(
                        chapter = chapterMap["chapter"].toString() as String?,
                        content = chapterMap["content"].toString() as String?
                    )
                } ?: emptyList()

                val favoriteStories = favoriteStoriesList?.map { favoriteMap ->
                    FavoriteStory(
                        uid = favoriteMap["uid"].toString() as String?,
                        category = favoriteMap["category"].toString() as String?,
                        favorite = favoriteMap["favorite"] as Boolean?
                    )
                } ?: emptyList()

                return Story(
                    title,
                    chapters,
                    author,
                    entry,
                    storyUid,
                    level,
                    category,
                    image,
                    favoriteStories,
                    viewList
                )

            } else {
                logError("fetchStory: data is empty!")
                null  // Jeśli dane są puste, zwróć null
            }
        } catch (e: Exception) {
            logError("fetchStory: $e")
            null
        }
    }

    override suspend fun fetchFavoriteStories(): List<Story> {
        val userDocument = firestore.collection(USERS).document(currentUser ?: "").get().await()
        val favoriteStoriesList =
            userDocument.toObject(User::class.java)?.favoriteStories ?: emptyList()

        val stories = mutableListOf<Story>()
        for (favoriteStory in favoriteStoriesList) {
            val storyDocument = firestore.collection(STORY)
                .document("yWhYIeotoTamzjd60yf9")
                .collection(favoriteStory.category ?: "")
                .document(favoriteStory.uid ?: "")
                .get()
                .await()

            val story = storyDocument.toObject(Story::class.java)
            story?.let { stories.add(it) }
        }

        return stories
    }

    override suspend fun addStoryToFavoriteSection(storyId: String, category: String) {
        val favoriteData = mapOf(
            "uid" to storyId,
            "userUid" to currentUser,
            "category" to category,
            "favorite" to true
        )
        val favoriteList = FieldValue.arrayUnion(favoriteData)

        val storyDocRef = firestore.collection(STORY).document("yWhYIeotoTamzjd60yf9")
            .collection(category).document(storyId)
        val userDocRef = firestore.collection(USERS).document(currentUser ?: "")

        try {
            // Dodaj historię do ulubionych w historii
            storyDocRef.update(FAVORITE_STORIES, favoriteList).await()
            logSuccess("addStoryToFavoriteSection: The story has been added to your favorites in the story.")

            // Dodaj historię do ulubionych użytkownika
            userDocRef.update(FAVORITE_STORIES, favoriteList).await()
            logSuccess("addStoryToFavoriteSection: The story has been added to your favorites in the user.")

        } catch (e: Exception) {
            logError("addStoryToFavoriteSection: Error adding stories to favorites: $e")
        }
    }

    override suspend fun removeStoryFromFavoriteSection(storyId: String, category: String) {
        val userDocRef = firestore.collection(USERS).document(currentUser ?: "")
        val storyDocRef = firestore.collection(STORY).document("yWhYIeotoTamzjd60yf9")
            .collection(category).document(storyId)

        try {
            val userSnapshot = userDocRef.get().await()
            val userFavorites = userSnapshot["favoriteStories"] as? MutableList<Map<String, String>>

            val storySnapshot = storyDocRef.get().await()
            val storyFavorites =
                storySnapshot["favoriteStories"] as? MutableList<Map<String, String>>

            val indexToRemoveUser = userFavorites?.indexOfFirst { it["uid"] == storyId }
            val indexToRemoveStory = storyFavorites?.indexOfFirst { it["userUid"] == currentUser }

            indexToRemoveUser?.let { index ->
                userFavorites.removeAt(index)
                userDocRef.update("favoriteStories", userFavorites).await()
                logSuccess("removeStoryFromFavoriteSection: The story has been removed from favorites.")
            }
                ?: logError("removeStoryFromFavoriteSection: Story with UID $storyId does not exist in the user's favorites list.")

            indexToRemoveStory?.let { index ->
                storyFavorites.removeAt(index)
                storyDocRef.update("favoriteStories", storyFavorites).await()
                logSuccess("removeStoryFromFavoriteSection: The story has been removed from your history favorites.")
            }
                ?: logError("removeStoryFromFavoriteSection: Story with UID $storyId does not exist in the history favorites list.")

        } catch (e: Exception) {
            logError("removeStoryFromFavoriteSection: Error when deleting history from favorites: $e")
        }
    }

    override suspend fun addLessonStatsToFirestore(flashcardUid: String, status: String) {
        val currentDate = getCurrentDate()
        val data = mutableMapOf("status" to status, "uid" to flashcardUid)
        val storyDocRef = firestore.collection("stats").document(currentUser ?: "")

        val currentDateTime = getCurrentDateTime()

        try {
            storyDocRef.get().await().let { documentSnapshot ->
                val lessonsData =
                    (documentSnapshot.get("lessonsData") as? Map<String, List<Map<String, Any>>>
                        ?: emptyMap()).toMutableMap()

                val allFlashcards =
                    (documentSnapshot.get("allFlashcards") as? List<Map<String, String>>
                        ?: emptyList()).toMutableList()

                updateLessonsData(lessonsData, currentDate, data)
                updateAllFlashcards(
                    allFlashcards,
                    flashcardUid,
                    status,
                    data,
                    currentDateTime,
                    currentDateTime
                )

                val userData = mapOf("lessonsData" to lessonsData, "allFlashcards" to allFlashcards)
                val action =
                    if (documentSnapshot.exists()) storyDocRef.set(userData, SetOptions.merge())
                    else storyDocRef.set(userData)

                action.addOnSuccessListener { logSuccess("addLessonStatsToFirestore: Data added successfully.") }
                    .addOnFailureListener { e -> logError("addLessonStatsToFirestore: Error adding data: $e") }
            }
        } catch (e: Exception) {
            logError("addLessonStatsToFirestore: Error retrieving user data: $e")
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun updateLessonsData(
        lessonsData: MutableMap<String, List<Map<String, Any>>>,
        currentDate: String,
        data: Map<String, Any>,
    ) {
        val currentLessonData = (lessonsData[currentDate] ?: mutableListOf()).toMutableList()
        currentLessonData.add(data)
        lessonsData[currentDate] = currentLessonData
    }

    private fun updateAllFlashcards(
        allFlashcards: MutableList<Map<String, String>>,
        flashcardUid: String,
        status: String,
        data: MutableMap<String, String>,
        firstStudy: String?,
        lastStudy: String?,
    ) {
        val existingIndex = allFlashcards.indexOfFirst { it["uid"] == flashcardUid }

        if (existingIndex != -1) {
            val existingData = allFlashcards[existingIndex]
            if (existingData["status"] != status) {
                val updatedFlashcardData =
                    existingData.toMutableMap() // Tworzymy kopię istniejących danych

                when (status) {
                    KnowledgeLevel.KNOW.value -> {
                        updatedFlashcardData["lastStudy"] = lastStudy ?: getCurrentDateTime()
                        updatedFlashcardData["status"] = "know"
                    }
                }
                allFlashcards[existingIndex] = updatedFlashcardData
            }
        } else {

            when (status) {
                KnowledgeLevel.KNOW.value -> {
                    data["firstStudy"] = firstStudy ?: getCurrentDateTime()
                    data["lastStudy"] = lastStudy ?: getCurrentDateTime()
                }

                KnowledgeLevel.SOMEWHAT_KNOW.value -> {
                    data["firstStudy"] = firstStudy ?: getCurrentDateTime()
                    data["lastStudy"] = ""
                }

                KnowledgeLevel.DO_NOT_KNOW.value -> {
                    data["firstStudy"] = firstStudy ?: getCurrentDateTime()
                    data["lastStudy"] = ""
                }
            }

            allFlashcards.add(data)
        }
    }

    private fun getCurrentDateTime(): String {
        // Utwórz obiekt reprezentujący bieżącą datę i godzinę
        val currentDate = Date()

        // Ustaw format daty i godziny
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Sformatuj bieżącą datę i godzinę
        return dateFormat.format(currentDate)
    }

    override suspend fun fetchDataOfStats(): Pair<Statistics, StatsSummary> {
        try {
            val documentSnapshot = firestore
                .collection("stats").document(currentUser ?: "")
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val data = documentSnapshot.toObject(Statistics::class.java)
                data?.let { stats ->
                    val allFlashcards = stats.allFlashcards

                    val totalFlashcards = allFlashcards.size
                    val knowCount = allFlashcards.filter { it.status == "know" }.size
                    val somewhatKnow = allFlashcards.filter { it.status == "somewhatKnow" }.size
                    val doNotKnowCount = allFlashcards.filter { it.status == "doNotKnow" }.size

                    val statsSummary =
                        StatsSummary(totalFlashcards, knowCount, somewhatKnow, doNotKnowCount)

                    return Pair(stats, statsSummary)
                } ?: throw Exception("Error: Firestore document snapshot parsing failed.")
            } else {
                throw Exception("Error: Firestore document does not exist.")
            }
        } catch (e: Exception) {
            println("Error fetching data: $e")
            throw e
        }
    }

    override fun addStoryUidToViewList(uidStory: String) {

        currentUser?.let {
            val storyRef = firestore.collection(STORY).document("yWhYIeotoTamzjd60yf9")
                .collection("criminal").document(uidStory)

            firestore.runTransaction { transaction ->
                val storyDoc = transaction.get(storyRef)
                val viewList = storyDoc.toObject(Story::class.java)?.viewList?.toMutableList()

                if (viewList != null && currentUser !in viewList) {
                    viewList.add(currentUser)
                    transaction.update(storyRef, "viewList", viewList)
                }
            }.addOnSuccessListener {
                logSuccess("addStoryUidToViewList: Transaction successfully completed")

            }.addOnFailureListener { e ->
                logError("addStoryUidToViewList: Transaction failed: ${e.message}")
            }
        }
    }
}