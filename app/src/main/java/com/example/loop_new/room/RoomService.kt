package com.example.loop_new.room

import androidx.lifecycle.LiveData
import com.example.loop_new.domain.model.firebase.Box

interface RoomService {

    suspend fun insertBox(box: Box)

    suspend fun deleteBox(uid: String)

    fun fetchBoxes(): LiveData<List<Box>>
}