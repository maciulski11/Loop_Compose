package com.example.loop_new.presentation.add_flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.Flashcard
import com.example.loop_new.domain.repository.InterfaceRepository
import kotlinx.coroutines.launch

class AddFlashcardViewModel(private val interfaceRepository: InterfaceRepository) : ViewModel() {

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
            interfaceRepository.addFlashcard(flashcardData, boxUid)
        }
    }
}