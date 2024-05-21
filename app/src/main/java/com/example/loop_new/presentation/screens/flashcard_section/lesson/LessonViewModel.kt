package com.example.loop_new.presentation.screens.flashcard_section.lesson

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.presentation.viewModel.MainViewModel
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonViewModel(
    private val firebaseService: FirebaseService,
    private val mainViewModel: MainViewModel,
    private val roomService: RoomService,
    boxId: Int,
) : ViewModel() {

    private val _flashcardList = MutableLiveData<List<Flashcard>>()
    val flashcardList: LiveData<List<Flashcard>> = _flashcardList

    private val _currentFlashcard = MutableLiveData<Flashcard?>(null)
    val currentFlashcard: LiveData<Flashcard?> = _currentFlashcard

    var progress by mutableFloatStateOf(0f)
    var progressText by mutableStateOf("")

    init {
        fetchFlashcardsForBox(boxId)
    }

    private fun fetchFlashcardsForBox(boxId: Int) {
        viewModelScope.launch {
            try {
                val boxWithFlashcards = withContext(Dispatchers.IO) {
                    roomService.fetchBoxWithFlashcards(boxId)
                }
                _flashcardList.postValue(boxWithFlashcards.flashcards)
                _currentFlashcard.value = boxWithFlashcards.flashcards.firstOrNull()

                progressText = _currentFlashcard.value?.let { "0 / ${boxWithFlashcards.flashcards.size}" }
                    ?: "0 / 0"

                Log.d("FlashcardViewModel", "getFlashcardsForBox: Success!")
            } catch (e: Exception) {
                Log.e("FlashcardViewModel", "getFlashcardsForBox: Error: $e")
            }
        }
    }

    fun addLessonStatsToFirestore(flashcardUid: String, status: String) {
        viewModelScope.launch {
            firebaseService.addLessonStatsToFirestore(flashcardUid, status)
        }
    }

    private fun calculateProgress(currentIndex: Int, totalFlashcards: Int): Float {
        return (currentIndex.toFloat() / totalFlashcards.toFloat()) * 100f
    }

    fun moveToNextFlashcard(navController: NavController) {
        val currentList = flashcardList.value
            ?: return // Pobierz listę kartek lub zwróć null, jeśli lista jest pusta
        val currentIndex = currentList.indexOf(_currentFlashcard.value)
        val totalFlashcards = currentList.size

        if (currentIndex >= 0 && currentIndex < totalFlashcards - 1) {
            _currentFlashcard.value = currentList[currentIndex + 1]
            progress = calculateProgress(currentIndex + 1, totalFlashcards)
        } else if (currentIndex == totalFlashcards - 1) {
            progress = calculateProgress(currentIndex + 1, totalFlashcards)
            navController.navigate(NavigationSupport.PrivateBoxScreen)
            _currentFlashcard.value = null
        } else {
            Log.d(LogTags.LESSON_VIEW_MODEL, "moveToNextFlashcard: No next flashcard available")
        }

        progressText = currentFlashcard.value?.let { "${currentIndex + 1} / $totalFlashcards" }
            ?: "0 / 0" // Sprawdź, czy currentFlashcard nie jest nullem, aby uniknąć NullPointerException
    }

    fun updateFlashcardToKnow(flashcardId: Int, newKnowledgeLevel: String) {
        viewModelScope.launch {
            roomService.updateFlashCardKnowledgeLevel(flashcardId, newKnowledgeLevel)
        }
    }

    fun updateFlashcardToSomewhatKnow(flashcardId: Int, newKnowledgeLevel: String) {
        viewModelScope.launch {
            roomService.updateFlashCardKnowledgeLevel(flashcardId, newKnowledgeLevel)
        }
    }

    fun updateFlashcardToDoNotKnow(flashcardId: Int, newKnowledgeLevel: String) {
        viewModelScope.launch {
            roomService.updateFlashCardKnowledgeLevel(flashcardId, newKnowledgeLevel)
        }
    }

    fun playAudioFromUrl(audioUrl: String) {
        mainViewModel.playAudioFromUrl(audioUrl)
    }

    fun deleteFlashcardFromRepeatSection(flashcardUid: String) {
        viewModelScope.launch {
            firebaseService.deleteFlashcardFromRepeatSection(flashcardUid)
        }
    }
}