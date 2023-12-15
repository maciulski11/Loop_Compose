package com.example.loop_new.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.services.Service
import kotlinx.coroutines.launch

class MainViewModel(private val service: Service): ViewModel() {

    fun playAudioFromUrl(audioUrl: String) {
        viewModelScope.launch {
            service.playAudioFromUrl(audioUrl)
        }
    }
}