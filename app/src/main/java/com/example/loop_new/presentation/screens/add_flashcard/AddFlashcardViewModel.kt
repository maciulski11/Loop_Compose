package com.example.loop_new.presentation.screens.add_flashcard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.InterfaceDictionaryService
import com.example.loop_new.domain.services.InterfaceFirebaseService
import com.example.loop_new.domain.services.InterfaceTranslateServices
import kotlinx.coroutines.launch

class AddFlashcardViewModel(
    private val interfaceFirebaseService: InterfaceFirebaseService,
    private val interfaceTranslateServices: InterfaceTranslateServices,
    private val interfaceDictionaryService: InterfaceDictionaryService
) : ViewModel() {

    var translate: String by mutableStateOf("")
    var meaning: String by mutableStateOf("")
    var example: String by mutableStateOf("")
    private var pronunciation: String by mutableStateOf("")
    private var audioUrl: String by mutableStateOf("")

    fun addFlashcard(
        word: String,
        translate: String,
        meaning: String,
        example: String,
//        partOfSpeech: String,
//        isFrontVisible: Boolean,
        boxUid: String
    ) {
        val flashcardData = Flashcard(
            word = word,
            translate = translate,
            meaning = meaning,
            example = example,
//            partOfSpeech = partOfSpeech,
            pronunciation = pronunciation,
            audioUrl = audioUrl,
//            isFrontVisible = isFrontVisible
        )

        viewModelScope.launch {
            try {
                interfaceFirebaseService.addFlashcard(flashcardData, boxUid)
                Log.d(LogTags.ADD_FLASHCARD_VIEW_MODEL, "addFlashcard: Success")

            } catch (e: Exception) {

                Log.e(LogTags.ADD_FLASHCARD_VIEW_MODEL, "addFlashcard: Error: $e")
            }
        }
    }

    fun fetchInfoOfWord(word: String) {

        interfaceTranslateServices.onTranslationResult(word) { translateWord ->

            translate = translateWord
            Log.d(LogTags.ADD_FLASHCARD_VIEW_MODEL, "Translation successful: $translateWord")
        }

        interfaceDictionaryService.onFetchWordInfo(word) { flashcard ->

            meaning = flashcard.meaning.toString()
            example = flashcard.example.toString()
            pronunciation = flashcard.pronunciation.toString()
            audioUrl = flashcard.audioUrl.toString()

            Log.d(LogTags.ADD_FLASHCARD_VIEW_MODEL, "Fetch word info successful for word: $word")
        }
    }
}