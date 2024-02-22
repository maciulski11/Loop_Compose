package com.example.loop_new.data.firebase

import android.util.Log
import com.example.loop_new.FlashcardFields
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.Category
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.model.firebase.TextContent
import com.example.loop_new.domain.model.firebase.User
import com.example.loop_new.domain.services.FirebaseService
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

class FirebaseService(private val firestore: FirebaseFirestore) :
    FirebaseService {

    companion object {
        const val BOX = "box"
        const val FLASHCARD = "flashcard"
        const val REPEAT = "repeat"
        const val USERS = "users"
        const val STORY = "story"
    }

    private val auth = Firebase.auth
    private val currentUser = auth.currentUser?.uid

    private val currentTime = Timestamp.now()

    // Create a calendar with date
    private val calendar = Calendar.getInstance()

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
                        Log.d("REPO_CREATE_NEW_GOOGLE_USER", "Successful, created new google user!")
                    }
                    .addOnFailureListener { e ->
                        Log.d("REPO_CREATE_NEW_GOOGLE_USER", "${e.printStackTrace()}")
                    }
            } catch (e: Exception) {
                Log.d("REPO_CREATE_NEW_GOOGLE_USER", "${e.printStackTrace()}")
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
     * Download and transfer the box with flashcards from the public section to
     * the user's private section.
     *
     * In this function, a public box with flashcards is downloaded and
     * then added to the user's private section. The function handles the
     * retrieval of the box and its associated flashcards, and then triggers
     * their addition to the private section.
     *
     * @param boxUid The unique identifier of the public box to be transferred.
     */
    override fun addPublicBoxToPrivateSection(boxUid: String) {
        // Begin by attempting to download the specified box from the public section
        firestore.collection(BOX).document(boxUid).get()
            .addOnSuccessListener { document ->
                // Check if the box document exists in the Firestore database
                if (document.exists()) {
                    val boxData = document.data
                    // Proceed if the box data is successfully retrieved
                    if (boxData != null) {
                        // Next, download all flashcards associated with this box
                        firestore.collection(BOX).document(boxUid)
                            .collection(FLASHCARD)
                            .whereEqualTo("boxUid", boxUid)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                // Convert the snapshot documents to Flashcard objects.
                                val flashcards = querySnapshot.documents.mapNotNull { document ->
                                    document.toObject(Flashcard::class.java)
                                }
                                Log.d(LogTags.FIREBASE_SERVICES, "Downloaded box with: $flashcards")

                                // Finally, transfer the box and its flashcards to the private section
                                addPrivateBoxWithFlashcards(boxUid, boxData, flashcards)
                            }
                            .addOnFailureListener { e ->
                                Log.e(
                                    LogTags.FIREBASE_SERVICES,
                                    "Error downloading flashcards: ${e.message}"
                                )
                            }
                    }
                } else {
                    Log.d("Firestore", "Box nie istnieje")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Błąd pobierania boxa: ${e.message}")
            }
    }

    /**
     * Adds a box along with its associated flashcards to the user's private section.
     *
     * This function takes a box and its related flashcards and adds them to the
     * Firestore database under the current user's private collection. It performs
     * this operation as a batch to ensure atomicity, meaning either all operations
     * succeed or none do. This function is a continuation of the process started
     * in 'createBoxInPrivateSection'.
     *
     * @param boxUid The unique identifier of the box.
     * @param boxData The data of the box including title, description, and colors.
     * @param flashcards A list of flashcards associated with the box.
     */
    private fun addPrivateBoxWithFlashcards(
        boxUid: String,
        boxData: Map<String, Any>,
        flashcards: List<Flashcard>,
    ) {
        val userBoxRef = firestore.collection(USERS).document(currentUser ?: "")
            .collection(BOX).document(boxUid)

        firestore.runBatch { batch ->
            // Add the box to the user's private collection
            batch.set(userBoxRef, boxData)

            // Add each associated flashcard to the box in the user's private collection
            flashcards.forEach { flashcard ->
                val flashcardRef = userBoxRef.collection(FLASHCARD).document(flashcard.uid ?: "")
                batch.set(flashcardRef, flashcard)
            }
        }.addOnSuccessListener {
            Log.d(
                LogTags.FIREBASE_SERVICES,
                "Box and flashcards successfully added to private collection"
            )
        }.addOnFailureListener { e ->
            Log.e(LogTags.FIREBASE_SERVICES, "Error adding box and flashcards: ${e.message}")
        }
    }

    /**
     * Create box for flashcards in the user's private section.
     *
     * The user can create a box that will have a title, description, and
     * can choose background colors from the available options.
     */
    override fun createBoxInPrivateSection(box: Box) {
        val uid = UUID.randomUUID().toString()
        val data = Box(
            name = box.name,
            describe = box.describe,
            uid = uid,
            color1 = box.color1,
            color2 = box.color2,
            color3 = box.color3,
            permissionToEdit = true
        )

        try {
            firestore
                .collection(USERS).document(currentUser ?: "")
                .collection(BOX).document(uid)
                .set(data)

            Log.d(LogTags.FIREBASE_SERVICES, "addBox: Correct addition of box")
        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "addBox: Error: $e")
            throw e
        }
    }

    /**
     * Deletes a specific box from the current user's collection in the Firestore database.
     *
     * This function attempts to delete a box identified by `boxUid` from the Firestore
     * collection belonging to the currently signed-in user. It logs the outcome of the
     * operation, indicating whether the deletion was successful or if an error occurred.
     *
     * @param boxUid The unique identifier of the box to be deleted.
     */
    override fun deleteBox(boxUid: String) {
        try {
            firestore.collection(USERS).document(currentUser ?: "")
                .collection(BOX).document(boxUid)
                .delete()

            Log.d(LogTags.FIREBASE_SERVICES, "deleteBox: Success!")

        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "deleteBox: Error: $e")
            throw e
        }
    }

    /**
     * Updates the knowledge level of a specific flashcard in the user's box.
     *
     * This function modifies the knowledge level (or other specified attributes) of a flashcard
     * identified by 'flashcardUid' within a box identified by 'boxUid'. The update is performed
     * in the Firestore database under the current user's collection.
     *
     * @param boxUid The unique identifier of the box containing the flashcard.
     * @param flashcardUid The unique identifier of the flashcard to be updated.
     * @param updateData A map containing the fields and values to be updated.
     */
    private fun setKnowledgeLevelOfFlashcard(
        boxUid: String,
        flashcardUid: String,
        updateData: Map<String, *>,
    ) {
        try {
            firestore.collection(USERS).document(currentUser ?: "")
                .collection(BOX).document(boxUid)
                .collection(FLASHCARD).document(flashcardUid)
                .update(updateData)
                .addOnSuccessListener {
                    Log.d(
                        LogTags.FIREBASE_SERVICES,
                        "setKnowledgeLevelOfFlashcard: Update successful"
                    )
                }
                .addOnFailureListener { e ->
                    Log.e(
                        LogTags.FIREBASE_SERVICES,
                        "setKnowledgeLevelOfFlashcard: Error updating: $e"
                    )
                }
        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "setKnowledgeLevelOfFlashcard: Error: $e")
        }
    }

    /**
     * Update the status of a flashcard to 'Know' in the user's box.
     *
     * This function changes the knowledge level of a specific flashcard to 'Know',
     * and updates the last studied date and the next study date. It calculates the next
     * study date based on the current time and adds a predefined number of days (4 days in this case).
     *
     * @param boxUid The unique identifier of the box containing the flashcard.
     * @param flashcardUid The unique identifier of the flashcard to be updated.
     */
    override fun updateFlashcardToKnow(boxUid: String, flashcardUid: String) {
        // Set the calendar to the current time
        calendar.time = currentTime.toDate()
        // Add 4 days to the current time for the next study date
        calendar.add(Calendar.DAY_OF_MONTH, 4)
        val newTimestamp = Timestamp(calendar.time)

        // Prepare the data to update the flashcard's knowledge level and study dates
        val updateData = mapOf(
            FlashcardFields.KNOWLEDGE_LEVEL to KnowledgeLevel.KNOW.value,
            FlashcardFields.LAST_STUDIED_DATE to currentTime.toDate(),
            FlashcardFields.NEXT_STUDY_DATE to newTimestamp
        )

        // Call the function to set the knowledge level of the flashcard with the new data
        setKnowledgeLevelOfFlashcard(boxUid, flashcardUid, updateData)
    }

    /**
     * Updates the status of a flashcard to 'Somewhat Know' in the user's box.
     *
     * This function changes the knowledge level of a specific flashcard to 'Somewhat Know',
     * and updates the last studied date and the next study date. It calculates the next
     * study date based on the current time and adds a predefined number of days (2 days in this case).
     *
     * @param boxUid The unique identifier of the box containing the flashcard.
     * @param flashcardUid The unique identifier of the flashcard to be updated.
     */
    override fun updateFlashcardToSomewhatKnow(boxUid: String, flashcardUid: String) {

        // Set the calendar to the current time
        calendar.time = currentTime.toDate()
        // Add 2 days to the current time for the next study date
        calendar.add(Calendar.DAY_OF_MONTH, 2)
        val newTimestamp = Timestamp(calendar.time)

        // Prepare the data to update the flashcard's knowledge level and study dates
        val updateData = mapOf(
            FlashcardFields.KNOWLEDGE_LEVEL to KnowledgeLevel.SOMEWHAT_KNOW.value,
            FlashcardFields.LAST_STUDIED_DATE to currentTime.toDate(),
            FlashcardFields.NEXT_STUDY_DATE to newTimestamp
        )

        // Call the function to set the knowledge level of the flashcard with the new data
        setKnowledgeLevelOfFlashcard(boxUid, flashcardUid, updateData)
    }

    /**
     * Updates the status of a flashcard to 'Do Not Know' in the user's box.
     *
     * This function changes the knowledge level of a specific flashcard to 'Do Not Know',
     * and updates the last studied date. It sets the next study date to 12 hours from
     * the current time, indicating a shorter interval for re-study due to the flashcard
     * being marked as not known.
     *
     * @param boxUid The unique identifier of the box containing the flashcard.
     * @param flashcardUid The unique identifier of the flashcard to be updated.
     */
    override fun updateFlashcardToDoNotKnow(boxUid: String, flashcardUid: String) {

        // Set the calendar to the current time
        calendar.time = currentTime.toDate()
        // Add 12 hours to the current time for the next study date, implying a quicker review cycle
        calendar.add(Calendar.HOUR_OF_DAY, 12)
        val newTimestamp = Timestamp(calendar.time)

        val updateData = mapOf(
            FlashcardFields.KNOWLEDGE_LEVEL to KnowledgeLevel.DO_NOT_KNOW.value,
            FlashcardFields.LAST_STUDIED_DATE to currentTime.toDate(),
            FlashcardFields.NEXT_STUDY_DATE to newTimestamp
        )

        // Call the function to set the knowledge level of the flashcard with the new data
        setKnowledgeLevelOfFlashcard(boxUid, flashcardUid, updateData)
    }

    /**
     * Sets up a real-time listener for changes in the user's 'repeat' collection in Firestore.
     *
     * This function creates a Firestore snapshot listener on the 'repeat' collection
     * of the current user. It triggers a callback function with a boolean flag indicating
     * whether the collection is empty whenever there is an update in the collection.
     * The function logs any errors encountered but does not stop the application.
     *
     * @param onCollectionUpdate A callback function that is triggered with the update status of the collection.
     */
    override fun setupRepeatCollectionListener(onCollectionUpdate: (Boolean) -> Unit) {
        if (currentUser != null) {
            firestore.collection(USERS).document(currentUser)
                .collection(REPEAT)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        Log.e(LogTags.FIREBASE_SERVICES, "Repeat section listener error: $error")
                        return@addSnapshotListener
                    }

                    // Update status based on data from Firestore
                    querySnapshot?.let {
                        onCollectionUpdate(it.isEmpty)
                    }
                }
        }
    }

    /**
     * Checks whether the 'repeat' collection for the current user in Firestore is empty.
     *
     * This suspend function asynchronously retrieves the 'repeat' collection of the
     * current user from Firestore and returns a Boolean value indicating whether the
     * collection is empty. If an error occurs during the Firestore operation, the function
     * logs the error and returns false, indicating the check could not be completed.
     *
     * @return Boolean indicating whether the 'repeat' collection is empty (true if empty, false otherwise).
     */
    override suspend fun checkRepeatCollectionWhetherIsEmpty(): Boolean {
        if (currentUser != null) {
            return try {
                // Asynchronously retrieve the 'repeat' collection for the current user
                val querySnapshot = firestore.collection(USERS).document(currentUser)
                    .collection(REPEAT)
                    .get()
                    .await()

                // Return true if the collection is empty, false otherwise
                querySnapshot.isEmpty
            } catch (exception: Exception) {
                Log.e(LogTags.FIREBASE_SERVICES, "checkFirestoreCollection exception: $exception")
                false // Return false in case of an exception
            }
        }
        // Return false if there is no current user
        return false
    }

    /**
     * Adds a new flashcard to a specified box in the user's private section in Firestore.
     *
     * This function generates a unique identifier for the new flashcard, associates it with
     * a specific box using 'boxUid', and then adds it to the Firestore database. It logs the
     * result of the operation, indicating whether the addition was successful or encountered an error.
     *
     * @param flashcard The flashcard object to be added.
     * @param boxUid The unique identifier of the box to which the flashcard will be added.
     */
    override fun addFlashcardInPrivateSection(flashcard: Flashcard, boxUid: String) {
        val uid = UUID.randomUUID().toString()
        val data = flashcard.copy(uid = uid, boxUid = boxUid)

        try {
            firestore.collection(USERS).document(currentUser ?: "")
                .collection(BOX).document(boxUid)
                .collection(FLASHCARD).document(uid)
                .set(data)

            Log.d(LogTags.FIREBASE_SERVICES, "addFlashcard: Correct addition of flashcard")

        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "addFlashcard: Error: $e")
            throw e
        }
    }

    /**
     * Deletes a specific flashcard from a user's box in the Firestore database.
     *
     * This function attempts to delete a flashcard identified by `flashcardUid`
     * from a specific box identified by `boxUid` within the current user's collection.
     * It logs the outcome of the operation, indicating success or failure.
     *
     * @param boxUid The unique identifier of the box containing the flashcard.
     * @param flashcardUid The unique identifier of the flashcard to be deleted.
     */
    override fun deleteFlashcard(boxUid: String, flashcardUid: String) {
        try {
            firestore.collection(USERS).document(currentUser ?: "")
                .collection(BOX).document(boxUid)
                .collection(FLASHCARD).document(flashcardUid)
                .delete()

            Log.d(LogTags.FIREBASE_SERVICES, "deleteFlashcard: Success!")

        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "deleteFlashcard: Error: $e")
            throw e
        }
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
            Log.d(LogTags.FIREBASE_SERVICES, "Fetching list of public boxes")

            // The last document in the list activates the addition of further boxes
            var lastVisibleDocument: DocumentSnapshot? = lastDocSnapshot
            val fetchedBoxes = mutableListOf<Box>()

            // Construct the query based on whether we are paginating or not
            val query = if (lastVisibleDocument == null) {
                Log.d(LogTags.FIREBASE_SERVICES, "Querying first batch of public boxes")
                firestore.collection(BOX).limit(10)
            } else {
                Log.d(
                    LogTags.FIREBASE_SERVICES,
                    "Querying next batch of public boxes starting after document: ${lastVisibleDocument.id}"
                )
                firestore.collection(BOX).startAfter(lastVisibleDocument).limit(4)
            }

            try {
                val querySnapshot = query.get().await()
                val documents = querySnapshot.documents

                // Process the query results
                documents.forEach { document ->
                    document.toObject(Box::class.java)?.let {
                        fetchedBoxes.add(it)
                        Log.d(LogTags.FIREBASE_SERVICES, "Added box: ${it.name} to the list")
                    }
                    lastVisibleDocument = document
                }

                // Send the list of fetched boxes along with the last document for pagination
                Log.d(LogTags.FIREBASE_SERVICES, "Sending list of fetched boxes")
                trySend(Pair(fetchedBoxes, lastVisibleDocument)).isSuccess

                // Check if there are no more boxes to fetch
                if (documents.isEmpty()) {
                    Log.d(LogTags.FIREBASE_SERVICES, "No more public boxes to fetch")
                    close()
                }
            } catch (e: Exception) {
                Log.e(LogTags.FIREBASE_SERVICES, "Error fetching public boxes: ${e.message}")
                close(e)
            }
            // Handle the closure of the flow
            awaitClose { Log.d(LogTags.FIREBASE_SERVICES, "Flow of public boxes has been closed") }
        }
    }

    /**
     * Fetches a list of boxes from the user's private section in Firestore.
     *
     * This function sets up a real-time snapshot listener on the user's private box collection.
     * Any changes in the collection will emit an updated list of boxes through the Flow.
     * The function handles errors and logs them, and also ensures the proper closure of the listener.
     *
     * @return A Flow emitting a list of boxes.
     */
    override fun fetchListOfPrivateBox(): Flow<List<Box>> {
        return callbackFlow {
            if (currentUser != null) {
                // Setting up a real-time snapshot listener on the user's box collection
                val listenerRegistration = firestore.collection(USERS).document(currentUser)
                    .collection(BOX)
                    .addSnapshotListener { querySnapshot, error ->

                        // Handle any errors that might occur during snapshot listening
                        if (error != null) {
                            close(error)
                            Log.e(
                                LogTags.FIREBASE_SERVICES,
                                "fetchListOfBoxPrivateBox: Error: $error"
                            )
                            return@addSnapshotListener
                        }

                        // Map each document in the snapshot to a Box object and send the list through the Flow
                        val tempList = querySnapshot?.documents?.mapNotNull {
                            it.toObject(Box::class.java)
                        } ?: mutableListOf()

                        trySend(tempList).isSuccess
                        Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfBoxPrivateBox: Success!")
                    }

                // Ensure the removal of the snapshot listener when the Flow is closed or cancelled
                awaitClose {
                    listenerRegistration.remove()
                }
            }
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
            // Setting up a real-time snapshot listener on the flashcard collection
            val listenerRegistration =
                firestore.collection(BOX).document(boxUid)
                    .collection(FLASHCARD)
                    .addSnapshotListener { flashcardsSnapshot, error ->
                        // Handle any errors encountered during snapshot listening
                        if (error != null) {
                            close(error)
                            Log.e(
                                LogTags.FIREBASE_SERVICES,
                                "fetchListOfFlashcardInPublicBox: Error: $error"
                            )
                            return@addSnapshotListener
                        }

                        // Map each document in the snapshot to a Flashcard object
                        val flashcards = mutableListOf<Flashcard>()
                        for (document in flashcardsSnapshot!!) {
                            val flashcard = document.toObject(Flashcard::class.java)
                            flashcards.add(flashcard)
                        }

                        // Send the list of flashcards through the Flow
                        trySend(flashcards).isSuccess
                        Log.d(
                            LogTags.FIREBASE_SERVICES,
                            "fetchListOfFlashcardInPublicBox: Success!"
                        )
                    }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    /**
     * Fetches a list of flashcards from a specified box in the user's private collection in Firestore.
     *
     * This function sets up a real-time snapshot listener on the flashcard collection of
     * a specific box identified by 'boxUid' in the user's private collection. It emits an
     * updated list of flashcards through a Flow whenever there are changes in the collection.
     * The function also handles and logs any errors encountered during the listening process.
     *
     * @param boxUid The unique identifier of the private box whose flashcards are to be fetched.
     * @return A Flow emitting a list of flashcards.
     */
    override fun fetchListOfFlashcardInPrivateBox(boxUid: String): Flow<List<Flashcard>> {
        return callbackFlow {
            val listenerRegistration =
                firestore.collection(USERS).document(currentUser ?: "")
                    .collection(BOX).document(boxUid)
                    .collection(FLASHCARD)
                    .addSnapshotListener { flashcardsSnapshot, error ->
                        if (error != null) {
                            // Handling any errors encountered while listening to the snapshot
                            close(error)
                            Log.e(
                                LogTags.FIREBASE_SERVICES,
                                "fetchListOfFlashcardInPrivateBox: Error: $error"
                            )
                            return@addSnapshotListener
                        }

                        // Creating a list of Flashcard objects from the snapshot documents
                        val flashcards = mutableListOf<Flashcard>()
                        for (document in flashcardsSnapshot!!) {
                            val flashcard = document.toObject(Flashcard::class.java)
                            flashcards.add(flashcard)
                        }

                        // Emitting the list of flashcards through the Flow
                        trySend(flashcards).isSuccess
                        Log.d(
                            LogTags.FIREBASE_SERVICES,
                            "fetchListOfFlashcardInPrivateBox: Success!"
                        )
                    }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    /**
     * Fetches a list of flashcards from a specified box for a lesson.
     *
     * This function performs a one-time query to Firestore to retrieve all flashcards
     * from a specific box identified by 'boxUid'. The retrieved flashcards are emitted
     * through a Flow. The function includes error handling and logs the outcomes of the query.
     *
     * @param boxUid The unique identifier of the box from which flashcards are to be fetched.
     * @return A Flow emitting a list of flashcards.
     */
    override fun fetchListOfFlashcardInLesson(boxUid: String): Flow<List<Flashcard>> {
        return callbackFlow {
            // Perform a one-time query to Firestore to fetch flashcards
            firestore.collection(USERS).document(currentUser ?: "")
                .collection(BOX).document(boxUid)
                .collection(FLASHCARD)
                .get()
                .addOnSuccessListener { flashcardsSnapshot ->
                    // Map the retrieved documents to Flashcard objects
                    val flashcards = flashcardsSnapshot.documents.mapNotNull { document ->
                        document.toObject(Flashcard::class.java)
                    }
                    // Emit the list of flashcards through the Flow
                    trySend(flashcards).isSuccess
                    Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcardInLesson: Success!")
                }
                .addOnFailureListener { error ->
                    // Handle any errors encountered during the query
                    close(error)
                    Log.e(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcardInLesson: Error: $error")
                }

            awaitClose { }
        }
    }

    /**
     * Fetches a list of flashcards from the user's 'repeat' collection in Firestore.
     *
     * This function performs a one-time query to Firestore to retrieve all flashcards
     * in the 'repeat' collection of the current user. The results are emitted as a list
     * of Flashcard objects through a Flow. The function operates on the I/O dispatcher
     * to handle the network request appropriately.
     *
     * @return A Flow emitting a list of Flashcard objects from the 'repeat' collection.
     */
    override fun fetchListOfFlashcardInRepeat(): Flow<List<Flashcard>> {
        return flow {
            // Perform a one-time query to Firestore to fetch flashcards from the 'repeat' collection
            val flashcardsSnapshot =
                firestore.collection(USERS).document(currentUser ?: "")
                    .collection(REPEAT)
                    .get()
                    .await()

            // Map the retrieved documents to Flashcard objects
            val flashcards = flashcardsSnapshot.documents.mapNotNull { document ->
                document.toObject(Flashcard::class.java)
            }

            // Emit the list of flashcards through the Flow
            emit(flashcards)
            Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcardInRepeat: Success!")
        }
            // Specify that the flow should operate on the I/O dispatcher
            .flowOn(Dispatchers.IO)
    }

    /**
     * Adds eligible flashcards to the 'repeat' section for the current user.
     *
     * This function iterates through all the 'box' documents of the current user. For each box,
     * it retrieves all flashcards and checks if they meet the criteria to be added to the 'repeat' section
     * (e.g., based on the 'nextStudyDate'). Eligible flashcards are then added to the 'repeat' section.
     */
    override fun addFlashcardsToRepeatSection() {
        if (currentUser != null) {
            // Query all 'box' documents of the current user
            firestore.collection(USERS).document(currentUser)
                .collection(BOX)
                .get()
                .addOnSuccessListener { boxSnapshot ->
                    for (boxDocument in boxSnapshot.documents) {
                        // Iterating through each 'box' document
                        val boxID = boxDocument.id

                        // Query all flashcards within the current box
                        firestore.collection(USERS).document(currentUser)
                            .collection(BOX).document(boxID)
                            .collection(FLASHCARD)
                            .get()
                            .addOnSuccessListener { flashcardSnapshot ->
                                for (flashcardDocument in flashcardSnapshot.documents) {
                                    // Iterating through each flashcard in the box
                                    val flashcard =
                                        flashcardDocument.toObject(Flashcard::class.java)

                                    // Check if the flashcard meets the criteria to be added to 'repeat'
                                    if (flashcard?.nextStudyDate != null && flashcard.nextStudyDate!! <= currentTime) {
                                        // Add the flashcard to the 'repeat' section
                                        addFlashcardToRepeat(flashcard)

                                        Log.d(
                                            LogTags.FIREBASE_SERVICES,
                                            "fetchRepeatFlashcards: ${flashcard.word}"
                                        )
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e(
                                    LogTags.FIREBASE_SERVICES,
                                    "fetchRepeatFlashcards: $exception"
                                )
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(LogTags.FIREBASE_SERVICES, "fetchRepeatFlashcards: $exception")
                }
        }
    }

    /**
     * Adds a flashcard to the 'repeat' collection for the current user if it's not already present.
     *
     * This function first checks if the specified flashcard already exists in the 'repeat'
     * collection based on its UID. If it doesn't exist, the flashcard is added to the collection.
     * The function logs the outcome of each operation.
     *
     * @param flashcard The flashcard to be added to the 'repeat' collection.
     */
    private fun addFlashcardToRepeat(flashcard: Flashcard) {
        // Check if the flashcard already exists in the 'repeat' collection
        firestore.collection(USERS).document(currentUser ?: "")
            .collection(REPEAT)
            .whereEqualTo("uid", flashcard.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // If the flashcard does not exist, add it to the 'repeat' collection
                    firestore.collection(USERS).document(currentUser ?: "")
                        .collection(REPEAT)
                        .document(flashcard.uid!!)
                        .set(flashcard)
                        .addOnSuccessListener { documentReference ->
                            Log.d(
                                LogTags.FIREBASE_SERVICES,
                                "addFlashcardToRepeatSection: Successfully added flashcard: $documentReference"
                            )
                        }
                        .addOnFailureListener { exception ->
                            Log.e(
                                LogTags.FIREBASE_SERVICES,
                                "addFlashcardToRepeatSection: $exception"
                            )
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(LogTags.FIREBASE_SERVICES, "addFlashcardToRepeatSection: $exception")
            }
    }

    /**
     * Deletes a specified flashcard from the 'repeat' section of the user's collection in Firestore.
     *
     * This function removes a flashcard, identified by 'flashcardUid', from the 'repeat' collection
     * within the current user's Firestore documents. The deletion is a straightforward Firestore operation
     * and doesn't include explicit error handling or confirmation logging.
     *
     * @param flashcardUid The unique identifier of the flashcard to be deleted from the 'repeat' section.
     */
    override fun deleteFlashcardFromRepeatSection(flashcardUid: String) {
        // Reference to the Firestore document of the specified flashcard in the 'repeat' collection
        firestore.collection(USERS).document(currentUser ?: "")
            .collection(REPEAT).document(flashcardUid)
            .delete() // Perform the deletion of the flashcard document
            .addOnSuccessListener {
                Log.d(
                    LogTags.FIREBASE_SERVICES,
                    "deleteFlashcardFromRepeatSection: Successfully deleted flashcard with UID: $flashcardUid"
                )
            }
            .addOnFailureListener { exception ->
                Log.e(
                    LogTags.FIREBASE_SERVICES,
                    "deleteFlashcardFromRepeatSection: Error deleting flashcard with UID: $flashcardUid: $exception"
                )
            }
    }

    override fun fetchListOfStory(): Flow<Category> {

            return callbackFlow {
                val categories = listOf("criminal", "drama", "comedy") // Dodaj swoje kategorie
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
            flow: SendChannel<Category>
        ): ListenerRegistration {
            val currentCategory = categories[currentIndex.get()]

            return firestore.collection(STORY).document("yWhYIeotoTamzjd60yf9")
                .collection(currentCategory)
                .addSnapshotListener { querySnapshot, error ->

                    // Handle errors
                    if (error != null) {
                        flow.close(error)
                        Log.e(LogTags.FIREBASE_SERVICES, "fetchListOfStory: Error: $error")
                        return@addSnapshotListener
                    }

                    // Map documents to Story objects
                    val stories = querySnapshot?.documents?.mapNotNull {
                        it.toObject(Story::class.java)
                    } ?: mutableListOf()

                    // Create Category object and send it through the Flow
                    val category = Category(currentCategory, stories)
                    flow.trySend(category).isSuccess
                    Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfStory for $currentCategory: Success!")

                    // Increase index to fetch the next category
                    val nextIndex = currentIndex.incrementAndGet()

                    // If there are more categories, fetch the next one
                    if (nextIndex < categories.size) {
                        fetchNextCategory(categories, currentIndex, flow)
                    }
                }
        }








//        return callbackFlow {
//            val listenerRegistration = firestore.collection(STORY).document("yWhYIeotoTamzjd60yf9")
//                .collection("criminal")
//                .addSnapshotListener { querySnapshot, error ->
//
//                    // Handle any errors that might occur during snapshot listening
//                    if (error != null) {
//                        close(error)
//                        Log.e(LogTags.FIREBASE_SERVICES, "fetchListOfStory: Error: $error")
//                        return@addSnapshotListener
//                    }
//
//                    // Map each document in the snapshot to a Box object and send the list through the Flow
//                    val tempList = querySnapshot?.documents?.mapNotNull {
//                        it.toObject(Story::class.java)
//                    } ?: mutableListOf()
//
//                    trySend(tempList).isSuccess
//                    Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfStory: Success!")
//                }
//
//            // Ensure the removal of the snapshot listener when the Flow is closed or cancelled
//            awaitClose {
//                listenerRegistration.remove()
//            }
//        }
//    }

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
                val chaptersList = storyData["text"] as List<Map<String, Any>>?

                // Sprawdź, czy masz listę rozdziałów
                val chapters = chaptersList?.map { chapterMap ->
                    TextContent(
                        chapter = chapterMap["chapter"].toString() as String?,
                        content = chapterMap["content"].toString() as String?
                    )
                }
                    ?: emptyList()  // Pusta lista, jeśli nie ma rozdziałów

                Story(title, chapters, storyUid)
            } else {
                Log.e(LogTags.FIREBASE_SERVICES, "fetchStory: data is empty!")
                null  // Jeśli dane są puste, zwróć null
            }
        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "fetchStory: $e")
            null
        }
    }
}