package com.example.loop_new.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.loop_new.domain.model.firebase.Favorite
import com.example.loop_new.domain.model.firebase.FavoriteStoryWithChapters
import com.example.loop_new.domain.model.firebase.TextContentRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteStoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTextContent(textContentRoom: TextContentRoom)

    @Query("UPDATE story SET favorite = :isFavorite WHERE uid = :storyUid")
    suspend fun updateFavoriteStatus(storyUid: String, isFavorite: Boolean)

    @Query("SELECT * FROM story WHERE uid = :uid LIMIT 1")
    suspend fun getFavoriteByUid(uid: String): Favorite?

    @Query("SELECT * FROM story WHERE uid = :storyUid")
    suspend fun fetchStory(storyUid: String) : Favorite

    @Query("SELECT * FROM story ORDER BY favorite DESC")
    fun fetchStories(): Flow<List<Favorite>>

    @Transaction
    @Query("SELECT * FROM story WHERE uid = :storyUid")
    fun fetchFavoriteStoryWithChapters(storyUid: String): Flow<FavoriteStoryWithChapters?>

    @Transaction
    suspend fun deleteStoryWithChapters(storyId: Int) {
        deleteChaptersByStoryId(storyId)
        deleteStory(storyId)
    }

    @Query("DELETE FROM story WHERE id = :storyId")
    suspend fun deleteStory(storyId: Int)

    @Query("DELETE FROM text_content WHERE storyId = :storyId")
    suspend fun deleteChaptersByStoryId(storyId: Int)
}