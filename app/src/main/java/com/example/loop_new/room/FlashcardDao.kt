package com.example.loop_new.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.loop_new.domain.model.firebase.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Insert
    suspend fun insertFlashCard(flashcard: Flashcard)

    @Update
    suspend fun updateFlashCard(flashcard: Flashcard)

    @Query("DELETE FROM flashcard WHERE uid = :uid")
    suspend fun deleteFlashCard(uid: String)

    @Query("SELECT * FROM flashcard WHERE idFlashcard = :flashcardId")
    suspend fun getFlashcardById(flashcardId: Int): Flashcard?

    @Query("SELECT * FROM flashcard WHERE nextStudyDate <= :currentTime LIMIT :limit")
    suspend fun getFlashcardsForRepeat(currentTime: Long, limit: Int): List<Flashcard>

    // Query -> load note by id
    @Query("SELECT * FROM flashcard WHERE boxId = :boxId")
    fun fetchFlashcardsById(boxId: Int): Flow<List<Flashcard>>

}