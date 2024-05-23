package com.example.loop_new.room

import android.util.Log
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.BoxWithFlashcards
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.RepeatSection
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class RoomService(
    private val boxDao: BoxDao,
    private var flashcardDao: FlashcardDao,
    private var repeatSectionDao: RepeatSectionDao,
) {

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

    suspend fun updateFlashCardKnowledgeLevel(
        flashcardId: Int,
        newKnowledgeLevel: String,
        timeToRepeat: Int,
    ) {
        val flashcard = flashcardDao.getFlashcardById(flashcardId)
        flashcard?.let {
            val currentTimeMillis = System.currentTimeMillis()

            // Dodaj czas do powtórzenia do bieżącego czasu, aby uzyskać czas następnej nauki
            val nextStudyTimeMillis =
                currentTimeMillis + (timeToRepeat * 3600 * 1000) // Dodaj godziny do bieżącego czasu w milisekundach

            // Aktualizuj Flashcard z nowym poziomem wiedzy i datami
            val updatedFlashcard = it.copy(
                knowledgeLevel = newKnowledgeLevel,
                lastStudiedDate = currentTimeMillis,
                nextStudyDate = nextStudyTimeMillis
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

    suspend fun updateRepeatSection(currentTime: String) {
        // Convert the current time to milliseconds
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(currentTime)
        val currentTimeMillis = currentDate?.time ?: 0

        Log.d("RepeatSection", "Converted current time to milliseconds: $currentTimeMillis")

        val repeatSectionCount = repeatSectionDao.getRepeatSectionCount()
        Log.d("RepeatSection", "Repeat section count: $repeatSectionCount")

        if (repeatSectionCount < 30) {
            val neededFlashcards = 30 - repeatSectionCount
            // Fetch flashcards for repetition
            val flashcardsForRepeat = flashcardDao.getFlashcardsForRepeat(currentTimeMillis, neededFlashcards)
            Log.d("RepeatSection", "Number of flashcards for repeat: ${flashcardsForRepeat.size}")

            if (flashcardsForRepeat.isNotEmpty()) {
                // Creating revision objects based on a flashcard
                val repeatSectionFlashcards = flashcardsForRepeat.map { flashcard ->
                    RepeatSection(
                        word = flashcard.word,
                        translate = flashcard.translate,
                        meaning = flashcard.meaning,
                        example = flashcard.example,
                        partOfSpeech = flashcard.partOfSpeech,
                        pronunciation = flashcard.pronunciation,
                        audioUrl = flashcard.audioUrl,
                        boxId = flashcard.boxId,
                        flashcardId = flashcard.idFlashcard
                    )
                }

                Log.d("RepeatSection", "Inserting flashcards into repeat section database")

                // Insert flashcards to repeat section
                repeatSectionDao.insertFlashcards(*repeatSectionFlashcards.toTypedArray())

            } else {
                Log.d("RepeatSection", "No flashcards for repeat found")
            }
        } else {
            Log.d("RepeatSection", "Repeat section already has 30 flashcards")
        }
    }

    suspend fun checkRepeatCollectionWhetherIsEmpty(): Boolean {
        return try {
            // Asynchronously retrieve the count of records in the repeat section
            val repeatSectionCount = repeatSectionDao.getRepeatSectionCount()

            // Return true if the count is 0, indicating that the collection is empty
            repeatSectionCount == 0
        } catch (exception: Exception) {
            Log.e("checkRepeatCollectionWhetherIsEmpty exception:", "$exception")
            false // Return false in case of an exception
        }
    }
}