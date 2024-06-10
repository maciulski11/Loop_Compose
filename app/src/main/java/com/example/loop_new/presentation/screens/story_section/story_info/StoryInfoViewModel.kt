package com.example.loop_new.presentation.screens.story_section.story_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.launch

class StoryInfoViewModel(
    private val firebaseService: FirebaseService,
    private val roomService: RoomService,
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

    fun addStoryUidToViewList(uid: String) {
        firebaseService.addStoryUidToViewList(uid)
    }

    fun addStoryToFavoriteSection1(story: Story) {
        viewModelScope.launch {
            roomService.insertStoryWithTextContents(story)
        }
    }

    fun deleteStory(storyId: Int) {
        viewModelScope.launch {
            roomService.deleteStory(storyId)
        }
    }
}