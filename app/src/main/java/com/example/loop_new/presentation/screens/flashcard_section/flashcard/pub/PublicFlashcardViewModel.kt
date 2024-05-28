package com.example.loop_new.presentation.screens.flashcard_section.flashcard.pub

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.presentation.viewModel.MainViewModel
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.launch

class PublicFlashcardViewModel(
    private val firebaseService: FirebaseService,
    private val mainViewModel: MainViewModel,
    private val roomService: RoomService,
    boxUid: String,
) : ViewModel() {


    val flashcardList: MutableState<List<Flashcard>> = mutableStateOf(emptyList())

    init {
        fetchListOfFlashcard(boxUid)
    }

    private fun fetchListOfFlashcard(boxUid: String) {
        viewModelScope.launch {
            try {
                val flashcardFlow = firebaseService.fetchListOfFlashcardInPublicBox(boxUid)

                flashcardFlow.collect { flashcards ->
                    flashcardList.value = flashcards
                }

                Log.d(LogTags.FLASHCARD_VIEW_MODEL, "fetchListOfFlashcard: Success")

            } catch (e: Exception) {
                Log.e(LogTags.FLASHCARD_VIEW_MODEL, "fetchListOfFlashcard: Error: $e")
            }
        }
    }

    fun playAudioFromUrl(audioUrl: String) {
        mainViewModel.playAudioFromUrl(audioUrl)
    }

    fun addPublicBoxToPrivateSection(box: Box, flashcards: List<Flashcard>) {
        viewModelScope.launch {
            roomService.addPrivateBoxWithFlashcards(box, flashcards)
        }
    }
}