package com.example.loop_new.domain.services

import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.Box
import kotlinx.coroutines.flow.Flow

interface InterfaceRepository {

    fun addBox(box: Box)

    fun addFlashcard(flashcard: Flashcard, boxUid: String)

    fun fetchListOfBox(): Flow<List<Box>>

    fun fetchListOfFlashcard(boxUid: String): Flow<List<Flashcard>>

}

class SampleDataRepository {



}