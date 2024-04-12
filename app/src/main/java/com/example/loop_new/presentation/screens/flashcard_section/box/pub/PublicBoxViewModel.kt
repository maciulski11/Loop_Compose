package com.example.loop_new.presentation.screens.flashcard_section.box.pub

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.FirebaseService
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class PublicBoxViewModel(private val firebaseService: FirebaseService) : ViewModel() {

    val publicBoxList = mutableStateListOf<Box>()
    private var hasMoreData = mutableStateOf(false)

    private val _isListEmpty = mutableStateOf(true)
    val isListEmpty: Boolean
        get() = _isListEmpty.value

    // Property specifying whether more boxes can be loaded
    val canLoadMore: Boolean
        get() = lastVisibleDocument != null

    private var lastVisibleDocument: DocumentSnapshot? = null

    init {
        checkRepeatCollectionWhetherIsEmpty()
        setupRepeatCollectionListener()
        fetchListOfPublicBox()
    }

    private fun fetchListOfPublicBox() {
        viewModelScope.launch {
            firebaseService.fetchListOfPublicBox(null).collect { (loadedBoxes, lastDoc) ->
                publicBoxList.addAll(loadedBoxes)
                lastVisibleDocument = lastDoc
            }
        }
    }

    fun loadMoreBoxes() {
        viewModelScope.launch {
            lastVisibleDocument?.let { lastDoc ->
                firebaseService.fetchListOfPublicBox(lastDoc).collect { (loadedBoxes, lastDoc) ->
                    if (loadedBoxes.isNotEmpty()) {
                        publicBoxList.addAll(loadedBoxes)
                        lastVisibleDocument = lastDoc
                    }
                    // Update the status of 'hasMoreData'
                    hasMoreData.value = loadedBoxes.isEmpty()
                }
            }
        }
    }

    private fun setupRepeatCollectionListener() {
        viewModelScope.launch {
            firebaseService.setupRepeatCollectionListener { isEmpty ->
                _isListEmpty.value = isEmpty
            }
        }
    }

    private fun checkRepeatCollectionWhetherIsEmpty() {
        viewModelScope.launch {
            _isListEmpty.value = firebaseService.checkRepeatCollectionWhetherIsEmpty()
        }
    }
}
