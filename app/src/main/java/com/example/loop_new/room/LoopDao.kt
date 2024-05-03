package com.example.loop_new.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.loop_new.domain.model.firebase.Box
import kotlinx.coroutines.flow.Flow

@Dao
interface LoopDao: RoomService {

    @Insert
    override suspend fun insertBox(box: Box)

    @Query("DELETE FROM box WHERE uid = :uid")
    override suspend fun deleteBox(uid: String)

    @Query("SELECT * FROM box")
    override fun fetchBoxes(): Flow<List<Box>>
}