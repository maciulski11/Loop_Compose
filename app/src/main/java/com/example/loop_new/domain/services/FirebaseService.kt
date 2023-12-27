package com.example.loop_new.domain.services

import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.User
import kotlinx.coroutines.flow.Flow

interface FirebaseService {

    //TODO: do zmiany na ogolnie usera, chyba
    fun createNewGoogleUser()

    fun getSignedInUser(): User?

    fun addBoxToUserLearningSection(boxUid: String)

    fun createBoxInPrivateSection(box: Box)

    fun addFlashcardInPrivateSection(flashcard: Flashcard, boxUid: String)

    fun deleteFlashcard(boxUid: String, flashcardUid: String)

    fun fetchListOfPublicBox(): Flow<List<Box>>

    fun fetchListOfPrivateBox(): Flow<List<Box>>

    fun fetchListOfFlashcardInPublicBox(boxUid: String): Flow<List<Flashcard>>

    fun fetchListOfFlashcardInPrivateBox(boxUid: String): Flow<List<Flashcard>>

    fun fetchListOfFlashcardInLesson(boxUid: String): Flow<List<Flashcard>>

    fun fetchListOfFlashcardInRepeat(): Flow<List<Flashcard>>

    fun addFlashcardsToRepeatSection()

    fun deleteFlashcardFromRepeatSection(flashcardUid: String)

    fun updateFlashcardToKnow(boxUid: String, flashcardUid: String)

    fun updateFlashcardToSomewhatKnow(boxUid: String, flashcardUid: String)

    fun updateFlashcardToDoNotKnow(boxUid: String, flashcardUid: String)

    fun setupRepeatCollectionListener(onCollectionUpdate: (Boolean) -> Unit)

    suspend fun checkRepeatCollectionWhetherIsEmpty(): Boolean

}