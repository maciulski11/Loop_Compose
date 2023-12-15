package com.example.loop_new.di

import com.example.loop_new.data.Service
import com.example.loop_new.data.api.DictionaryService
import com.example.loop_new.data.api.TranslateService
import com.example.loop_new.data.firebase.FirebaseService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
class DependencyProvider() {

    // Firebase
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val firebaseService: FirebaseService = FirebaseService(firebaseFirestore)

    // Translate Api
    val translateService: TranslateService = TranslateService()

    // Dictionary Api
    val dictionaryService: DictionaryService = DictionaryService()

    // Service
    val service: Service = Service()

}