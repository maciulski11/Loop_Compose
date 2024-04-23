package com.example.loop_new.room

import androidx.room.Dao
import androidx.room.Insert
import com.example.loop_new.domain.model.firebase.Box

@Dao
interface LoopDao {

    @Insert
    suspend fun insertBox(vararg box: Box)
}