package com.example.loop_new.presentation.add_flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.Flashcard
import com.example.loop_new.domain.repository.InterfaceRepository
import kotlinx.coroutines.launch

class AddFlashcardViewModel(private val interfaceRepository: InterfaceRepository): ViewModel() {

    fun addFlashcard(flashcard: Flashcard) {
        val flashcardData = Flashcard(
            word = flashcard.word,
            translate = flashcard.translate,
            meaning = flashcard.meaning,
            example = flashcard.example,
            partOfSpeech = flashcard.partOfSpeech,
            pronunciation = flashcard.pronunciation,
            audioUrl = flashcard.audioUrl,
            isFrontVisible = flashcard.isFrontVisible
        )

        viewModelScope.launch {
            interfaceRepository.addFlashcard(flashcardData, "")
        }
    }
}