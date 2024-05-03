package com.example.loop_new.presentation.viewModel

import androidx.lifecycle.LiveData
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.room.LoopDao
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class RoomService(private val loopDao: LoopDao): RoomService {

    override suspend fun insertBox(box: Box) {
        loopDao.insertBox(box)
    }

    override suspend fun deleteBox(uid: String) {
        loopDao.deleteBox(uid)
    }

    override fun fetchBoxes(): Flow<List<Box>> {
        return loopDao.fetchBoxes()
    }
}