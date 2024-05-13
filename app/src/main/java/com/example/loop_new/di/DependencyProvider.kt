package com.example.loop_new.di

import android.app.Application
import com.example.loop_new.data.Service
import com.example.loop_new.data.api.DictionaryService
import com.example.loop_new.data.api.TranslateService
import com.example.loop_new.data.firebase.FirebaseService
import com.example.loop_new.room.RoomService
import com.example.loop_new.room.BoxDao
import com.example.loop_new.room.FlashcardDao
import com.example.loop_new.room.LoopDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
class DependencyProvider(application: Application) {

    // Firebase
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val firebaseService: FirebaseService = FirebaseService(firebaseFirestore)

    // Translate Api
    val translateService: TranslateService = TranslateService()

    // Dictionary Api
    val dictionaryService: DictionaryService = DictionaryService()

    // Room Database
    private val loopDatabase: LoopDatabase = LoopDatabase.getInstance(application)
    private val boxDao: BoxDao = loopDatabase.boxDao()
    private val flashcardDao: FlashcardDao = loopDatabase.flashcardDao()
    val roomService: RoomService = RoomService(boxDao, flashcardDao)

    // Service
    val service: Service = Service()

}