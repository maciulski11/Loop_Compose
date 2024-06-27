package com.example.loop_new.presentation.screens.story_section.story_favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Favorite
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryFavoriteViewModel(
    private val roomService: RoomService
) : ViewModel() {

    private val _stories = MutableStateFlow<List<Favorite>>(emptyList())
    val stories: StateFlow<List<Favorite>> = _stories

    init {
        viewModelScope.launch {
            fetchFavoriteStories()
        }
    }

    private suspend fun fetchFavoriteStories() {

        viewModelScope.launch {
            roomService.fetchStories().collect { story ->
                _stories.value = story
            }
        }
    }

    fun deleteStory(storyId: Int) {
        viewModelScope.launch {
            roomService.deleteStory(storyId)
        }
    }
}