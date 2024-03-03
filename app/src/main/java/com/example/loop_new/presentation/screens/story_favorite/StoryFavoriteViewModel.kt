package com.example.loop_new.presentation.screens.story_favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryFavoriteViewModel(private val firebaseService: FirebaseService): ViewModel() {

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories

    init {
        viewModelScope.launch {
            fetchFavoriteStories()
        }
    }

    private suspend fun fetchFavoriteStories() {
        val favoriteStories = firebaseService.fetchFavoriteStories()
        _stories.value = favoriteStories
    }
}