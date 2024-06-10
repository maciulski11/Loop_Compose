package com.example.loop_new.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loop_new.domain.model.firebase.Favorite
import com.example.loop_new.domain.model.firebase.TextContentRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteStoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTextContent(textContentRoom: TextContentRoom)

    @Query("SELECT * FROM story WHERE uid = :uid LIMIT 1")
    suspend fun getFavoriteByUid(uid: String): Favorite?

    @Query("SELECT * FROM story")
    fun fetchStories(): Flow<List<Favorite>>

    @Query("DELETE FROM story WHERE id = :storyId")
    suspend fun deleteBox(storyId: Int)
}