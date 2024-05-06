package com.example.loop_new.presentation.viewModel

import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.room.BoxDao
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.flow.Flow

class RoomService(private val boxDao: BoxDao): RoomService {

    override suspend fun insertBox(box: Box) {
        boxDao.insertBox(box)
    }

    override suspend fun deleteBox(uid: String) {
        boxDao.deleteBox(uid)
    }

    override fun fetchBoxes(): Flow<List<Box>> {
        return boxDao.fetchBoxes()
    }
}