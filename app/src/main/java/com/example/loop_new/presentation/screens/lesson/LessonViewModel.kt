package com.example.loop_new.presentation.screens.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.InterfaceFirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LessonViewModel(private val interfaceFirebaseServices: InterfaceFirebaseService, boxUid: String): ViewModel() {

    private val flashcardList = MutableStateFlow<List<Flashcard>>(emptyList())
    private val _currentFlashcard = MutableStateFlow<Flashcard?>(null)
    val currentFlashcard: StateFlow<Flashcard?> = _currentFlashcard
    val isDataLoaded = MutableStateFlow(false)

    init {
        fetchListOfFlashcard(boxUid)
    }

    private fun fetchListOfFlashcard(boxUid: String) {
        viewModelScope.launch {
            interfaceFirebaseServices.fetchListOfFlashcard(boxUid).collect { newFlashcardList ->
                flashcardList.value = newFlashcardList
                _currentFlashcard.value = newFlashcardList.firstOrNull()
                isDataLoaded.value = true
            }
        }
    }

    fun moveToNextFlashcard() {
        val currentList = flashcardList.value
        val currentIndex = currentList.indexOf(_currentFlashcard.value)
        if (currentIndex >= 0 && currentIndex < currentList.size - 1) {
            _currentFlashcard.value = currentList[currentIndex + 1]
        }
    }
}




//class LessonViewModel(private val interfaceFirebaseServices: InterfaceFirebaseService, boxUid: String): ViewModel() {
//
//    val flashcardList: MutableState<List<Flashcard>?> = mutableStateOf(null)
//
//    init {
//        fetchListOfFlashcard(boxUid)
//    }
//
//    private fun fetchListOfFlashcard(boxUid: String) {
//        viewModelScope.launch {
//            val flashcardsFlow = interfaceFirebaseServices.fetchListOfFlashcard(boxUid)
//            flashcardsFlow.collect {
//                flashcardList.value = it
//            }
//        }
//    }
//
//}