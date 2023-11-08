package com.example.loop_new.presentation.screens.add_flashcard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var meaning: String by mutableStateOf("")
    var example: String by mutableStateOf("")
    var translate: String by mutableStateOf("")

    fun addFlashcard(
        word: String,
        translate: String,
//        meaning: String,
//        example: String,
//        partOfSpeech: String,
        pronunciation: String,
//        audioUrl: String,
//        isFrontVisible: Boolean,
        boxUid: String
    ) {
        val flashcardData = Flashcard(
            word = word,
            translate = translate,
//            meaning = meaning,
//            example = example,
//            partOfSpeech = partOfSpeech,
            pronunciation = pronunciation,
//            audioUrl = audioUrl,
//            isFrontVisible = isFrontVisible
        )

        viewModelScope.launch {
            interfaceFirebaseService.addFlashcard(flashcardData, boxUid)
        }
    }

    fun fetchInfoOfWord(word: String) {

        interfaceTranslateServices.onTranslationResult(word) { translateWord ->

            translate = translateWord
        }

        interfaceDictionaryService.onFetchWordInfo(word) {flashcard ->

            meaning = flashcard.meaning.toString()
            example = flashcard.example.toString()
        }
    }
}