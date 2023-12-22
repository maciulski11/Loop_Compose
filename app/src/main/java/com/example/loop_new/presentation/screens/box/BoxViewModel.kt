package com.example.loop_new.presentation.screens.box

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.FirebaseService
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BoxViewModel(
    private val firebaseService: FirebaseService,
    private val fetchBoxes: suspend () -> Flow<List<Box>>,
) : ViewModel() {

    val boxList = mutableStateOf<List<Box>?>(null)

    private val firestore = Firebase.firestore
    private val _isListEmpty = mutableStateOf(true)
    val isListEmpty: Boolean
        get() = _isListEmpty.value

    init {
        // Wywołaj funkcję sprawdzającą stan listy w Firestore w konstruktorze lub odpowiednim miejscu
        checkRepeatCollectionWhetherIsEmpty()
        setupRepeatCollectionListener()
        fetchListOfBox()
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

    private fun fetchListOfBox() {
        viewModelScope.launch {
            try {
                fetchBoxes().collect { boxes ->
                    boxList.value = boxes
                }
            } catch (e: Exception) {
                Log.e(LogTags.BOX_VIEW_MODEL, "Error: ${e.message}")
            }
        }
    }
}

// ViewModel Common Factory
class BoxViewModelFactory(
    private val firebaseService: FirebaseService,
    private val isPublic: Boolean,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = if (isPublic) {
            PublicBoxViewModel(firebaseService)
        } else {
            PrivateBoxViewModel(firebaseService)
        }
        if (modelClass.isAssignableFrom(viewModel::class.java)) {
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// ViewModel Implementations
class PublicBoxViewModel(firebaseService: FirebaseService) :
    BoxViewModel(firebaseService, firebaseService::fetchListOfBox)

class PrivateBoxViewModel(firebaseService: FirebaseService) :
    BoxViewModel(firebaseService, firebaseService::fetchListOfBoxUser)