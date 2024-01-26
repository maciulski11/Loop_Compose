package com.example.loop_new.presentation.screens.story

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import kotlinx.coroutines.launch

class StoryViewModel(private val firebaseService: FirebaseService) : ViewModel() {

    val storyList = mutableStateListOf<Story>()

    init {
        fetchListOfStory()
    }

    private fun fetchListOfStory() {
        viewModelScope.launch {
            firebaseService.fetchListOfStory().collect { loadedStory ->
                storyList.addAll(loadedStory)
            }
        }
    }
}