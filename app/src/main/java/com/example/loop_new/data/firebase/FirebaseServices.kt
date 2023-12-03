package com.example.loop_new.data.firebase

import android.util.Log
import com.example.loop_new.FlashcardFields
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import com.example.loop_new.domain.services.InterfaceFirebaseService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.UUID

class FirebaseServices(private val firestore: FirebaseFirestore) : InterfaceFirebaseService {

    companion object {
        const val BOX = "box"
        const val FLASHCARD = "flashcard"
        const val REPEAT = "repeat"
    }

    private val currentTime = Timestamp.now()

    // Create a calendar with date
    private val calendar = Calendar.getInstance()

    override fun addBox(box: Box) {
        val uid = UUID.randomUUID().toString()
        val data = Box(box.name, box.describe, uid)

        try {
            firestore.collection(BOX).document(uid).set(data)
            Log.d(LogTags.FIREBASE_SERVICES, "addBox: Correct addition of box")
        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "addBox: Error: $e")
            throw e
        }
    }

    private fun setKnowledgeLevelOfFlashcard(
        boxUid: String,
        flashcardUid: String,
        updateData: Map<String, *>
    ) {
        try {
            firestore.collection(BOX).document(boxUid)
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

    override fun updateFlashcardToKnow(boxUid: String, flashcardUid: String) {

        calendar.time = currentTime.toDate()
        calendar.add(Calendar.DAY_OF_MONTH, 4)
        val newTimestamp = Timestamp(calendar.time)

        val updateData = mapOf(
            FlashcardFields.KNOWLEDGE_LEVEL to KnowledgeLevel.KNOW.value,
            FlashcardFields.LAST_STUDIED_DATE to currentTime.toDate(),
            FlashcardFields.NEXT_STUDY_DATE to newTimestamp
        )

        setKnowledgeLevelOfFlashcard(boxUid, flashcardUid, updateData)
    }

    override fun updateFlashcardToSomewhatKnow(boxUid: String, flashcardUid: String) {

        calendar.time = currentTime.toDate()
        calendar.add(Calendar.DAY_OF_MONTH, 2)
        val newTimestamp = Timestamp(calendar.time)

        val updateData = mapOf(
            FlashcardFields.KNOWLEDGE_LEVEL to KnowledgeLevel.SOMEWHAT_KNOW.value,
            FlashcardFields.LAST_STUDIED_DATE to currentTime.toDate(),
            FlashcardFields.NEXT_STUDY_DATE to newTimestamp
        )

        setKnowledgeLevelOfFlashcard(boxUid, flashcardUid, updateData)
    }

    override fun updateFlashcardToDoNotKnow(boxUid: String, flashcardUid: String) {

        calendar.time = currentTime.toDate()
        calendar.add(Calendar.HOUR_OF_DAY, 12)
        val newTimestamp = Timestamp(calendar.time)

        val updateData = mapOf(
            FlashcardFields.KNOWLEDGE_LEVEL to KnowledgeLevel.DO_NOT_KNOW.value,
            FlashcardFields.LAST_STUDIED_DATE to currentTime.toDate(),
            FlashcardFields.NEXT_STUDY_DATE to newTimestamp
        )

        setKnowledgeLevelOfFlashcard(boxUid, flashcardUid, updateData)
    }

    override fun addFlashcard(flashcard: Flashcard, boxUid: String) {
        val uid = UUID.randomUUID().toString()
        val data = flashcard.copy(uid = uid, boxUid = boxUid)

        try {
            firestore.collection(BOX).document(boxUid).collection(FLASHCARD).document(uid).set(data)
            Log.d(LogTags.FIREBASE_SERVICES, "addFlashcard: Correct addition of flashcard")

        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "addFlashcard: Error: $e")
            throw e
        }
    }

    override fun deleteFlashcard(boxUid: String, flashcardUid: String) {
        try {
            firestore.collection(BOX).document(boxUid)
                .collection(FLASHCARD).document(flashcardUid)
                .delete()

            Log.d(LogTags.FIREBASE_SERVICES, "deleteFlashcard: Success!")

        } catch (e: Exception) {
            Log.e(LogTags.FIREBASE_SERVICES, "deleteFlashcard: Error: $e")
            throw e
        }
    }

    override fun fetchListOfBox(): Flow<List<Box>> {
        val collection = firestore.collection(BOX)

        return callbackFlow {
            val listenerRegistration = collection.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    close(error)
                    Log.e(LogTags.FIREBASE_SERVICES, "fetchListOfBox: Error: $error")
                    return@addSnapshotListener
                }

                val tempList = mutableListOf<Box>()
                for (document in querySnapshot!!) {
                    val box = document.toObject(Box::class.java)
                    tempList.add(box)
                }

                trySend(tempList).isSuccess
                Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfBox: Success!")
            }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override fun fetchListOfFlashcardInBox(boxUid: String): Flow<List<Flashcard>> {
        val flashcardsCollectionRef =
            firestore.collection(BOX).document(boxUid).collection(FLASHCARD)

        return callbackFlow {
            val listenerRegistration =
                flashcardsCollectionRef.addSnapshotListener { flashcardsSnapshot, error ->
                    if (error != null) {
                        close(error)
                        Log.e(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcard: Error: $error")
                        return@addSnapshotListener
                    }

                    val flashcards = mutableListOf<Flashcard>()
                    for (document in flashcardsSnapshot!!) {
                        val flashcard = document.toObject(Flashcard::class.java)
                        flashcards.add(flashcard)
                    }

                    trySend(flashcards).isSuccess
                    Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcard: Success!")
                }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override fun fetchListOfFlashcardInLesson(boxUid: String): Flow<List<Flashcard>> {
        val flashcardsCollectionRef =
            firestore.collection(BOX).document(boxUid).collection(FLASHCARD)

        return callbackFlow {
            // Wykonaj jednorazowe zapytanie do Firestore
            flashcardsCollectionRef.get()
                .addOnSuccessListener { flashcardsSnapshot ->
                    val flashcards = flashcardsSnapshot.documents.mapNotNull { document ->
                        document.toObject(Flashcard::class.java)
                    }
                    trySend(flashcards).isSuccess
                    Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcard: Success!")
                }
                .addOnFailureListener { error ->
                    close(error)
                    Log.e(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcard: Error: $error")
                }

            awaitClose { }
        }
    }

//    override fun fetchListOfFlashcardInRepeat(): Flow<List<Flashcard>> {
//        val repeatCollectionRef = firestore.collection(REPEAT)
//
//        return  callbackFlow {
//            // Wykonaj jednorazowe zapytanie do Firestore
//            repeatCollectionRef.get()
//                .addOnSuccessListener { flashcardsSnapshot ->
//                    val flashcards = flashcardsSnapshot.documents.mapNotNull { document ->
//                        document.toObject(Flashcard::class.java)
//                    }
//                    trySend(flashcards).isSuccess
//                    Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcard: Success!")
//                }
//                .addOnFailureListener { error ->
//                    close(error)
//                    Log.e(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcard: Error: $error")
//                }
//
//            awaitClose { }
//        }
//    }

    override fun fetchListOfFlashcardInRepeat(): Flow<List<Flashcard>> {
        val repeatCollectionRef = firestore.collection(REPEAT)

        return flow {
            // Wykonaj zapytanie do Firestore i przekonwertuj dane na listę fiszek
            val flashcardsSnapshot = repeatCollectionRef.get().await()
            val flashcards = flashcardsSnapshot.documents.mapNotNull { document ->
                document.toObject(Flashcard::class.java)
            }

            emit(flashcards) // Wyemituj listę fiszek
            Log.d(LogTags.FIREBASE_SERVICES, "fetchListOfFlashcardInRepeat: Success!")
        }.flowOn(Dispatchers.IO)
    }

    override fun addFlashcardsToRepeatSection() {
        firestore.collection(BOX)
            .get()
            .addOnSuccessListener { boxSnapshot ->
                for (boxDocument in boxSnapshot.documents) {
                    // Iteracja przez dokumenty "box" użytkownika
                    val boxID = boxDocument.id

                    firestore.collection(BOX).document(boxID)
                        .collection(FLASHCARD)
                        .get()
                        .addOnSuccessListener { flashcardSnapshot ->
                            for (flashcardDocument in flashcardSnapshot.documents) {
                                // Iteracja przez fiszki w danym boxie
                                val flashcard = flashcardDocument.toObject(Flashcard::class.java)

                                if (flashcard?.nextStudyDate != null && flashcard.nextStudyDate!! <= currentTime) {

                                    addFlashcardToRepeat(flashcard)

                                    Log.d(
                                        LogTags.FIREBASE_SERVICES,
                                        "fetchRepeatFlashcards: ${flashcard.word}"
                                    )
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e(LogTags.FIREBASE_SERVICES, "fetchRepeatFlashcards: $exception")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(LogTags.FIREBASE_SERVICES, "fetchRepeatFlashcards: $exception")
            }
    }

    private fun addFlashcardToRepeat(flashcard: Flashcard) {
        firestore.collection(REPEAT)
            .whereEqualTo("uid", flashcard.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Fiszka o danym uid nie istnieje, więc możesz ją dodać
                    firestore.collection(REPEAT)
                        .document(flashcard.uid!!)
                        .set(flashcard)
                        .addOnSuccessListener { documentReference ->
                            Log.d(
                                LogTags.FIREBASE_SERVICES,
                                "addFlashcardToRepeatSection: Successfully added flashcard to repeat collection!"
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
}