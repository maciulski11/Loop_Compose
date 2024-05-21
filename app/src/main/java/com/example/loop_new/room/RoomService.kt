package com.example.loop_new.room

import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.BoxWithFlashcards
import com.example.loop_new.domain.model.firebase.Flashcard
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class RoomService(private val boxDao: BoxDao, private var flashcardDao: FlashcardDao) {

    suspend fun insertBox(box: Box) {
        boxDao.insertBox(box)
    }

    suspend fun deleteBox(uid: String) {
        boxDao.deleteBox(uid)
    }

    fun fetchBoxes(): Flow<List<Box>> {
        return boxDao.fetchBoxes()
    }

    suspend fun insertFlashCard(flashcard: Flashcard) {
        val uid = UUID.randomUUID().toString()
        val data = flashcard.copy(uid = uid)

        flashcardDao.insertFlashCard(data)
    }

    suspend fun updateFlashCardKnowledgeLevel(flashcardId: Int, newKnowledgeLevel: String) {
        val flashcard = flashcardDao.getFlashcardById(flashcardId)
        flashcard?.let {
            // Zaktualizowanie poziomu wiedzy
            val updatedFlashcard = it.copy(knowledgeLevel = newKnowledgeLevel)
            flashcardDao.updateFlashCard(updatedFlashcard)
        }
    }

    suspend fun deleteFlashcard(uid: String) {
        flashcardDao.deleteFlashCard(uid)
    }

    fun fetchFlashcardsById(boxId: Int): Flow<List<Flashcard>> {
        return flashcardDao.fetchFlashcardsById(boxId)
    }

    suspend fun fetchBoxWithFlashcards(boxId: Int): BoxWithFlashcards {
        return boxDao.fetchBoxWithFlashcards(boxId)
    }
}