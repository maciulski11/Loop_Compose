package com.example.loop_new.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.loop_new.domain.model.firebase.Box
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxDao{

    @Insert
    suspend fun insertBox(box: Box)

    @Query("DELETE FROM box WHERE uid = :uid")
    suspend fun deleteBox(uid: String)

    @Query("SELECT * FROM box")
    fun fetchBoxes(): Flow<List<Box>>
}