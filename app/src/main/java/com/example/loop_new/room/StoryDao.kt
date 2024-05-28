package com.example.loop_new.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.loop_new.domain.model.firebase.Story

@Dao
interface StoryDao {

    @Insert
    suspend fun addStory(story: Story)

    @Query("DELETE FROM story WHERE id = :storyId")
    suspend fun deleteBox(storyId: String)
}