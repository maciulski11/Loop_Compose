package com.example.loop_new.presentation.box

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.Flashcard
import com.example.loop_new.domain.repository.InterfaceRepository
import kotlinx.coroutines.launch

class BoxViewModel(private val interfaceRepository: InterfaceRepository,  boxUid: String) : ViewModel() {

    val flashcardList: MutableState<List<Flashcard>?> = mutableStateOf(null)

    init {
        fetchListOfFlashcard(boxUid)
    }

    private fun fetchListOfFlashcard(boxUid: String) {
        viewModelScope.launch {
            val flashcardsFlow = interfaceRepository.fetchListOfFlashcard(boxUid)
            flashcardsFlow.collect {
                flashcardList.value = it
            }
        }
    }
}