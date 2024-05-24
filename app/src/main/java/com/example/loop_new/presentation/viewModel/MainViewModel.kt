package com.example.loop_new.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import com.example.loop_new.domain.services.GoogleAuthService
import com.example.loop_new.domain.services.Service
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val service: Service,
    private val googleAuthService: GoogleAuthService,
    private val roomService: RoomService,
) : ViewModel() {

    private val _isSignedOut = MutableStateFlow(false)
    val isSignedOut = _isSignedOut.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            googleAuthService.signOut()
            _isSignedOut.value = true
        }
    }

    fun playAudioFromUrl(audioUrl: String) {
        viewModelScope.launch {
            service.playAudioFromUrl(audioUrl)
        }
    }

    fun updateFlashcardToKnow(flashcardId: Int) {
        viewModelScope.launch {
            roomService.updateFlashcardKnowledgeLevel(
                flashcardId,
                KnowledgeLevel.KNOW.value,
                timeToRepeat = 1
            )
        }
    }

    fun updateFlashcardToSomewhatKnow(flashcardId: Int) {
        viewModelScope.launch {
            roomService.updateFlashcardKnowledgeLevel(
                flashcardId,
                KnowledgeLevel.SOMEWHAT_KNOW.value,
                timeToRepeat = 0
            )
        }
    }

    fun updateFlashcardToDoNotKnow(flashcardId: Int) {
        viewModelScope.launch {
            roomService.updateFlashcardKnowledgeLevel(
                flashcardId,
                KnowledgeLevel.DO_NOT_KNOW.value,
                timeToRepeat = 0
            )
        }
    }
}