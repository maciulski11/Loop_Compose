package com.example.loop_new.presentation.screens.box.priv

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.FirebaseService
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class PrivateBoxViewModel(private val firebaseService: FirebaseService): ViewModel() {

    val privateBoxList = mutableStateListOf<Box>()
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
        fetchListOfPrivateBox()
    }

    private fun fetchListOfPrivateBox() {
        viewModelScope.launch {
            firebaseService.fetchListOfPrivateBox(null).collect { (loadedBoxes, lastDoc) ->
                privateBoxList.addAll(loadedBoxes)
                lastVisibleDocument = lastDoc
            }
        }
    }

    fun loadMorePrivateBoxes() {
        viewModelScope.launch {
            lastVisibleDocument?.let { lastDoc ->
                firebaseService.fetchListOfPrivateBox(lastDoc).collect { (loadedBoxes, lastDoc) ->
                    if (loadedBoxes.isNotEmpty()) {
                        privateBoxList.addAll(loadedBoxes)
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

    fun createBoxInPrivateSection(name: String, describe: String, colorGroup: List<Color>) {

        // Convert colors to HEX format
        val color1 = colorToHex(colorGroup[0])
        val color2 = colorToHex(colorGroup[1])
        val color3 = colorToHex(colorGroup[2])

        val box =
            Box(name = name, describe = describe, color1 = color1, color2 = color2, color3 = color3)
        viewModelScope.launch {
            try {
                firebaseService.createBoxInPrivateSection(box)

                Log.d(LogTags.BOX_VIEW_MODEL, "createBoxInPrivateSection: Correct addition of box")

            } catch (e: Exception) {

                Log.e(LogTags.BOX_VIEW_MODEL, "createBoxInPrivateSection: Error: $e")
            }
        }
    }

    // Helper function to convert color to HEX format
    private fun colorToHex(color: Color): String {
        return "#%02x%02x%02x".format(
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt()
        )
    }
}