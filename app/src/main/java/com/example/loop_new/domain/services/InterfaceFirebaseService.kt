package com.example.loop_new.domain.services

import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import kotlinx.coroutines.flow.Flow

interface InterfaceFirebaseService {

    fun addBox(box: Box)

    fun addFlashcard(flashcard: Flashcard, boxUid: String)

    fun deleteFlashcard(boxUid: String, flashcardUid: String)

    fun fetchListOfBox(): Flow<List<Box>>

    fun fetchListOfFlashcardInLesson(boxUid: String): Flow<List<Flashcard>>

    fun fetchListOfFlashcardInBox(boxUid: String): Flow<List<Flashcard>>

    fun fetchRepeatFlashcards()

    fun updateFlashcardToKnow(boxUid: String, flashcardUid: String)

    fun updateFlashcardToSomewhatKnow(boxUid: String, flashcardUid: String)

    fun updateFlashcardToDoNotKnow(boxUid: String, flashcardUid: String)

}

class SampleDataRepository {



}