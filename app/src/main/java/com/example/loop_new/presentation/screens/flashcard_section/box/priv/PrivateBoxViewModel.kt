package com.example.loop_new.presentation.screens.flashcard_section.box.priv

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.FirebaseService
import com.example.loop_new.presentation.viewModel.RoomService
import com.example.loop_new.room.LoopDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class PrivateBoxViewModel(
    private val firebaseService: FirebaseService,
    private val roomService: RoomService,
) : ViewModel() {



    private val _boxes = MutableStateFlow<List<Box>>(emptyList())
    val boxes: StateFlow<List<Box>> = _boxes


    init {
//        checkRepeatCollectionWhetherIsEmpty()
//        setupRepeatCollectionListener()
//        fetchListOfPrivateBox()

        fetchBoxes()
    }

    fun insert(name: String, describe: String, colorGroup: List<Color>) {

        val uid = UUID.randomUUID().toString()

        // Convert colors to HEX format
        val color1 = colorToHex(colorGroup[0])
        val color2 = colorToHex(colorGroup[1])
        val color3 = colorToHex(colorGroup[2])

        val box =
            Box(
                name = name,
                describe = describe,
                uid = uid,
                color1 = color1,
                color2 = color2,
                color3 = color3,
                permissionToEdit = true,
                addFlashcardFromStory = true
            )

        viewModelScope.launch {
            roomService.insertBox(box)
        }
    }

    fun delete(uid: String) {
        viewModelScope.launch {
            roomService.deleteBox(uid)
            fetchBoxes()
        }
    }

    private fun fetchBoxes() {
        viewModelScope.launch {
            roomService.fetchBoxes().collect { boxes ->
                _boxes.value = boxes
            }
        }
    }

    val privateBoxList = mutableStateListOf<Box>()


    private val _isListEmpty = mutableStateOf(true)
    val isListEmpty: Boolean
        get() = _isListEmpty.value

//    init {
//        checkRepeatCollectionWhetherIsEmpty()
//        setupRepeatCollectionListener()
////        fetchListOfPrivateBox()
//
//        fetchBoxes()
//    }

    private fun fetchListOfPrivateBox() {
        viewModelScope.launch {
            firebaseService.fetchListOfPrivateBox().collect { loadedBoxes ->
                privateBoxList.clear()
                privateBoxList.addAll(loadedBoxes)
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
            Box(
                name = name,
                describe = describe,
                color1 = color1,
                color2 = color2,
                color3 = color3,
                permissionToEdit = true,
                addFlashcardFromStory = true
            )
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

//    fun deleteBox(boxUid: String) {
//        firebaseService.deleteBox(boxUid)
//    }
}