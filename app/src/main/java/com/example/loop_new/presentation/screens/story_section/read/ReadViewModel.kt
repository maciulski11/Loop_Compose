package com.example.loop_new.presentation.screens.story_section.read

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.FavoriteStoryWithChapters
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.services.DictionaryService
import com.example.loop_new.domain.services.TranslateService
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReadViewModel(
    private val translateService: TranslateService,
    private val dictionaryService: DictionaryService,
    private val roomService: RoomService,
    storyUid: String,
) : ViewModel() {

    private val _storyDetails = MutableStateFlow<FavoriteStoryWithChapters?>(null)
    val storyDetails: StateFlow<FavoriteStoryWithChapters?> = _storyDetails

    val privateBoxList = mutableStateListOf<Box>()

    var translate: String by mutableStateOf("")
    private var meaning: String by mutableStateOf("")
    private var example: String by mutableStateOf("")
    private var pronunciation: String by mutableStateOf("")
    private var audioUrl: String by mutableStateOf("")

    init {
        fetchStory(storyUid)
        fetchListOfPrivateBox()
    }

    private fun fetchStory(storyUid: String) {
        viewModelScope.launch {
            roomService.fetchFavoriteStoryWithChapters(storyUid)
                .collect { story ->
                    _storyDetails.value = story
                }
        }
    }

    fun fetchInfoOfWord(word: String) {
        viewModelScope.launch {
            translateService.onTranslationResult(word) { translateWord ->

                translate = translateWord
                Log.d(LogTags.ADD_FLASHCARD_VIEW_MODEL, "Translation successful: $translateWord")
            }

            dictionaryService.onFetchWordInfo(word) { flashcard ->

                meaning = flashcard.meaning.toString()
                example = flashcard.example.toString()
                pronunciation = flashcard.pronunciation.toString()
                audioUrl = flashcard.audioUrl.toString()

                Log.d(
                    LogTags.ADD_FLASHCARD_VIEW_MODEL,
                    "Fetch word info successful for word: $word"
                )
            }
        }
    }

    fun addFlashcard(word: String, boxId: Int, boxUid: String) {
        val flashcardData = Flashcard(
            word = word,
            translate = translate,
            meaning = meaning,
            example = example,
            pronunciation = pronunciation,
            audioUrl = audioUrl,
            boxId = boxId,
            boxUid = boxUid
        )

        viewModelScope.launch {
            try {
                roomService.insertFlashCard(flashcardData)
                Log.d(LogTags.ADD_FLASHCARD_VIEW_MODEL, "addFlashcard to room: Success")

            } catch (e: Exception) {

                Log.e(LogTags.ADD_FLASHCARD_VIEW_MODEL, "addFlashcard to room: Error: $e")
            }
        }
    }

    private fun fetchListOfPrivateBox() {
        viewModelScope.launch {
            roomService.fetchBoxes().collect { loadedBoxes ->
                privateBoxList.clear()
                loadedBoxes.filter { it.addFlashcardFromStory != false }.forEach {
                    privateBoxList.add(it)
                }
            }
        }
    }
}
