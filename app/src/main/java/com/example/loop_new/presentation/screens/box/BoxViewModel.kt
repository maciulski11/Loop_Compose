package com.example.loop_new.presentation.screens.box

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.InterfaceFirebaseService
import kotlinx.coroutines.launch

class BoxViewModel(private val interfaceFirebaseServices: InterfaceFirebaseService, boxUid: String) : ViewModel() {

    val flashcardList: MutableState<List<Flashcard>?> = mutableStateOf(null)

    init {
        fetchListOfFlashcard(boxUid)
    }

    private fun fetchListOfFlashcard(boxUid: String) {
        viewModelScope.launch {
            val flashcardsFlow = interfaceFirebaseServices.fetchListOfFlashcard(boxUid)
            flashcardsFlow.collect {
                flashcardList.value = it
            }
        }
    }

    fun deleteFlashcard(boxUid: String, flashcardUid: String) {
        interfaceFirebaseServices.deleteFlashcard(boxUid, flashcardUid)
    }

}