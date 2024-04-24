package com.example.loop_new.presentation.viewModel

import androidx.lifecycle.LiveData
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.room.LoopDao
import com.example.loop_new.room.RoomService
import java.util.UUID

class RoomService(private val loopDao: LoopDao): RoomService {
    override suspend fun insertBox(box: Box) {
        val uid = UUID.randomUUID().toString()
        val data = Box(
            name = box.name,
            describe = box.describe,
            uid = uid,
            color1 = box.color1,
            color2 = box.color2,
            color3 = box.color3,
            permissionToEdit = true
        )

        loopDao.insertBox(data)
    }

    override suspend fun deleteBox(uid: String) {
        loopDao.deleteBox(uid)
    }

    override fun fetchBoxes(): LiveData<List<Box>> {
        TODO("Not yet implemented")
    }
}