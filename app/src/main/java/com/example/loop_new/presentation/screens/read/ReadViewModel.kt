package com.example.loop_new.presentation.screens.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import kotlinx.coroutines.launch

class ReadViewModel(private val firebaseService: FirebaseService, storyUid: String): ViewModel() {

    private var _story: Story? = null
    val story: Story? get() = _story

    init {
        fetchStory(storyUid)
    }

    private fun fetchStory(storyUid: String) {
        viewModelScope.launch {
            _story = firebaseService.fetchStory(storyUid)
        }
    }
}