package com.example.loop_new.presentation.screens.box

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.FirebaseService
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class BoxViewModel(private val firebaseService: FirebaseService) : ViewModel() {

    val publicBoxList = mutableStateListOf<Box>()
    var hasMoreData = mutableStateOf(false)

    private val _isListEmpty = mutableStateOf(true)
    val isListEmpty: Boolean
        get() = _isListEmpty.value

    // Właściwość określająca, czy można załadować więcej boxów
    val canLoadMore: Boolean
        get() = lastVisibleDocument != null

    private var lastVisibleDocument: DocumentSnapshot? = null

    init {
        checkRepeatCollectionWhetherIsEmpty()
        setupRepeatCollectionListener()
        loadInitialBoxes()
    }

    private fun loadInitialBoxes() {
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
                    // Aktualizacja stanu 'hasMoreData'
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

//    private fun fetchListOfBox() {
//        viewModelScope.launch {
//            try {
//                firebaseService.fetchListOfPublicBox().collect { boxes ->
//                    boxList.value = boxes
//                }
//            } catch (e: Exception) {
//                Log.e(LogTags.BOX_VIEW_MODEL, "Error: ${e.message}")
//            }
//        }
//    }
}
