package com.example.loop_new.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loop_new.domain.model.firebase.RepeatSection

@Dao
interface RepeatSectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(vararg flashcards: RepeatSection)

//    @Query("DELETE FROM repeat_section WHERE flashcardId = :flashcardId")
//    suspend fun deleteFlashcardById(flashcardId: Int)

    @Query("SELECT COUNT(*) FROM repeat_section")
    suspend fun getRepeatSectionCount(): Int
}
