package com.example.loop_new.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Insert
    suspend fun insertFlashCard(flashcard: Flashcard)

    @Update
    fun updateFlashCard(flashcard: Flashcard)

    @Query("DELETE FROM flashcard WHERE uid = :uid")
    suspend fun deleteFlashCard(uid: String)

    // Query -> load note by id
    @Query("SELECT * FROM flashcard WHERE boxId = :boxId")
    fun fetchFlashcardById(boxId: Int): Flow<List<Flashcard>>
}