package com.example.loop_new.presentation.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.repository.InterfaceRepository
import kotlinx.coroutines.launch

class MainViewModel(private val interfaceRepository: InterfaceRepository): ViewModel() {

    val boxList: MutableState<List<Box>?> = mutableStateOf(null)

    init {
        fetchListOfBox()
    }

    fun addBox(name: String, describe: String) {
        val box = Box(name = name, describe = describe)
        viewModelScope.launch {
            interfaceRepository.addBox(box)
        }
    }

    private fun fetchListOfBox() {
        viewModelScope.launch {
            val boxFlow = interfaceRepository.fetchListOfBox()
            boxFlow.collect {//nasluchiwanie na strumien boxFlow przy uzyciu fun collect
                boxList.value = it
            }
        }
    }
}



