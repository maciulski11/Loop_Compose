package com.example.loop_new.presentation.screens.flashcard_section.repeat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.RepeatSection
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.presentation.viewModel.MainViewModel
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepeatViewModel(
    private val roomService: RoomService,
    private val mainViewModel: MainViewModel
) : ViewModel() {

    private val _repeatSectionList = MutableStateFlow<List<RepeatSection>>(emptyList())
    val repeatSectionList: StateFlow<List<RepeatSection>> = _repeatSectionList

    private val _currentRepeatSection = MutableStateFlow<RepeatSection?>(null)
    val currentRepeatSection: StateFlow<RepeatSection?> = _currentRepeatSection

    var progress by mutableFloatStateOf(0f)
    var progressText: String by mutableStateOf("")

    init {
        fetchListOfRepeatSection()
    }

    private fun fetchListOfRepeatSection() {
        viewModelScope.launch {
            try {
                val newRepeatSectionList = roomService.fetchFlashcardsInRepeatSection()
                _repeatSectionList.value = newRepeatSectionList
                Log.d("RepeatViewModel", "New repeat section list: $newRepeatSectionList")

                if (newRepeatSectionList.isNotEmpty()) {
                    _currentRepeatSection.value = newRepeatSectionList.first()
                    progressText = "1 / ${newRepeatSectionList.size}"
                } else {
                    _currentRepeatSection.value = null
                    progressText = "0 / 0"
                }

                Log.d(LogTags.LESSON_VIEW_MODEL, "fetchListOfRepeatSection: Success!")

            } catch (e: Exception) {
                Log.e(LogTags.LESSON_VIEW_MODEL, "fetchListOfRepeatSection: Error: $e")
            }
        }
    }



    private fun calculateProgress(currentIndex: Int, totalFlashcards: Int): Float {
        return (currentIndex.toFloat() / totalFlashcards.toFloat()) * 100f
    }

    fun moveToNextRepeatSection(navController: NavController) {
        val currentList = repeatSectionList.value
        val currentIndex = currentList.indexOf(_currentRepeatSection.value)
        val totalSections = currentList.size

        if (currentIndex >= 0 && currentIndex < currentList.size - 1) {

            _currentRepeatSection.value = currentList[currentIndex + 1]
            // Calculate progress based on index
            progress = calculateProgress(currentIndex + 1, totalSections)

        } else if (currentIndex == currentList.size - 1) {

            progress = calculateProgress(currentIndex + 1, totalSections)
            // Navigate to another screen if on the last card
            navController.navigate(NavigationSupport.PrivateBoxScreen)

            // Lock scrolling when the user reaches the last card
            _repeatSectionList.value = emptyList()
            _currentRepeatSection.value = null

        } else {

            Log.d(LogTags.LESSON_VIEW_MODEL, "moveToNextRepeatSection: No next repeat section available")
        }

        progressText = currentRepeatSection.value?.let { "${currentIndex + 1} / $totalSections" } ?: "0 / 0"
    }

    fun updateFlashcardToKnow(flashcardId: Int) {
        viewModelScope.launch {
            mainViewModel.updateFlashcardToKnow(flashcardId)
            deleteFlashcardInRepeatSection(flashcardId)
        }
    }

    fun updateFlashcardToSomewhatKnow(flashcardId: Int) {
        viewModelScope.launch {
            mainViewModel.updateFlashcardToSomewhatKnow(flashcardId)
            deleteFlashcardInRepeatSection(flashcardId)
        }
    }

    fun updateFlashcardToDoNotKnow(flashcardId: Int) {
        viewModelScope.launch {
            mainViewModel.updateFlashcardToDoNotKnow(flashcardId)
            deleteFlashcardInRepeatSection(flashcardId)
        }
    }

    private fun deleteFlashcardInRepeatSection(flashcardId: Int) {
        viewModelScope.launch {
            roomService.deleteFlashcardInRepeatSection(flashcardId)
        }
    }

    fun playAudioFromUrl(audioUrl: String) {
        mainViewModel.playAudioFromUrl(audioUrl)
    }
}