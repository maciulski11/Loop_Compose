package com.example.loop_new.presentation.screens.story_section.story_favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryFavoriteViewModel(private val firebaseService: FirebaseService) : ViewModel() {

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            fetchFavoriteStories()
        }
    }

    private suspend fun fetchFavoriteStories() {
        // Set the loading state to true before downloading the data
        _isLoading.value = true

        try {
            val favoriteStories = firebaseService.fetchFavoriteStories()
            _stories.value = favoriteStories

        } catch (e: Exception) {
            // Obsługa błędów, np. wyświetlenie komunikatu użytkownikowi

        } finally {
            // Data is finished downloading, set the loading status to false
            _isLoading.value = false
        }
    }
}