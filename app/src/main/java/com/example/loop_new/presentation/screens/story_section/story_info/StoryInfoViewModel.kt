package com.example.loop_new.presentation.screens.story_section.story_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Favorite
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.room.RoomService
import kotlinx.coroutines.launch

class StoryInfoViewModel(
    private val firebaseService: FirebaseService,
    private val roomService: RoomService,
    storyUid: String,
    typeOfDatabase: String,
) : ViewModel() {

    private var _storyDetails: Story? = null
    val storyDetails: Story? get() = _storyDetails

    private var _favoriteDetails: Favorite? = null
    val favoriteDetails: Favorite? get() = _favoriteDetails

    init {
        when (typeOfDatabase) {
            "room" -> {
                fetchStoryFromRoom(storyUid)
            }

            "firebase" -> {
                fetchStoryFromFirebase(storyUid)
            }
        }
    }

    private fun fetchStoryFromFirebase(storyUid: String) {
        viewModelScope.launch {
            _storyDetails = firebaseService.fetchStory(storyUid)
        }
    }

    private fun fetchStoryFromRoom(storyUid: String) {
        viewModelScope.launch {
            _favoriteDetails = roomService.fetchStory(storyUid)
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