package com.example.loop_new.presentation.screens.flashcard_section.flashcard.priv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.presentation.viewModel.MainViewModel
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrivateFlashcardViewModel(
    private val roomService: RoomService,
    private val mainViewModel: MainViewModel,
    boxId: Int,
) : ViewModel() {

    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards

    init {
        fetchFlashcardById(boxId)
    }

    fun delete(uid: String) {
        viewModelScope.launch {
            roomService.deleteFlashCard(uid)
        }
    }

    private fun fetchFlashcardById(boxId: Int) {
        viewModelScope.launch {
            roomService.fetchFlashcardById(boxId).collect { flashcard ->
                _flashcards.value = flashcard
            }
        }
    }

    fun playAudioFromUrl(audioUrl: String) {
        mainViewModel.playAudioFromUrl(audioUrl)
    }
}