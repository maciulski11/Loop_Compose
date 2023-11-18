package com.example.loop_new.presentation.screens.box

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.InterfaceFirebaseService
import kotlinx.coroutines.launch
import java.io.IOException

class BoxViewModel(
    private val interfaceFirebaseServices: InterfaceFirebaseService,
    boxUid: String
) : ViewModel() {

    val flashcardList: MutableState<List<Flashcard>?> = mutableStateOf(null)

    init {
        fetchListOfFlashcard(boxUid)
    }

    private fun fetchListOfFlashcard(boxUid: String) {
        viewModelScope.launch {
            try {
                val flashcardFlow = interfaceFirebaseServices.fetchListOfFlashcard(boxUid)

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

    // TODO: przeneisc stad i lessonViewModel do jednego!!!
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