package com.example.loop_new.room

import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.BoxWithFlashcards
import com.example.loop_new.domain.model.firebase.Flashcard
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

    suspend fun updateFlashCardKnowledgeLevel(flashcardId: Int, newKnowledgeLevel: String, timeToRepeat: Int) {

        val flashcard = flashcardDao.getFlashcardById(flashcardId)
        flashcard?.let {
            val currentDate = Date()
            val nextStudyDate = Calendar.getInstance().apply {
                time = currentDate
                add(Calendar.HOUR, timeToRepeat)
            }.time

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val lastStudiedDateStr = dateFormat.format(currentDate)
            val nextStudyDateStr = dateFormat.format(nextStudyDate)

            val updatedFlashcard = it.copy(
                knowledgeLevel = newKnowledgeLevel,
                lastStudiedDate = lastStudiedDateStr,
                nextStudyDate = nextStudyDateStr
            )

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