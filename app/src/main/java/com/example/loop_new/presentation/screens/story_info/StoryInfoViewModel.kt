package com.example.loop_new.presentation.screens.story_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.domain.services.TranslateService
import kotlinx.coroutines.launch

class StoryInfoViewModel(
    private val firebaseService: FirebaseService,
    storyUid: String,
) : ViewModel() {

    private var _storyDetails: Story? = null
    val storyDetails: Story? get() = _storyDetails

    init {
        fetchStory(storyUid)
    }

    private fun fetchStory(storyUid: String) {
        viewModelScope.launch {
            _storyDetails = firebaseService.fetchStory(storyUid)
        }
    }
}