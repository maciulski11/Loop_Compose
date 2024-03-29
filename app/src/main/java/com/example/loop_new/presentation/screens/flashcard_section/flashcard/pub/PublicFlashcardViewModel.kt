package com.example.loop_new.presentation.screens.flashcard_section.flashcard.pub

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch

class PublicFlashcardViewModel(
    private val firebaseService: FirebaseService,
    private val mainViewModel: MainViewModel,
    boxUid: String,
) : ViewModel() {

    val flashcardList: MutableState<List<Flashcard>?> = mutableStateOf(null)

    init {
        fetchListOfFlashcard(boxUid)
    }

    private fun fetchListOfFlashcard(boxUid: String) {
        viewModelScope.launch {
            try {
                val flashcardFlow = firebaseService.fetchListOfFlashcardInPublicBox(boxUid)

                flashcardFlow.collect {
                    flashcardList.value = it
                }

                Log.d(LogTags.FLASHCARD_VIEW_MODEL, "fetchListOfFlashcard: Success")

            } catch (e: Exception) {

                Log.e(LogTags.FLASHCARD_VIEW_MODEL, "fetchListOfFlashcard: Error: $e")
            }
        }
    }

    fun addPublicBoxToPrivateSection(boxUid: String) {
        viewModelScope.launch {
            try {
                firebaseService.addPublicBoxToPrivateSection(boxUid)
                Log.d(LogTags.FLASHCARD_VIEW_MODEL, "addBoxToUserLearningSection: Success")

            } catch (e: Exception) {

                Log.e(LogTags.FLASHCARD_VIEW_MODEL, "addBoxToUserLearningSection: Error: $e")
            }
        }
    }

    fun playAudioFromUrl(audioUrl: String) {
        mainViewModel.playAudioFromUrl(audioUrl)
    }
}