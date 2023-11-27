package com.example.loop_new.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.services.InterfaceService
import kotlinx.coroutines.launch

class MainViewModel(private val interfaceService: InterfaceService): ViewModel() {

    fun playAudioFromUrl(audioUrl: String) {
        viewModelScope.launch {
            interfaceService.playAudioFromUrl(audioUrl)
        }
    }
}