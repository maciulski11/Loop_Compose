package com.example.loop_new.presentation.screens.story_section.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Category
import com.example.loop_new.domain.services.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryViewModel(private val firebaseService: FirebaseService) : ViewModel() {

    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList: StateFlow<List<Category>> = _categoryList

    init {
        fetchListOfStoriesForAllCategories()
    }

    private fun fetchListOfStoriesForAllCategories() {
        viewModelScope.launch {
            firebaseService.fetchListOfStory().collect { loadedCategory ->
                _categoryList.value = _categoryList.value + loadedCategory
            }
        }
    }
}
