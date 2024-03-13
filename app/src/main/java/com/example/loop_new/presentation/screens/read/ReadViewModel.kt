package com.example.loop_new.presentation.screens.read

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.domain.services.TranslateService
import kotlinx.coroutines.launch

class ReadViewModel(
    private val firebaseService: FirebaseService,
    private val translateService: TranslateService,
    storyUid: String,
) : ViewModel() {

    private var _storyDetails: Story? = null
    val storyDetails: Story? get() = _storyDetails
    var translate: String by mutableStateOf("")

    val privateBoxList = mutableStateListOf<Box>()


    init {
        fetchStory(storyUid)
        fetchListOfPrivateBox()
    }

    private fun fetchStory(storyUid: String) {
        viewModelScope.launch {
            _storyDetails = firebaseService.fetchStory(storyUid)
        }
    }

    fun translateWord(word: String) {
        translateService.onTranslationResult(word) { translateWord ->

            translate = translateWord
            Log.d(LogTags.ADD_FLASHCARD_VIEW_MODEL, "Translation successful: $translateWord")
        }
    }

    private fun fetchListOfPrivateBox() {
        viewModelScope.launch {
            firebaseService.fetchListOfPrivateBox().collect { loadedBoxes ->
                privateBoxList.clear()
                loadedBoxes.filter { it.addFlashcardFromStory != false }.forEach {
                    privateBoxList.add(it)
                }
            }
        }
    }
}
