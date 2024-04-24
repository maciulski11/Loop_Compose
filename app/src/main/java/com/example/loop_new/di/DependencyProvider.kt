package com.example.loop_new.di

import android.app.Application
import androidx.room.RoomDatabase
import com.example.loop_new.data.Service
import com.example.loop_new.data.api.DictionaryService
import com.example.loop_new.data.api.TranslateService
import com.example.loop_new.data.firebase.FirebaseService
import com.example.loop_new.presentation.viewModel.RoomService
import com.example.loop_new.room.LoopDao
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
    private val loopDao: LoopDao = loopDatabase.loopDao()
    val roomService: RoomService = RoomService(loopDao)

    // Service
    val service: Service = Service()

}