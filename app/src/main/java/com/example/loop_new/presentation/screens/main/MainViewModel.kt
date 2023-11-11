package com.example.loop_new.presentation.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.LogTags
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.services.InterfaceFirebaseService
import kotlinx.coroutines.launch

class MainViewModel(private val interfaceFirebaseService: InterfaceFirebaseService) : ViewModel() {

    val boxList: MutableState<List<Box>?> = mutableStateOf(null)

    init {
        fetchListOfBox()
    }

    fun addBox(name: String, describe: String) {
        val box = Box(name = name, describe = describe)
        viewModelScope.launch {
            try {
                interfaceFirebaseService.addBox(box)

                Log.d(LogTags.MAIN_VIEW_MODEL, "addBox: Correct addition of box")

            } catch (e: Exception) {

                Log.e(LogTags.MAIN_VIEW_MODEL, "addBox: Error: $e")
            }
        }
    }

    private fun fetchListOfBox() {
        viewModelScope.launch {
            try {
                val boxFlow = interfaceFirebaseService.fetchListOfBox()
                boxFlow.collect { boxes ->
                    boxList.value = boxes

                    Log.d(LogTags.MAIN_VIEW_MODEL, "fetchListOfBox: Success!")
                }
            } catch (e: Exception) {

                Log.e(LogTags.MAIN_VIEW_MODEL, "fetchListOfBox: Error: $e")
            }
        }
    }
}



