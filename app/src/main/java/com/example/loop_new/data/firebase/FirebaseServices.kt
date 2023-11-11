package com.example.loop_new.data.firebase

import android.util.Log
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.InterfaceFirebaseService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class FirebaseServices(private val firestore: FirebaseFirestore): InterfaceFirebaseService {

    companion object {
        const val BOX = "box"
        const val FLASHCARD = "flashcard"
    }

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

    override fun addFlashcard(flashcard: Flashcard, boxUid: String) {
        val uid = UUID.randomUUID().toString()
        val data = flashcard.copy(uid = uid)

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
        return callbackFlow {
            val collection = firestore.collection(BOX)
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

    override fun fetchListOfFlashcard(boxUid: String): Flow<List<Flashcard>> {
        val userDocRef = firestore.collection(BOX).document(boxUid)
        val flashcardsCollectionRef = userDocRef.collection(FLASHCARD)

        return callbackFlow {
            val listenerRegistration = flashcardsCollectionRef.addSnapshotListener { flashcardsSnapshot, error ->
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
}