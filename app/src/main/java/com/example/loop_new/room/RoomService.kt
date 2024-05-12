package com.example.loop_new.room

import com.example.loop_new.domain.model.firebase.Box
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

    fun updateFlashCard(flashcard: Flashcard) {
        TODO("Not yet implemented")
    }

    suspend fun deleteFlashCard(uid: String) {
        flashcardDao.deleteFlashCard(uid)
    }

    fun fetchFlashcardById(boxId: Int): Flow<List<Flashcard>> {
        return flashcardDao.fetchFlashcardById(boxId)
    }
}