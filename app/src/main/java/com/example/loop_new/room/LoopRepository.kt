package com.example.loop_new.room

import com.example.loop_new.domain.model.firebase.Box

class LoopRepository(private var loopDao: LoopDao) {


    suspend fun insert(box: Box) {
        loopDao.insertBox(box)
    }
}