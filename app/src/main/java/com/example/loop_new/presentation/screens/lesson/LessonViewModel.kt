package com.example.loop_new.presentation.screens.lesson

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import com.example.loop_new.domain.services.InterfaceFirebaseService
import com.example.loop_new.presentation.navigation.NavigationSupport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class LessonViewModel(
    private val interfaceFirebaseServices: InterfaceFirebaseService,
    boxUid: String,
) : ViewModel() {

    val flashcardList = MutableStateFlow<List<Flashcard>>(emptyList())
    private val _currentFlashcard = MutableStateFlow<Flashcard?>(null)
    val currentFlashcard: StateFlow<Flashcard?> = _currentFlashcard

    var progress by mutableFloatStateOf(0f)
    var progressText: String by mutableStateOf("")

    init {
        fetchListOfFlashcard(boxUid)
    }

    private fun fetchListOfFlashcard(boxUid: String) {
        viewModelScope.launch {
            try {
                interfaceFirebaseServices.fetchListOfFlashcardInLesson(boxUid)
                    .collect { newFlashcardList ->
                        flashcardList.value = newFlashcardList
                        _currentFlashcard.value = newFlashcardList.firstOrNull()

                        progressText = currentFlashcard.let { "0 / ${flashcardList.value.size}" }

                    }

                Log.d(LogTags.LESSON_VIEW_MODEL, "fetchListOfFlashcard: Success!")

            } catch (e: Exception) {

                Log.e(LogTags.LESSON_VIEW_MODEL, "fetchListOfFlashcard: Error: $e")
            }
        }
    }

    fun moveToNextFlashcard(navController: NavController, boxUid: String) {
        val currentList = flashcardList.value
        val currentIndex = currentList.indexOf(_currentFlashcard.value)
        val totalFlashcards = currentList.size

        if (currentIndex >= 0 && currentIndex < currentList.size - 1) {

            _currentFlashcard.value = currentList[currentIndex + 1]
            // Oblicz postęp na podstawie indeksu
            progress = calculateProgress(currentIndex + 1, totalFlashcards)

        } else if (currentIndex == currentList.size - 1) {

            progress = calculateProgress(currentIndex + 1, totalFlashcards)
            // Nawiguj do innego ekranu, jeśli jesteś na ostatniej karcie
            navController.navigate("${NavigationSupport.BoxScreen}/$boxUid")

            // Zablokuj przewijanie, gdy użytkownik osiągnął ostatnią kartę
            flashcardList.value = emptyList()
            _currentFlashcard.value = null

        } else {

            Log.d(LogTags.LESSON_VIEW_MODEL, "moveToNextFlashcard: No next flashcard available")
        }

        progressText = currentFlashcard.let { "${currentIndex + 1} / $totalFlashcards" }
    }

    private fun calculateProgress(currentIndex: Int, totalFlashcards: Int): Float {
        return (currentIndex.toFloat() / totalFlashcards.toFloat()) * 100f
    }

    fun setKnowledgeLevelOfFlashcard(
        boxUid: String,
        flashcardUid: String,
        knowledgeLevel: KnowledgeLevel
    ) {
        viewModelScope.launch {
            interfaceFirebaseServices.setKnowledgeLevelOfFlashcard(
                boxUid,
                flashcardUid,
                knowledgeLevel
            )
        }
    }

    fun playAudioFromUrl(url: String) {
        MediaPlayer().apply {
            try {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener {
                    start()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}