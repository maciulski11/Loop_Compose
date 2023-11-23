package com.example.loop_new.presentation.screens.box

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.InterfaceFirebaseService
import com.example.loop_new.domain.services.InterfaceService
import kotlinx.coroutines.launch

class BoxViewModel(
    private val interfaceFirebaseServices: InterfaceFirebaseService,
    private val interfaceService: InterfaceService,
    boxUid: String
) : ViewModel() {

    val flashcardList: MutableState<List<Flashcard>?> = mutableStateOf(null)

    init {
        fetchListOfFlashcard(boxUid)
    }

    private fun fetchListOfFlashcard(boxUid: String) {
        viewModelScope.launch {
            try {
                val flashcardFlow = interfaceFirebaseServices.fetchListOfFlashcardInBox(boxUid)

                flashcardFlow.collect {
                    flashcardList.value = it
                }

                Log.d(LogTags.BOX_VIEW_MODEL, "fetchListOfFlashcard: Success")

            } catch (e: Exception) {

                Log.e(LogTags.BOX_VIEW_MODEL, "fetchListOfFlashcard: Error: $e")
            }
        }
    }

    fun deleteFlashcard(boxUid: String, flashcardUid: String) {
        try {
            interfaceFirebaseServices.deleteFlashcard(boxUid, flashcardUid)
            Log.d(LogTags.BOX_VIEW_MODEL, "deleteFlashcard: Success")

        } catch (e: Exception) {

            Log.e(LogTags.BOX_VIEW_MODEL, "deleteFlashcard: Error: $e")
        }
    }

    fun playAudioFromUrl(audioUrl: String) {
        viewModelScope.launch {
            interfaceService.playAudioFromUrl(audioUrl)
        }
    }
}