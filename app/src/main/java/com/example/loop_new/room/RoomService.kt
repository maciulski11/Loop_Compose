package com.example.loop_new.room

import androidx.lifecycle.LiveData
import com.example.loop_new.domain.model.firebase.Box
import kotlinx.coroutines.flow.Flow

interface RoomService {

    suspend fun insertBox(box: Box)

    suspend fun deleteBox(uid: String)

    fun fetchBoxes(): Flow<List<Box>>
}