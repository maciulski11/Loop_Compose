package com.example.loop_new.data.firebase

import android.util.Log
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
        const val FIREBASE_SERVICES = "FIREBASE_SERVICES:"
    }

    override fun addBox(box: Box) {
        val uid = UUID.randomUUID().toString()
        val data = Box(box.name, box.describe, uid)

        try {
            firestore.collection(BOX).document(uid).set(data)
            Log.d(FIREBASE_SERVICES, "Fun addBox: correct addition of box")
        } catch (e: Exception) {
            Log.d(FIREBASE_SERVICES, "Fun addBox: $e")
            throw e
        }
    }

    override fun addFlashcard(flashcard: Flashcard, boxUid: String) {
        val uid = UUID.randomUUID().toString()
        val data = flashcard.copy(uid = uid)

        try {
            firestore.collection(BOX).document(boxUid).collection(FLASHCARD).document(uid).set(data)
            Log.d(FIREBASE_SERVICES, "Fun addFlashcard: correct addition of flashcard")

        } catch (e: Exception) {
            Log.d(FIREBASE_SERVICES, "Fun addFlashcard: $e")
            throw e
        }
    }

    override fun deleteFlashcard(boxUid: String, flashcardUid: String) {
        try {
            firestore.collection(BOX).document(boxUid)
                .collection(FLASHCARD).document(flashcardUid)
                .delete()

        } catch (e: Exception) {
            Log.d(FIREBASE_SERVICES, "Fun deleteFlashcard: $e")
            throw e
        }
    }

    override fun fetchListOfBox(): Flow<List<Box>> {
        return callbackFlow {
            val collection = firestore.collection(BOX)
            val listenerRegistration = collection.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val tempList = mutableListOf<Box>()
                for (document in querySnapshot!!) {
                    val box = document.toObject(Box::class.java)
                    tempList.add(box)
                }

                trySend(tempList).isSuccess
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
                    return@addSnapshotListener
                }

                val flashcards = mutableListOf<Flashcard>()
                for (document in flashcardsSnapshot!!) {
                    val flashcard = document.toObject(Flashcard::class.java)
                    flashcards.add(flashcard)
                }

                trySend(flashcards).isSuccess
            }

            awaitClose {
                listenerRegistration.remove()
            }
        }
    }
}