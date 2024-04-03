package com.example.loop_new.presentation.screens.story_section.story_check_all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryCheckAllViewModel(private val firebaseService: FirebaseService, private val category: String) : ViewModel() {

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories

    init {
        viewModelScope.launch {
            fetchAllStoriesFromOneCategory()
        }
    }

    private suspend fun fetchAllStoriesFromOneCategory() {
        val favoriteStories = firebaseService.fetchAllStoriesFromOneCategory(category)
        _stories.value = favoriteStories
    }
}