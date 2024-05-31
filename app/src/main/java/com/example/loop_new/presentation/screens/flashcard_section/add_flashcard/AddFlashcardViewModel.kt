package com.example.loop_new.presentation.screens.flashcard_section.add_flashcard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.DictionaryService
import com.example.loop_new.domain.services.TranslateService
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.launch

class AddFlashcardViewModel(
    private val translateService: TranslateService,
    private val dictionaryService: DictionaryService,
    private val roomService: RoomService,
) : ViewModel() {

    var translate: String by mutableStateOf("")
    var meaning: String by mutableStateOf("")
    var example: String by mutableStateOf("")
    var pronunciation: String by mutableStateOf("")
    private var audioUrl: String by mutableStateOf("")

    fun insertFlashcard(
        word: String,
        translate: String,
        meaning: String,
        example: String,
        pronunciation: String,
        boxUid: String,
        boxId: Int,
    ) {

        val flashcardData = Flashcard(
            word = word,
            translate = translate,
            meaning = meaning,
            example = example,
            pronunciation = pronunciation,
            audioUrl = audioUrl,
            boxUid = boxUid,
            boxId = boxId
        )

        viewModelScope.launch {
            roomService.insertFlashCard(flashcardData)
        }
    }

    fun fetchInfoOfWord(word: String) {

        translateService.onTranslationResult(word) { translateWord ->

            translate = translateWord
            Log.d(LogTags.ADD_FLASHCARD_VIEW_MODEL, "Translation successful: $translateWord")
        }

        dictionaryService.onFetchWordInfo(word) { flashcard ->

            meaning = flashcard.meaning.toString()
            example = flashcard.example.toString()
            pronunciation = flashcard.pronunciation.toString()
            audioUrl = flashcard.audioUrl.toString()

            Log.d(LogTags.ADD_FLASHCARD_VIEW_MODEL, "Fetch word info successful for word: $word")
        }
    }
}